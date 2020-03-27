package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgregarPerfilActivity extends AppCompatActivity {

    EditText edtxNombresPB,edtxApellidosPB,edtxFechaNacPB;
    Spinner spnCursoPB;
    Button btnListoPB;
    ImageButton btnFechaNacPB;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatePickerDialog.OnDateSetListener mDateSetListener;

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
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


        btnFechaNacPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int dia = calendario.get(Calendar.DAY_OF_MONTH);
                int mes = calendario.get(Calendar.MONTH);
                int anio = calendario.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(AgregarPerfilActivity.this,android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,anio,mes,dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth+"/"+month+"/"+year;
                edtxFechaNacPB.setText(date);
            }
        };

        btnListoPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    registroBeneficiario();
                    Intent i = new Intent(getBaseContext(),PerfilesActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });
    }

    private void registroBeneficiario(){
        Usuario nuevoBene = new Usuario();
        nuevoBene.setCurso(spnCursoPB.getSelectedItem().toString());
        nuevoBene.setNombres(edtxNombresPB.getText().toString());
        nuevoBene.setApellidos(edtxApellidosPB.getText().toString());
        try{
            nuevoBene.setFechaNacimiento( new SimpleDateFormat("dd/MM/yyyy").parse(edtxFechaNacPB.getText().toString()));
        }catch (Exception e){}
        nuevoBene.setRol("Beneficiario");
        nuevoBene.setEmail(user.getEmail());
        myRef = database.getReference();
        String key = myRef.push().getKey();
        nuevoBene.setId(key);
        nuevoBene.setIdUsuario(user.getUid());
        myRef = database.getReference(PATH_USUARIOS+key);
        myRef.setValue(nuevoBene);
        Toast.makeText(getApplicationContext(),"perfil agregado correctamente",Toast.LENGTH_LONG).show();
    }

    private boolean validarCampos() {
        String nombres, apellidos, curso;
        Date fecha_nacimiento;
        Boolean valido = true;
        nombres = edtxNombresPB.getText().toString();
        apellidos = edtxApellidosPB.getText().toString();
        curso = spnCursoPB.getSelectedItem().toString();
        try{
            fecha_nacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(edtxFechaNacPB.getText().toString());
        }catch (Exception e){
            edtxFechaNacPB.setError("Requerido");
            return false;
        }

        if(TextUtils.isEmpty(nombres) || TextUtils.isEmpty(apellidos) || curso.equals("-")){
            valido = false;
            edtxNombresPB.setError("requerido");
            edtxApellidosPB.setError("requerido");
        }
        return valido;

    }
}
