package com.example.focusappm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import petrov.kristiyan.colorpicker.ColorPicker;

public class AgregarActividadActivity extends AppCompatActivity {

    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    EditText edttxtNomActividad;
    EditText edttxtDescripcion;
    Button btnAgregar,btnColor,btnAgregarHorarioActividad;
    Spinner spnTipo, spnAsignatura;
    Spinner spnDesempenio, spnBeneficiariosAgrAc;
    Integer codigo;
    List<String> nombresBeneficiarios;
    List<Usuario> beneficiarios;
    ArrayAdapter<String> adapter;
    ArrayList<Horario> horarioActividad;
    RadioGroup radioGroup;
    RadioButton radioSi;
    RadioButton radioNo;
    LinearLayout linear;
    Date fechaini,fechafin;
    TextView textViewAsig;
    int colorActivity;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_ACTIVIDADES = "actividades/";
    public static final String PATH_USUARIOS = "usuarios/";
    public static final int REQUEST_CODE_ACTIVIDAD = 2;
    //public static final String PATH_ACTIVIDADES_TAREAS = "actividades";
    FirebaseAuth mAuth;
    FirebaseUser user;

    Actividad miActividad;

    Boolean hayHorarios=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        edttxtNomActividad = findViewById(R.id.edttxtNomActividad);
        edttxtDescripcion = findViewById(R.id.edttxtDescripcion);
        btnAgregarHorarioActividad = findViewById(R.id.btnAgregarHorarioActividad);
        btnColor =findViewById(R.id.btnColor);
        btnAgregar = findViewById(R.id.btnAceptarAgregarAct);
        spnTipo = findViewById(R.id.spnTipo);
        spnDesempenio = findViewById(R.id.spnDesempeno);
        spnBeneficiariosAgrAc = findViewById(R.id.spnBeneficiariosAgrAc);
        spnAsignatura = findViewById(R.id.spnAsignaturas);
        nombresBeneficiarios = (List<String>) getIntent().getSerializableExtra("nombreBeneficiarios");
        beneficiarios = (List<Usuario>)getIntent().getSerializableExtra("beneficiarios");
        codigo =(Integer) getIntent().getSerializableExtra("codigo");
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nombresBeneficiarios);
        adapter.notifyDataSetChanged();
        spnBeneficiariosAgrAc.setAdapter(adapter);
        radioSi = findViewById(R.id.radioSi);
        radioNo = findViewById(R.id.radioNo);
        radioGroup = findViewById(R.id.radioGroup);
        linear = findViewById(R.id.linear);
        textViewAsig = findViewById(R.id.textViewAsig);
        horarioActividad = new ArrayList<>();
        fechaini =null;
        fechafin =null;

        ArrayAdapter<CharSequence> adapterDesempeono = ArrayAdapter.createFromResource(this, R.array.Desempeno, android.R.layout.simple_spinner_item);
        spnDesempenio.setAdapter(adapterDesempeono);

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoActividad, android.R.layout.simple_spinner_item);
        spnTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterAsigna = ArrayAdapter.createFromResource(this, R.array.Asignaturas, android.R.layout.simple_spinner_item);
        spnAsignatura.setAdapter(adapterAsigna);

        spnAsignatura.setVisibility(View.GONE);
        textViewAsig.setVisibility(View.GONE);

        if(codigo==1){
            miActividad =(Actividad) getIntent().getSerializableExtra("actividad");
            llenarDatos();
            hayHorarios=true;
            horarioActividad = miActividad.getHorarios();

            fechaini = (Date) miActividad.getFechaInicio().clone();
            fechafin = (Date) miActividad.getFechaFinal().clone();
            fechaini.setYear(fechaini.getYear()+3800);
            fechafin.setYear(fechafin.getYear()+3800);
            Log.i("FechasAgregar" , "INI Suma " + fechaini.getYear() );
            Log.i("FechasAgregar" , "FIN Suma " + fechafin.getYear() );
            Log.i("FechasAgregar" , "INI " + miActividad.getFechaInicio().getYear());
            Log.i("FechasAgregar" , "FIN " + miActividad.getFechaFinal().getYear());

        }

        spnTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                //Spinner spnAsignaturas = new Spinner(getApplicationContext());
                if(spnTipo.getSelectedItem().equals("Académica")){
                    textViewAsig.setVisibility(View.VISIBLE);
                    spnAsignatura.setVisibility(View.VISIBLE);
                }
                if(spnTipo.getSelectedItem().equals("Extracurricular")){
                    textViewAsig.setVisibility(View.GONE);
                    spnAsignatura.setVisibility(View.GONE);
                }
                if(spnTipo.getSelectedItem().equals("Médica")){
                    textViewAsig.setVisibility(View.GONE);
                    spnAsignatura.setVisibility(View.GONE);
                }
                if(spnTipo.getSelectedItem().equals("Familiar")){
                    textViewAsig.setVisibility(View.GONE);
                    spnAsignatura.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView arg0) {

            }
        });

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker colorPicker = new ColorPicker(AgregarActividadActivity.this);
                colorPicker.setTitle("Selecciona un color");
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        btnColor.setBackgroundColor(color);
                        colorActivity = color;
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }

        });



        btnAgregarHorarioActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),EventosCalendarioActivity.class);
                i.putExtra("codigo", 0);
                if(hayHorarios) {
                    i.putExtra("horarios", horarioActividad);
                    i.putExtra("fechaini", fechaini);
                    i.putExtra("fechafin", fechafin);
                    i.putExtra("codigo", 1);

                }
                startActivityForResult(i,REQUEST_CODE_ACTIVIDAD);
            }
        });



        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()) {
                    Actividad actividad = new Actividad();
                    System.out.println(edttxtNomActividad.getText().toString());
                    actividad.setNombre(edttxtNomActividad.getText().toString());
                    actividad.setDescripcion(edttxtDescripcion.getText().toString());
                    actividad.setTipo(spnTipo.getSelectedItem().toString());
                    actividad.setDesempeño(spnDesempenio.getSelectedItem().toString());
                    actividad.setColor(colorActivity);
                    actividad.setHorarios(horarioActividad);
                    fechaini.setHours(horarioActividad.get(0).getmStartTime().getHours());
                    fechaini.setMinutes(horarioActividad.get(0).getmStartTime().getMinutes());
                    fechafin.setHours(horarioActividad.get(0).getmEndTime().getHours());
                    fechafin.setMinutes(horarioActividad.get(0).getmEndTime().getMinutes());
                    actividad.setFechaInicio(fechaini);
                    actividad.setFechaFinal(fechafin);



                    String idBeneficiario = beneficiarios.get(spnBeneficiariosAgrAc.getSelectedItemPosition()).getIdBeneficiario();

                    actividad.setIdUsaurio(idBeneficiario);
                    Toast.makeText(getApplicationContext(), edttxtNomActividad.getText().toString(), Toast.LENGTH_LONG).show();


                    if(actividad.getTipo().equals("Académica")) {
                        Log.i("Asignatura", "entra en agregar");

                        actividad.setAsignatura(spnAsignatura.getSelectedItem().toString());
                       Log.i("Asignatura", "asig " + actividad.getAsignatura());


                        List<String> areas = new ArrayList<>();
                        if (actividad.getAsignatura().equals("Ciencias naturales")) {
                            areas.add("Lectura");
                            areas.add("Escritura");
                            areas.add("Competencias");
                        }
                        if (actividad.getAsignatura().equals("Ciencias sociales")) {
                            areas.add("Lectura");
                            areas.add("Escritura");
                            areas.add("Competencias");

                        }
                        if (actividad.getAsignatura().equals("Educación artistica")) {
                            areas.add("Escritura");
                            areas.add("Competencias");

                        }
                        if (actividad.getAsignatura().equals("Ética y valores")) {
                            areas.add("Lectura");
                            areas.add("Escritura");
                            areas.add("Competencias");
                        }
                        if (actividad.getAsignatura().equals("Educación física")) {
                            areas.add("Razonamiento");
                            areas.add("Competencias");
                        }
                        if (actividad.getAsignatura().equals("Religión")) {
                            areas.add("Lectura");
                            areas.add("Escritura");
                            areas.add("Competencias");
                        }
                        if (actividad.getAsignatura().equals("Humanidades, español e idiomas")) {
                            areas.add("Lectura");
                            areas.add("Escritura");
                            areas.add("Ingles");
                        }
                        if (actividad.getAsignatura().equals("Matemáticas")) {
                            areas.add("Lectura");
                            areas.add("Escritura");
                            areas.add("Razonamiento");
                        }
                        if (actividad.getAsignatura().equals("Tecnología e informática")) {
                            areas.add("Razonamiento");
                            areas.add("Ingles");
                            areas.add("Competencias");
                        }

                        actividad.getAreas().addAll(areas);
                    }

                    if(codigo==0) {
                        myRef = FirebaseDatabase.getInstance().getReference().child("");
                        String key = myRef.push().getKey();
                        //Log.i("MyAPP", myRef.getKey());
                        myRef = database.getReference(PATH_ACTIVIDADES + key);
                        actividad.setIdActividad(myRef.getKey());
                        myRef.setValue(actividad);
                    }

                    if(codigo==1){
                        myRef = database.getReference(PATH_ACTIVIDADES+miActividad.getIdActividad());
                        actividad.setIdActividad(miActividad.getIdActividad());
                        myRef.setValue(actividad);
                    }


                    Toast.makeText(getApplicationContext(), "Persistencia hecha", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getBaseContext(), HomeAppActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });
    }

    private void llenarDatos() {
        for (int i=0; i<beneficiarios.size(); i++){
            if(miActividad.getIdUsaurio().equals(beneficiarios.get(i).getIdBeneficiario())){
                spnBeneficiariosAgrAc.setSelection(i);
            }
        }
        edttxtNomActividad.setText(miActividad.getNombre());
        edttxtDescripcion.setText(miActividad.getDescripcion());
        spnTipo.setSelection(obtenerPosicionItem(spnTipo,miActividad.getTipo()));
        spnDesempenio.setSelection(obtenerPosicionItem(spnDesempenio,miActividad.getDesempeño()));
        if(spnTipo.getSelectedItem().equals("Académica")) {
            spnAsignatura.setSelection(obtenerPosicionItem(spnAsignatura, miActividad.getAsignatura()));
        }

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        miActividad.getFechaInicio().setMonth(miActividad.getFechaInicio().getMonth());
        miActividad.getFechaInicio().setYear(miActividad.getFechaInicio().getYear()-1900);
        String fechInicio = df.format(miActividad.getFechaInicio());
        btnColor.setBackgroundColor(miActividad.getColor());

        miActividad.getFechaFinal().setMonth(miActividad.getFechaFinal().getMonth());
        miActividad.getFechaFinal().setYear(miActividad.getFechaFinal().getYear()-1900);
        String fechFin = df.format(miActividad.getFechaFinal());
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

    private boolean validarDatos() {
        boolean esValido = true;
        if(TextUtils.isEmpty(edttxtNomActividad.getText().toString())){
            esValido = false;
            edttxtNomActividad.setError("Requerido");
        }
        if(fechaini==null || fechafin == null){
            esValido = false;
            Toast.makeText(getApplicationContext(),"agrege un horario",Toast.LENGTH_LONG);
        }

        if(spnTipo.getSelectedItem().equals("Seleccione el tipo")){
            esValido = false;
            TextView errorText = (TextView)spnTipo.getSelectedView();
            errorText.setError("");
        }
        if(spnTipo.getSelectedItem().equals("Académica") ) {
            if (spnAsignatura.getSelectedItem().equals("Seleccione la asignatura")) {
                esValido = false;
                TextView errorText = (TextView) spnAsignatura.getSelectedView();
                errorText.setError("");
            }
        }
        if(spnDesempenio.getSelectedItem().equals("Seleccione el desempeño")){
            esValido = false;
            TextView errorText = (TextView)spnDesempenio.getSelectedView();
            errorText.setError("");
        }
        return esValido;
    }


    private void cargarPerfilesB(){

        myRef = database.getReference(PATH_USUARIOS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    Usuario beneficiario = sn.getValue(Usuario.class);
                    if(beneficiario.getRol().equals("Beneficiario") && beneficiario.getIdUsuario().equals(user.getUid())){
                        beneficiarios.add(beneficiario);
                        nombresBeneficiarios.add(beneficiario.getNombres()+" "+beneficiario.getApellidos());
                        Log.i("beneficiarios", "onDataChange: "+(beneficiario.getNombres()+" "+beneficiario.getApellidos()));
                    }
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nombresBeneficiarios);
                adapter.notifyDataSetChanged();
                spnBeneficiariosAgrAc.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ACTIVIDAD){
            if(resultCode == RESULT_OK){
                horarioActividad = (ArrayList<Horario>) data.getSerializableExtra("horarios");
                fechaini = (Date) data.getSerializableExtra("fechaini");
                fechafin = (Date) data.getSerializableExtra("fechafin");
                Log.i("fechas Agregar", "onActivityResult: StartTime "+fechaini+" endtime "+fechafin);
                hayHorarios= true;
            }
        }
    }
}
