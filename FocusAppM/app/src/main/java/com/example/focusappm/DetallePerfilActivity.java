package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class DetallePerfilActivity extends AppCompatActivity {

    EditText edtxNombreDetallePB,edtxApellidosDetallePB,edtxGradoDetallePB,edtxFechaNacDetallePB;
    Button btnAgregarHorDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_perfil);

        btnAgregarHorDis = findViewById(R.id.btnAgregarHorDis);
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

        btnAgregarHorDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),CalendarDisponibleActivity.class);
                i.putExtra("beneficiario",beneficiario);
                startActivity(i);
            }
        });
    }


}
