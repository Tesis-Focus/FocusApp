package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgregarTareaActivity extends AppCompatActivity {

    Spinner spnBeneficiariosAgrTar;
    EditText txtFechaEntrega;
    EditText txtHoraEntrega;
    ImageButton btnFechaEntrega;
    ImageButton btnHoraEntrega;
    EditText txtNombTarea;
    EditText txtDescripTarea;
    TextView txtMotivacion;
    TextView txtClasificacion;
    TextView txtAreas;
    Spinner sprComplejidad;
    Spinner sprClasificacion;
    Spinner sprActividad;
    Button btnGuardarTarea;
    Calendar calendario;
    DatePickerDialog datePickerDialog;
    ArrayList<String> nombre_Actividades;
    ArrayList<String> desempenoActividades;
    ArrayList<String> id_Actividades;
    ArrayList<String> idActiv;
    ArrayList<Actividad> actividades;
    List<String> nombresBeneficiarios;
    List<Usuario> beneficiarios;

    Integer codigo;
    ArrayAdapter<String> adapterBenef;
    RadioGroup radioGroup;
    RadioButton radioSi;
    RadioButton radioNo;
    boolean motivacion;
    CheckBox checkLectura;
    CheckBox checkEscritura;
    CheckBox checkRazonamiento;
    CheckBox checkIngles;
    CheckBox checkCompentencias;


    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    public static final String PATH_TAREAS = "tareas/";
    public static final String PATH_ACTIVIDADES = "actividades/";
    public static final String PATH_ESTILOS = "estilosAprendizaje/";


    String idBeneficiario;
    String estiloDominante;
    String estiloSecundario;
    Tarea miTarea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        txtClasificacion = findViewById(R.id.textClasificacion);
        txtMotivacion = findViewById(R.id.txtMotivacion);
        spnBeneficiariosAgrTar = findViewById(R.id.spnBeneficiariosAgrTar);
        txtFechaEntrega = findViewById(R.id.txtFechaEntrega);
        btnFechaEntrega = findViewById(R.id.btnFechaEntrega);
        txtHoraEntrega = findViewById(R.id.txtHoraEntrega);
        btnHoraEntrega = findViewById(R.id.btnHoraEntrega);
        txtNombTarea = findViewById(R.id.txtNombTarea);
        txtDescripTarea = findViewById(R.id.txtDescripTarea);
        sprComplejidad = findViewById(R.id.sprComplejidad);
        sprClasificacion = findViewById(R.id.sprClasificacion);
        sprActividad = findViewById(R.id.sprActividad);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);
        nombre_Actividades = new ArrayList<String>();
        desempenoActividades = new ArrayList<String>();
        id_Actividades = new ArrayList<String>();
        actividades = new ArrayList<>();
        idActiv = new ArrayList<String>();
        nombresBeneficiarios = (List<String>) getIntent().getSerializableExtra("nombreBeneficiarios");
        beneficiarios = (List<Usuario>)getIntent().getSerializableExtra("beneficiarios");
        codigo =(Integer) getIntent().getSerializableExtra("codigo");
        adapterBenef = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nombresBeneficiarios);
        adapterBenef.notifyDataSetChanged();
        spnBeneficiariosAgrTar.setAdapter(adapterBenef);
        radioSi = findViewById(R.id.radioSi);
        radioNo = findViewById(R.id.radioNo);
        radioGroup = findViewById(R.id.radioGroup);

        ArrayAdapter<CharSequence> adapterComplej = ArrayAdapter.createFromResource(this, R.array.Complejidad, android.R.layout.simple_spinner_item);
        sprComplejidad.setAdapter(adapterComplej);

        ArrayAdapter<CharSequence> adapterClasif = ArrayAdapter.createFromResource(this, R.array.Clasificacion, android.R.layout.simple_spinner_item);
        sprClasificacion.setAdapter(adapterClasif);


        spnBeneficiariosAgrTar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                nombre_Actividades.clear();
                desempenoActividades.clear();
                id_Actividades.clear();
                idBeneficiario = beneficiarios.get(spnBeneficiariosAgrTar.getSelectedItemPosition()).getIdBeneficiario();
                Log.i("TAG", "idBeneficiario: " + idBeneficiario);

                spinnerActiv(idBeneficiario);
                estilosAprendizaje(idBeneficiario);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(codigo==1){
            miTarea =(Tarea) getIntent().getSerializableExtra("tarea");
            llenarDatos();
        }


        btnFechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        btnHoraEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendario = Calendar.getInstance();
                int hora = calendario.get(Calendar.HOUR_OF_DAY);
                int minutos = calendario.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AgregarTareaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);

                        txtHoraEntrega.setText(hourOfDay +":"+ minutoFormateado);
                        txtHoraEntrega.setError(null);
                    }
                }, hora, minutos,false);
                timePickerDialog.show();

            }
        });


        radioSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                motivacion = radioSi.isChecked();
                txtMotivacion.setError(null);
                //Log.i("ESTADO", "ESTA EN SI");
            }
        });

        radioNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                motivacion = false;
                txtMotivacion.setError(null);
                //Log.i("ESTADO", "ESTA EN NO");
            }
        });

        btnGuardarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if(validarDatos()) {
                    ReglasDecision reglas = new ReglasDecision();
                    Tarea tarea = new Tarea();
                    Tarea nueva = new Tarea();
                    Date fechaAsig = new Date();
                    tarea.setNombre(txtNombTarea.getText().toString());
                    tarea.setDescripcion(txtDescripTarea.getText().toString());
                    tarea.setComplejidad(sprComplejidad.getSelectedItem().toString());
                    tarea.setClasificacion(sprClasificacion.getSelectedItem().toString());
                    String hora = txtHoraEntrega.getText().toString();
                    String mHora = hora.split(":")[0];
                    String mMinuto = hora.split(":")[1];

                    try {
                        tarea.setFechaEntrega(new SimpleDateFormat("dd/MM/yyyy").parse(txtFechaEntrega.getText().toString()));
                        tarea.getFechaEntrega().setHours(Integer.parseInt(mHora));
                        tarea.getFechaEntrega().setMinutes(Integer.parseInt(mMinuto));
                        tarea.getFechaEntrega().setYear(tarea.getFechaEntrega().getYear() + 1900);
                        tarea.getFechaEntrega().setMonth(tarea.getFechaEntrega().getMonth());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tarea.setEstaMotivado(motivacion);
                    String id_Actividad = id_Actividades.get(sprActividad.getSelectedItemPosition());
                    String desempenoActividad = desempenoActividades.get(sprActividad.getSelectedItemPosition());
                    List<String> areasActividad = new ArrayList<String>();
                    areasActividad.addAll(actividades.get(sprActividad.getSelectedItemPosition()).getAreas());

                    List<String> areas = new ArrayList<>();
                    /*
                    if(checkLectura.isChecked()){
                        areas.add("Lectura");
                    }
                    if(checkEscritura.isChecked()){
                        areas.add("Escritura");
                    }
                    if(checkRazonamiento.isChecked()){
                        areas.add("Razonamiento");
                    }
                    if(checkIngles.isChecked()){
                        areas.add("Ingles");
                    }
                    if(checkCompentencias.isChecked()){
                        areas.add("Competencias");
                    }
                     */

                  //  tarea.setAreas(areas);
                 //tarea.getAreas().addAll(areas);
                    Log.i("testAreas", tarea.getAreas().toString());

                    Log.i("test", desempenoActividad);

                    tarea.setIdActividad(id_Actividad);
                    tarea = reglas.asignarTiempos(tarea, desempenoActividad, areasActividad, estiloDominante, estiloSecundario);
                    tarea = reglas.asignarPrioridad(tarea, desempenoActividad, areasActividad);
                    Log.i("TAG", "onClick: " + tarea.getPrioridad());

                    Log.i("TAG", "onClick: agregar tarea a actividad " + id_Actividad + " " + nombre_Actividades.get(sprActividad.getSelectedItemPosition()));

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    fechaAsig.setYear(fechaAsig.getYear() + 1900);
                    fechaAsig.setMonth(fechaAsig.getMonth() );
                    tarea.setFechaAsignacion(fechaAsig);  //El dia que ingresa la tarea
                    tarea.setIdBeneficiario(idBeneficiario);
                    tarea.setColor(actividades.get(sprActividad.getSelectedItemPosition()).getColor());

                    Toast.makeText(getApplicationContext(), txtNombTarea.getText().toString(), Toast.LENGTH_LONG).show();

                    if(codigo==0) {
                        myRef = FirebaseDatabase.getInstance().getReference().child("");
                        String key = myRef.push().getKey();
                        myRef = database.getReference(PATH_TAREAS + key);
                        tarea.setIdTarea(myRef.getKey());
                        myRef.setValue(tarea);
                    }
                    if(codigo==1) {
                        myRef = database.getReference(PATH_TAREAS+miTarea.getIdTarea());
                        tarea.setIdTarea(miTarea.getIdTarea());
                        myRef.setValue(tarea);
                    }

                    UtilsFocus.planeacion(idBeneficiario);
                    Intent i = new Intent(getBaseContext(), HomeAppActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                //}
            }
        });
    }

    private void estilosAprendizaje(String idBeneficiario) {
        myRef.child(PATH_ESTILOS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    EstiloAprendizaje estilo = ds.getValue(EstiloAprendizaje.class);

                    if(idBeneficiario.equalsIgnoreCase(estilo.getIdBeneficiario())) {
                        estiloDominante = estilo.getDominate();
                        estiloSecundario = estilo.getSecundario();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarDatos() {
        //Tarea tarea =(Tarea) getIntent().getSerializableExtra("tarea");
        txtNombTarea.setText(miTarea.getNombre());
        txtDescripTarea.setText(miTarea.getDescripcion());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        miTarea.getFechaEntrega().setMonth(miTarea.getFechaEntrega().getMonth());
        miTarea.getFechaEntrega().setYear(miTarea.getFechaEntrega().getYear());
        String fechEntrega = df.format(miTarea.getFechaEntrega());
        txtFechaEntrega.setText(fechEntrega);

        DateFormat dfHora = new SimpleDateFormat("hh:mm");
        String horaEntrega = dfHora.format(miTarea.getFechaEntrega());
        txtHoraEntrega.setText(horaEntrega);

        if(miTarea.isEstaMotivado()){
            radioSi.setChecked(true);
        }else if(!miTarea.isEstaMotivado()){
            radioNo.setChecked(true);
        }

        sprComplejidad.setSelection(obtenerPosicionItem(sprComplejidad,miTarea.getComplejidad()));
        sprClasificacion.setSelection(obtenerPosicionItem(sprClasificacion,miTarea.getClasificacion()));

        for (int i=0; i<miTarea.getAreas().size(); i++){
            if(miTarea.getAreas().get(i).equals("Lectura")){
                checkLectura.setChecked(true);
            }
            if(miTarea.getAreas().get(i).equals("Escritura")){
                checkEscritura.setChecked(true);
            }
            if(miTarea.getAreas().get(i).equals("Razonamiento")){
                checkRazonamiento.setChecked(true);
            }
            if(miTarea.getAreas().get(i).equals("Ingles")){
                checkIngles.setChecked(true);
            }
            if(miTarea.getAreas().get(i).equals("Competencias")){
                checkCompentencias.setChecked(true);
            }
        }

        for (int i=0; i<beneficiarios.size(); i++){
            if(miTarea.getIdBeneficiario().equals(beneficiarios.get(i).getIdBeneficiario())){
                spnBeneficiariosAgrTar.setSelection(i);
            }
        }


    }

    public static int obtenerPosicionItem(Spinner spinner, String nombre) {
        int posicion = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(nombre)) {
                posicion = i;
            }
        }
        return posicion;
    }

    /*
    private boolean validarDatos() {
        boolean esValido = true;
        if(TextUtils.isEmpty(txtNombTarea.getText().toString())){
            esValido = false;
            txtNombTarea.setError("Requerido");
        }
        if(TextUtils.isEmpty(txtFechaEntrega.getText().toString())){
            esValido = false;
            txtFechaEntrega.setError("Requerido");
        }
        if(TextUtils.isEmpty(txtHoraEntrega.getText().toString())){
            esValido = false;
            txtHoraEntrega.setError("Requerido");
        }
        if(!radioSi.isChecked() && !radioNo.isChecked()){
            Log.i("ESTADO", "-------");
            esValido = false;
            txtMotivacion.setError("Requerido");

        }
        if(sprActividad.getSelectedItem().equals("")){
            esValido = false;
            TextView errorText = (TextView)sprActividad.getSelectedView();
            errorText.setError("");
        }
        if(sprComplejidad.getSelectedItem().equals("Seleccione la complejidad")){
            esValido = false;
            TextView errorText = (TextView)sprComplejidad.getSelectedView();
            errorText.setError("");
        }
        if(sprClasificacion.getSelectedItem().equals("Seleccione la clasificación")){
            esValido = false;
            TextView errorText = (TextView)sprClasificacion.getSelectedView();
            errorText.setError("");
        }

        if(!checkLectura.isChecked() && !checkEscritura.isChecked() && !checkRazonamiento.isChecked() && !checkIngles.isChecked() && !checkCompentencias.isChecked()){
            Log.i("ESTADO", "-------");
            esValido = false;
            txtAreas.setError("Requerido");

        }

        return esValido;
    }
*/

    public void spinnerActiv(String idBeneficiario){

        //Log.i("TAG", "Beneficiario funcion" + idBeneficiario);

        myRef.child(PATH_ACTIVIDADES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int posicion = 0;
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Actividad actActual = ds.getValue(Actividad.class);
                    Usuario usrActual = ds.getValue(Usuario.class);
                    //Log.i("TAG", "Beneficiario: " + usrActual.getNombres());

                    if(idBeneficiario.equalsIgnoreCase(actActual.getIdUsaurio())) {

                        //Log.i("TAG", "Iguales: " + idBeneficiario + actActual.getIdUsaurio());
                        String nombre = actActual.getNombre();
                        String desempeno = actActual.getDesempeño();
                        nombre_Actividades.add(nombre);
                        desempenoActividades.add(desempeno);
                        id_Actividades.add(actActual.getIdActividad());
                        idActiv.add(actActual.getIdActividad());
                        actividades.add(actActual);

                        if(codigo==1){
                            if(miTarea.getIdActividad().equals(actActual.getIdActividad())){
                                posicion=nombre_Actividades.size()-1;
                            }
                        }
                        //Log.i("TAG", "Actividad: " + nombre);
                    }
                }
                if(nombre_Actividades.isEmpty()){
                    nombre_Actividades.add("");
                }
                ArrayAdapter<String> adapterAct = new ArrayAdapter<>(AgregarTareaActivity.this, android.R.layout.simple_dropdown_item_1line, nombre_Actividades);
                sprActividad.setAdapter(adapterAct);
                sprActividad.setSelection(posicion);
                //adapterAct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void obtenerFecha(){

        DatePickerDialog.OnDateSetListener dateList = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);

                txtFechaEntrega.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                txtFechaEntrega.setError(null);
            }
        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();
    }




}
