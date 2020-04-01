package com.example.focusappm;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
    Spinner spnMotivacion;
    Spinner spnDesempenio, spnBeneficiariosAgrAc;
    CheckBox horarioFijo;
    List<String> nombresBeneficiarios;
    List<Usuario> beneficiarios;
    ArrayAdapter<String> adapter;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_ACTIVIDADES = "actividades/";
    public static final String PATH_USUARIOS = "usuarios/";
    //public static final String PATH_ACTIVIDADES_TAREAS = "actividades";
    FirebaseAuth mAuth;
    FirebaseUser user;


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
        spnMotivacion = findViewById(R.id.spnMotivacion);
        spnDesempenio = findViewById(R.id.spnDesempeno);
        spnBeneficiariosAgrAc = findViewById(R.id.spnBeneficiariosAgrAc);
        horarioFijo = findViewById(R.id.chbxHorarioFijo);
        nombresBeneficiarios = (List<String>) getIntent().getSerializableExtra("nombreBeneficiarios");
        beneficiarios = (List<Usuario>)getIntent().getSerializableExtra("beneficiarios");
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nombresBeneficiarios);
        adapter.notifyDataSetChanged();
        spnBeneficiariosAgrAc.setAdapter(adapter);

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
                Actividad actividad = new Actividad();
                System.out.println(edttxtNomActividad.getText().toString());
                actividad.setNombre(edttxtNomActividad.getText().toString());
                actividad.setDescripcion(edttxtDescripcion.getText().toString());
                actividad.setTipo(spnTipo.getSelectedItem().toString());
                actividad.setMotivacion(spnMotivacion.getSelectedItem().toString());
                actividad.setDesempeño(spnDesempenio.getSelectedItem().toString());
                Log.i("MyAPP", String.valueOf(horarioFijo.isChecked()));
                actividad.setFechaInicio(edttxtFechaIni.getText().toString());
                actividad.setFechaFinal(edttxtFechaFin.getText().toString());
                actividad.setHorarioFijo(horarioFijo.isChecked());

                String idBeneficiario = beneficiarios.get(spnBeneficiariosAgrAc.getSelectedItemPosition()).getIdBeneficiario();

                actividad.setIdUsaurio(idBeneficiario);
                Toast.makeText(getApplicationContext(),edttxtNomActividad.getText().toString(), Toast.LENGTH_LONG).show();
                myRef = FirebaseDatabase.getInstance().getReference().child("");
                String key = myRef.push().getKey();
                //Log.i("MyAPP", myRef.getKey());
                myRef = database.getReference(PATH_ACTIVIDADES+key);
                actividad.setIdActividad(myRef.getKey());
                myRef.setValue(actividad);

                Toast.makeText(getApplicationContext(),"Persistencia hecha", Toast.LENGTH_LONG).show();
            }
        });
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
                    System.out.println("edit1");
                }
                else{
                    edttxtFechaFin.setText(diaFormateado + "/" + mesFormateado + "/" + year);
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
