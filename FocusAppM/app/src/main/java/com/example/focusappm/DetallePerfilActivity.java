package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DetallePerfilActivity extends AppCompatActivity {

    EditText edtxNombreDetallePB,edtxApellidosDetallePB,edtxGradoDetallePB,edtxFechaNacDetallePB;
    Button btnAgregarHorDis;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static final String PATH_HORARIO_DISPONIBLE = "horarioDisponible2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_perfil);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
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
                passEvents();
            }
        });
    }

    private void passEvents() {
        Usuario beneficiario = (Usuario) getIntent().getSerializableExtra("Beneficiario");
        ArrayList<Horario> eventsHorario = new ArrayList<>();
        myRef = database.getReference(PATH_HORARIO_DISPONIBLE);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot sd : dataSnapshot.getChildren()){
                    Horario horario = sd.getValue(Horario.class);
                    if( beneficiario.getIdBeneficiario().equals(horario.getmId())){
                        eventsHorario.add(horario);
                    }
                }
                Log.i("cal", "tamanio de eventos llega "+eventsHorario.size());
                Intent i = new Intent(getBaseContext(),CalendarDisponibleActivity.class);
                i.putExtra("beneficiario",beneficiario);
                i.putExtra("eventos",eventsHorario);
                startActivity(i);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
