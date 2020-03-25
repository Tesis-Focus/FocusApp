package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.focusappm.RegistroActivity.PATH_USUARIOS;
import static com.example.focusappm.RegistroActivity.focus;

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
    Spinner spnDesempenio;
    CheckBox horarioFijo;

    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_ACTIVIDADES = "actividades/";
    //public static final String PATH_USUARIOS = "usuarios/";
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
         horarioFijo = findViewById(R.id.chbxHorarioFijo);

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
                actividad.setIdUsaurio(user.getUid());
                //actividad.getIdTareas().add("dxsxsxs");

                Toast.makeText(getApplicationContext(),edttxtNomActividad.getText().toString(), Toast.LENGTH_LONG).show();
                myRef = FirebaseDatabase.getInstance().getReference().child("");
                String key = myRef.push().getKey();
                //Log.i("MyAPP", myRef.getKey());
                myRef = database.getReference(PATH_ACTIVIDADES+key);
                myRef.setValue(actividad);





                /*
                List<Usuario> usuarios = focus.getUsuarios();
                System.out.println("Tamaño: " + usuarios.size());

                for (Usuario usuario: usuarios) {
                    System.out.println("Usuarios " + usuario);
                    if (usuario.getId() == user.getUid()){
                        usuario.getIdActividades().add(key);
                        myRef = FirebaseDatabase.getInstance().getReference().child("");
                        String keyActivity = myRef.push().getKey();
                        myRef = database.getReference(PATH_USUARIOS +  keyActivity);
                        myRef.setValue(usuario);
                    }
                }

                //user.getUid() +

               /* Usuario usuario = new Usuario();
                usuario.getIdActividades().add(key);
                usuario.getIdActividades().add("dccddccdcd");
                usuario.setNombres("anyi");
                myRef = FirebaseDatabase.getInstance().getReference().child("");
                String keyActivity = myRef.push().getKey();
                myRef = database.getReference(PATH_USUARIOS + user.getUid());
                myRef.setValue(usuario);*/

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
}
