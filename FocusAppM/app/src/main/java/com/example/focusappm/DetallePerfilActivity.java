package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class DetallePerfilActivity extends AppCompatActivity {

    EditText edtxNombreDetallePB,edtxApellidosDetallePB,edtxGradoDetallePB,edtxFechaNacDetallePB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_perfil);
        edtxNombreDetallePB = findViewById(R.id.edtxNombreDetallePB);
        edtxApellidosDetallePB = findViewById(R.id.edtxApellidosDetallePB);
        edtxGradoDetallePB = findViewById(R.id.edtxGradoDetallePB);
        edtxFechaNacDetallePB = findViewById(R.id.edtxFechaNacDetallePB);
        Usuario beneficiario = (Usuario) getIntent().getSerializableExtra("Beneficiario");
        edtxNombreDetallePB.setText(beneficiario.getNombres());
        edtxApellidosDetallePB.setText(beneficiario.getApellidos());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String fechaNac = df.format(beneficiario.getFechaNacimiento());
        edtxFechaNacDetallePB.setText(fechaNac);
        edtxGradoDetallePB.setText(beneficiario.getCurso());
    }
}
