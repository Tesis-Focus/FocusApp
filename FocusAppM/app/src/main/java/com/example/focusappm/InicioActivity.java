package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class InicioActivity extends AppCompatActivity {

    EditText edttxtCorreo, edttxtContrasena;
    TextView txtvwRegistro;
    Button btnInicioSesion;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mAuth = FirebaseAuth.getInstance();

        edttxtCorreo = findViewById(R.id.edttxtCorreo);
        edttxtContrasena = findViewById(R.id.edttxtContrasena);
        txtvwRegistro = findViewById(R.id.txtvwRegistro);
        btnInicioSesion = findViewById(R.id.btnInicioSesion);

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    String correo = edttxtCorreo.getText().toString();
                    String contrasena = edttxtContrasena.getText().toString();
                    signInUser(correo,contrasena);
                }
            }
        });
        txtvwRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),RegistroActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser){
        if( currentUser != null){
            Intent i = new Intent(getBaseContext(),HomeAppActivity.class);
            i.putExtra("user",currentUser.getEmail());
            startActivity(i);
        }
    }
    private boolean validarCampos(){
        boolean esValido = true;
        String correo = edttxtCorreo.getText().toString();
        String contrasena = edttxtContrasena.getText().toString();
        if(TextUtils.isEmpty(correo)){
            esValido = false;
            edttxtCorreo.setError("Requerido");
        }else if(!correo.matches("^(.+)@(.+)$")){
            esValido = false;
            edttxtCorreo.setError("Ingrese un correo electrónico");
        }

        if(TextUtils.isEmpty(contrasena)){
            esValido = false;
            edttxtContrasena.setError("Requerido");
        }
        if(esValido)
            Log.i("validacion campos", "validarCampos: Campos correctos");
        else
            Log.i("validacion campos", "validarCampos: Campos incorrectos");
        return esValido;

    }
    private void signInUser(String correo, String contrasena){
        mAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("inicio sesion", "signInWithEmail: succsess ");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else if(!task.isSuccessful()){
                    edttxtCorreo.setError("Usuario o contraseña incorrectos");
                    edttxtContrasena.setText("");
                }
            }
        });
    }
}
