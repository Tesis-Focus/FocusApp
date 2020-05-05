package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RegistroActivity extends AppCompatActivity {

    public static final String PATH_USUARIOS = "usuarios/";
    public static final Focus focus= new Focus();

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference myRef;
    TextView edttxtFechaNacimiento,edttxtNombre,edttxtApellido,edttxtEmail,edttxtContrasena,edttxtConfirmaContra;
    Button btnRegistro;
    ImageButton btnRegistroFechaNac;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    public final Calendar c = Calendar.getInstance();
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnRegistro = findViewById(R.id.btnRegistro);
        edttxtNombre =findViewById(R.id.edttxtNombre);
        edttxtApellido = findViewById(R.id.edttxtApellido);
        edttxtEmail = findViewById(R.id.edttxtEmail);
        edttxtContrasena = findViewById(R.id.edttxtContrasena);
        edttxtFechaNacimiento = findViewById(R.id.edttxtFechaNacimiento);
        edttxtConfirmaContra = findViewById(R.id.edttxtConfirmaContra);
        btnRegistroFechaNac = findViewById(R.id.btnRegistroFechaNac);

        btnRegistroFechaNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth+"/"+month+"/"+year;
                edttxtFechaNacimiento.setText(date);
            }
        };

        edttxtNombre.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edttxtNombre.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        edttxtApellido.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edttxtApellido.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        edttxtEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edttxtEmail.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarCampos()){
                    registro();
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

                edttxtFechaNacimiento.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                edttxtFechaNacimiento.setError(null);
            }
        };

        DatePickerDialog recogerFecha = new DatePickerDialog(this, dateList, anio, mes, dia);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recogerFecha.setOnDateSetListener(dateList);
        }

        recogerFecha.show();
    }

    private void updateUI(FirebaseUser currentUser){
        if( currentUser != null){
            Intent i = new Intent(getBaseContext(),HomeAppActivity.class);
            i.putExtra("user",currentUser.getEmail());
            startActivity(i);
        }
    }
    public boolean validarCampos(){

        String nombre,apellido,fechaNacimiento,correo,contrasena;
        nombre = edttxtNombre.getText().toString();
        apellido = edttxtApellido.getText().toString();
        fechaNacimiento = edttxtFechaNacimiento.getText().toString();
        correo = edttxtEmail.getText().toString();
        contrasena = edttxtContrasena.getText().toString();

        boolean esValido = true;

        if(TextUtils.isEmpty(correo)){
            esValido = false;
            edttxtEmail.setError("Requerido");
        }else if(!correo.matches("^(.+)@(.+)$")){
            esValido = false;
            edttxtEmail.setError("Ingrese un correo electrónico");
        }
        if( TextUtils.isEmpty(nombre)){
            esValido = false;
            edttxtNombre.setError("Requerido");
        }
        if(TextUtils.isEmpty(apellido)){
            esValido = false;
            edttxtApellido.setError("Requerido");
        }
        if(TextUtils.isEmpty(fechaNacimiento)){
            esValido = false;
            edttxtFechaNacimiento.setError("Requerido");
        }
        if(TextUtils.isEmpty(contrasena)){
            esValido = false;
            edttxtContrasena.setError("Requerido");
        }
        if(!edttxtContrasena.getText().toString().matches(edttxtConfirmaContra.getText().toString())){
            esValido = false;
            edttxtContrasena.setError("Las contraseñas no coinciden");
        }
        if(edttxtContrasena.getText().toString().length() <= 5 && !TextUtils.isEmpty(contrasena) ){
            esValido = false;
            edttxtContrasena.setError("La contraseña es muy corta");
        }
        return esValido;
    }

    public void registro(){
        String email,password;
        
        email = edttxtEmail.getText().toString();
        password = edttxtContrasena.getText().toString();
        
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("TAG", "createUserWithEmail:onComplete: " + task.isSuccessful());
                    user = mAuth.getCurrentUser();
                    if(user!=null){
                        UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                        upcrb.setDisplayName(edttxtNombre.getText().toString()+" "+edttxtApellido.getText().toString());
                        persisitir();
                        user.updateProfile(upcrb.build());
                        updateUI(user);
                    }
                }
            }
        });

    }
    public void persisitir(){
        String nombre,apellido,email,id;
        Date fechaNacimiento = null;
        nombre = edttxtNombre.getText().toString();
        apellido = edttxtApellido.getText().toString();
        email = edttxtEmail.getText().toString();
        try {
            fechaNacimiento = new SimpleDateFormat("dd/MM/yy").parse(edttxtFechaNacimiento.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(user != null){
            // List<String> idActividades = new ArrayList<String>();
            Usuario usuario = new Usuario();
            usuario.setIdBeneficiario(user.getUid());
            usuario.setNombres(nombre);
            usuario.setApellidos(apellido);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setRol("Usuario");
            usuario.setEmail(email);
            usuario.setCurso("NA");
            focus.getUsuarios().add(usuario);
            myRef = database.getReference(PATH_USUARIOS+user.getUid());
            myRef.setValue(usuario);
            Toast.makeText(getApplicationContext(),"Registro realizado con éxito", Toast.LENGTH_LONG).show();
        }
    }
}
