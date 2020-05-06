package com.example.focusappm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.Calendar;
import java.util.List;

public class AgregarActividadActivity extends AppCompatActivity {

    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    EditText edttxtNomActividad;
    EditText edttxtDescripcion;
    EditText edttxtFechaIni;
    EditText edttxtFechaFin;
    Button btnAgregar;
    ImageButton btnFechaIni;
    ImageButton btnFechaFin;
    Spinner spnTipo;
    Spinner spnDesempenio, spnBeneficiariosAgrAc;
    Integer codigo;
    CheckBox horarioFijo;
    List<String> nombresBeneficiarios;
    List<Usuario> beneficiarios;
    ArrayAdapter<String> adapter;
    RadioGroup radioGroup;
    RadioButton radioSi;
    RadioButton radioNo;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_ACTIVIDADES = "actividades/";
    public static final String PATH_USUARIOS = "usuarios/";
    //public static final String PATH_ACTIVIDADES_TAREAS = "actividades";
    FirebaseAuth mAuth;
    FirebaseUser user;

    Actividad miActividad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        edttxtFechaIni = findViewById(R.id.edttxtFechaIni);
        edttxtFechaFin = findViewById(R.id.edttxtFechaFin);
        edttxtNomActividad = findViewById(R.id.edttxtNomActividad);
        edttxtDescripcion = findViewById(R.id.edttxtDescripcion);
        btnFechaIni = findViewById(R.id.btnFechaIni);
        btnFechaFin = findViewById(R.id.btnFechaFin);
        btnAgregar = findViewById(R.id.btnAceptarAgregarAct);
        spnTipo = findViewById(R.id.spnTipo);
        spnDesempenio = findViewById(R.id.spnDesempeno);
        spnBeneficiariosAgrAc = findViewById(R.id.spnBeneficiariosAgrAc);
        horarioFijo = findViewById(R.id.chbxHorarioFijo);
        nombresBeneficiarios = (List<String>) getIntent().getSerializableExtra("nombreBeneficiarios");
        beneficiarios = (List<Usuario>)getIntent().getSerializableExtra("beneficiarios");
        codigo =(Integer) getIntent().getSerializableExtra("codigo");
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nombresBeneficiarios);
        adapter.notifyDataSetChanged();
        spnBeneficiariosAgrAc.setAdapter(adapter);
        radioSi = findViewById(R.id.radioSi);
        radioNo = findViewById(R.id.radioNo);
        radioGroup = findViewById(R.id.radioGroup);

        ArrayAdapter<CharSequence> adapterDesempeono = ArrayAdapter.createFromResource(this, R.array.Desempeno, android.R.layout.simple_spinner_item);
        spnDesempenio.setAdapter(adapterDesempeono);

        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.TipoActividad, android.R.layout.simple_spinner_item);
        spnTipo.setAdapter(adapterTipo);

        if(codigo==1){
            miActividad =(Actividad) getIntent().getSerializableExtra("actividad");
            llenarDatos();
        }

        btnFechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha(1);
            }
        });
        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha(2);
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
                    Log.i("MyAPP", String.valueOf(horarioFijo.isChecked()));

                    try {
                        actividad.setFechaInicio(new SimpleDateFormat("dd/MM/yyyy").parse(edttxtFechaIni.getText().toString()));
                        actividad.getFechaInicio().setYear(actividad.getFechaInicio().getYear() + 1900);
                        actividad.getFechaInicio().setMonth(actividad.getFechaInicio().getMonth() + 1);
                        actividad.setFechaFinal(new SimpleDateFormat("dd/MM/yyyy").parse(edttxtFechaFin.getText().toString()));
                        actividad.getFechaFinal().setYear(actividad.getFechaFinal().getYear() + 1900);
                        actividad.getFechaFinal().setMonth(actividad.getFechaFinal().getMonth() + 1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                   // actividad.setFechaInicio(edttxtFechaIni.getText().toString());
                    // actividad.setFechaFinal(edttxtFechaFin.getText().toString());
                    actividad.setHorarioFijo(horarioFijo.isChecked());

                    String idBeneficiario = beneficiarios.get(spnBeneficiariosAgrAc.getSelectedItemPosition()).getIdBeneficiario();

                    actividad.setIdUsaurio(idBeneficiario);
                    Toast.makeText(getApplicationContext(), edttxtNomActividad.getText().toString(), Toast.LENGTH_LONG).show();

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
                        miActividad.setIdActividad(miActividad.getIdActividad());
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

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        miActividad.getFechaInicio().setMonth(miActividad.getFechaInicio().getMonth()-1);
        miActividad.getFechaInicio().setYear(miActividad.getFechaInicio().getYear()-1900);
        String fechInicio = df.format(miActividad.getFechaInicio());
        edttxtFechaIni.setText(fechInicio);

        miActividad.getFechaFinal().setMonth(miActividad.getFechaFinal().getMonth()-1);
        miActividad.getFechaFinal().setYear(miActividad.getFechaFinal().getYear()-1900);
        String fechFin = df.format(miActividad.getFechaFinal());
        edttxtFechaFin.setText(fechFin);

        if(miActividad.getHorarioFijo()){
            horarioFijo.setChecked(true);
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

    private boolean validarDatos() {
        boolean esValido = true;
        if(TextUtils.isEmpty(edttxtNomActividad.getText().toString())){
            esValido = false;
            edttxtNomActividad.setError("Requerido");
        }
        /*if(TextUtils.isEmpty(edttxtDescripcion.getText().toString())){
            esValido = false;
            edttxtDescripcion.setError("Requerido");
        }*/
        if(TextUtils.isEmpty(edttxtFechaIni.getText().toString())){
            esValido = false;
            edttxtFechaIni.setError("");
        }
        if(TextUtils.isEmpty(edttxtFechaFin.getText().toString())){
            esValido = false;
            edttxtFechaFin.setError("");
        }

        if(spnTipo.getSelectedItem().equals("Seleccione el tipo")){
            esValido = false;
            TextView errorText = (TextView)spnTipo.getSelectedView();
            errorText.setError("");
        }
        if(spnDesempenio.getSelectedItem().equals("Seleccione el desempeño")){
            esValido = false;
            TextView errorText = (TextView)spnDesempenio.getSelectedView();
            errorText.setError("");
        }
        return esValido;
    }

    public void obtenerFecha(final int codigo) {
        DatePickerDialog.OnDateSetListener dateList = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                if (codigo == 1){
                    edttxtFechaIni.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                    edttxtFechaIni.setError(null);
                    System.out.println("edit1");
                }
                else{
                    edttxtFechaFin.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                    edttxtFechaFin.setError(null);
                    System.out.println("edit2");
                }

            }

        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();
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
}
