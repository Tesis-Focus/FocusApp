package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AgregarPerfilActivity extends AppCompatActivity {

    EditText edtxNombresPB, edtxApellidosPB, edtxFechaNacPB;
    Spinner spnCursoPB;
    Button btnListoPB;
    ImageButton btnFechaNacPB;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Usuario miUsuario;
    Integer codigo;

    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    private final static String PATH_USUARIOS = "usuarios/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_perfil);

        edtxNombresPB = findViewById(R.id.edtxNombrePB);
        edtxApellidosPB = findViewById(R.id.edtxApellidosPB);
        edtxFechaNacPB = findViewById(R.id.edtxFechaNacPB);
        spnCursoPB = findViewById(R.id.spnCursoPB);
        btnListoPB = findViewById(R.id.btnListoPB);
        btnFechaNacPB = findViewById(R.id.btnFechaNacPB);
        codigo = (Integer) getIntent().getSerializableExtra("codigo");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        if (codigo == 1) {
            miUsuario = (Usuario) getIntent().getSerializableExtra("usuario");
            llenarDatosUsuario();
        }

        btnFechaNacPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                edtxFechaNacPB.setText(date);
            }
        };

        btnListoPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validarCampos()) {

                    registroBeneficiario();
                    Intent i = new Intent(getBaseContext(), PerfilesActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });
    }


    private void obtenerFecha() {
        DatePickerDialog.OnDateSetListener dateList = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);

                edtxFechaNacPB.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                edtxFechaNacPB.setError(null);
            }
        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();
    }

    private void registroBeneficiario() {

        Usuario nuevoBene = new Usuario();
        nuevoBene.setCurso(spnCursoPB.getSelectedItem().toString());
        nuevoBene.setNombres(edtxNombresPB.getText().toString());
        nuevoBene.setApellidos(edtxApellidosPB.getText().toString());
        try {
            nuevoBene.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(edtxFechaNacPB.getText().toString()));
            nuevoBene.getFechaNacimiento().setYear(nuevoBene.getFechaNacimiento().getYear()+1900);
            nuevoBene.getFechaNacimiento().setMonth(nuevoBene.getFechaNacimiento().getMonth());
        } catch (Exception e) {
        }
        nuevoBene.setRol("Beneficiario");
        nuevoBene.setEmail(user.getEmail());
        myRef = database.getReference();
        String key = myRef.push().getKey();
        nuevoBene.setIdBeneficiario(key);
        nuevoBene.setIdUsuario(user.getUid());

        if (codigo == 1) {
            myRef = database.getReference(PATH_USUARIOS + miUsuario.getIdBeneficiario());
            nuevoBene.setIdBeneficiario(miUsuario.getIdBeneficiario());
            myRef.setValue(nuevoBene);

            Toast.makeText(getApplicationContext(), "Perfil editado exitosamente", Toast.LENGTH_LONG).show();
        }

        if(codigo == 0){

            myRef = database.getReference(PATH_USUARIOS + key);
            myRef.setValue(nuevoBene);

            Toast.makeText(getApplicationContext(), "Perfil agregado correctamente", Toast.LENGTH_LONG).show();
        }
    }

        private boolean validarCampos () {
            String nombres, apellidos, curso;

            Boolean valido = true;
            nombres = edtxNombresPB.getText().toString();
            apellidos = edtxApellidosPB.getText().toString();
            curso = spnCursoPB.getSelectedItem().toString();

            Log.i("Perfil", nombres);
            Log.i("Perfil", edtxNombresPB.getText().toString());

            if (TextUtils.isEmpty(nombres)) {
                valido = false;
                edtxNombresPB.setError("Requerido");
            }
            if (TextUtils.isEmpty(apellidos)) {
                valido = false;
                edtxApellidosPB.setError("Requerido");
            }
            if (TextUtils.isEmpty(edtxFechaNacPB.getText().toString())) {
                valido = false;
                edtxFechaNacPB.setError("Requerido");
            }

            if(spnCursoPB.getSelectedItem().equals("Seleccione el curso")){
                valido = false;
                TextView errorText = (TextView) spnCursoPB.getSelectedView();
                errorText.setError("");
            }

            return valido;
        }

        private void llenarDatosUsuario () {

            edtxNombresPB.setText(miUsuario.getNombres());
            edtxApellidosPB.setText(miUsuario.getApellidos());

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            miUsuario.getFechaNacimiento().setMonth(miUsuario.getFechaNacimiento().getMonth());
            miUsuario.getFechaNacimiento().setYear(miUsuario.getFechaNacimiento().getYear());
            String fechaNacimiento = df.format(miUsuario.getFechaNacimiento());
            edtxFechaNacPB.setText(fechaNacimiento);

            spnCursoPB.setSelection(obtenerPosicionItem(spnCursoPB, miUsuario.getCurso()));
        }

        public static int obtenerPosicionItem(Spinner spinner, String nombre){
            int posicion = 0;
            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(nombre)) {
                    posicion = i;
                }
            }
            return posicion;
        }
}
