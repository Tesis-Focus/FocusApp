package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
    EditText txtTemaTarea;
    Spinner sprComplejidad;
    Spinner sprClasificacion;
    Spinner sprArea;
    Spinner sprActividad;
    Button btnGuardarTarea;
    Calendar calendario;
    DatePickerDialog datePickerDialog;
    ArrayList<String> nombre_Actividades;
    ArrayList<String> desempenoActividades;
    ArrayList<String> id_Actividades;
    List<String> nombresBeneficiarios;
    List<Usuario> beneficiarios;
    ArrayAdapter<String> adapterBenef;

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

    String idBeneficiario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        spnBeneficiariosAgrTar = findViewById(R.id.spnBeneficiariosAgrTar);
        txtFechaEntrega = findViewById(R.id.txtFechaEntrega);
        btnFechaEntrega = findViewById(R.id.btnFechaEntrega);
        txtHoraEntrega = findViewById(R.id.txtHoraEntrega);
        btnHoraEntrega = findViewById(R.id.btnHoraEntrega);
        txtNombTarea = findViewById(R.id.txtNombTarea);
        txtDescripTarea = findViewById(R.id.txtDescripTarea);
        txtTemaTarea = findViewById(R.id.txtTemaTarea);
        sprComplejidad = findViewById(R.id.sprComplejidad);
        sprClasificacion = findViewById(R.id.sprClasificacion);
        sprArea = findViewById(R.id.sprArea);
        sprActividad = findViewById(R.id.sprActividad);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);
        nombre_Actividades = new ArrayList<String>();
        desempenoActividades = new ArrayList<String>();
        id_Actividades = new ArrayList<String>();
        nombresBeneficiarios = (List<String>) getIntent().getSerializableExtra("nombreBeneficiarios");
        beneficiarios = (List<Usuario>)getIntent().getSerializableExtra("beneficiarios");
        adapterBenef = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nombresBeneficiarios);
        adapterBenef.notifyDataSetChanged();
        spnBeneficiariosAgrTar.setAdapter(adapterBenef);

        spnBeneficiariosAgrTar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                nombre_Actividades.clear();
                desempenoActividades.clear();
                id_Actividades.clear();
                idBeneficiario = beneficiarios.get(spnBeneficiariosAgrTar.getSelectedItemPosition()).getIdBeneficiario();
                Log.i("TAG", "idBeneficiario: " + idBeneficiario);

                spinnerActiv(idBeneficiario);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnFechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerFecha();

                /*
                calendario = Calendar.getInstance();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH);
                int anio = calendario.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AgregarTareaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        txtFechaEntrega.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, anio, mes, dia);
                datePickerDialog.show();

                 */
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

                        txtHoraEntrega.setText(hourOfDay +":"+ minute);
                    }
                }, hora, minutos,false);
                timePickerDialog.show();

            }
        });

        ArrayAdapter<CharSequence> adapterComplej = ArrayAdapter.createFromResource(this, R.array.Complejidad, android.R.layout.simple_spinner_item);
        sprComplejidad.setAdapter(adapterComplej);

        ArrayAdapter<CharSequence> adapterClasif = ArrayAdapter.createFromResource(this, R.array.Clasificacion, android.R.layout.simple_spinner_item);
        sprClasificacion.setAdapter(adapterClasif);

        ArrayAdapter<CharSequence> adapterArea = ArrayAdapter.createFromResource(this, R.array.Area, android.R.layout.simple_spinner_item);
        sprArea.setAdapter(adapterArea);

        btnGuardarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReglasDecision reglas = new ReglasDecision();
                Tarea tarea = new Tarea();
                Tarea nueva = new Tarea();
                Date fechaAsig = new Date();
                tarea.setNombre(txtNombTarea.getText().toString());
                tarea.setDescripcion(txtDescripTarea.getText().toString());
                tarea.setTema(txtTemaTarea.getText().toString());
                tarea.setComplejidad(sprComplejidad.getSelectedItem().toString());
                tarea.setClasificacion(sprClasificacion.getSelectedItem().toString());
                String hora= txtHoraEntrega.getText().toString();
                String mHora = hora.split(":")[0];
                String mMinuto = hora.split(":")[1];

                try {
                    tarea.setFechaEntrega(new SimpleDateFormat("dd/MM/yyyy").parse(txtFechaEntrega.getText().toString()));
                    tarea.getFechaEntrega().setHours(Integer.parseInt(mHora));
                    tarea.getFechaEntrega().setMinutes(Integer.parseInt(mMinuto));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tarea.setArea(sprArea.getSelectedItem().toString());
                String id_Actividad = id_Actividades.get(sprActividad.getSelectedItemPosition());
                String desempenoActividad = desempenoActividades.get(sprActividad.getSelectedItemPosition());

                Log.i("test",desempenoActividad );

                tarea.setIdActividad(id_Actividad);
                tarea = reglas.asignarTiempos(tarea,desempenoActividad);
                tarea = reglas.asignarPrioridad(tarea,desempenoActividad);
                Log.i("TAG", "onClick: "+tarea.getPrioridad());

                Log.i("TAG", "onClick: agregar tarea a actividad "+id_Actividad+" "+nombre_Actividades.get(sprActividad.getSelectedItemPosition()));

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tarea.setFechaAsignacion(fechaAsig);  //El dia que ingresa la tarea
                tarea.setIdBeneficiario(idBeneficiario);

                Toast.makeText(getApplicationContext(), txtNombTarea.getText().toString(), Toast.LENGTH_LONG).show();
                myRef = FirebaseDatabase.getInstance().getReference().child("");
                String key = myRef.push().getKey();
                myRef = database.getReference(PATH_TAREAS + key);
                tarea.setIdTarea(myRef.getKey());
                myRef.setValue(tarea);

                lanzarPlaneacion(tarea, idBeneficiario);

                Intent i = new Intent(getBaseContext(),HomeAppActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    private void lanzarPlaneacion(Tarea tarea, String idBeneficiario) {
        UtilsFocus.calcularHorarioPorTarea(tarea,idBeneficiario);
    }


    public void spinnerActiv(String idBeneficiario){

        //Log.i("TAG", "Beneficiario funcion" + idBeneficiario);

        myRef.child(PATH_ACTIVIDADES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
                        //Log.i("TAG", "Actividad: " + nombre);
                    }
                }

                ArrayAdapter<String> adapterAct = new ArrayAdapter<>(AgregarTareaActivity.this, android.R.layout.simple_dropdown_item_1line, nombre_Actividades);
                sprActividad.setAdapter(adapterAct);
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
            }
        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();
    }

}
