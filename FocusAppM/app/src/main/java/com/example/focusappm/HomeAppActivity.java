package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;


public class HomeAppActivity extends AppCompatActivity {

    private ImageView imageView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    Spinner spnPerfiles;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button mostrarActividades;
    Button mostrarTareas;
    ImageButton agregarActividad,btnPerfiles, agregarTarea;
    List<Usuario> beneficiarios;
    List<String> nombresBeneficiarios;
    private final static String PATH_USUARIOS = "usuarios/";
    private final static String PATH_TAREAS = "tareas/";
    ArrayAdapter<String> adapter;
    String idBeneficiario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_app);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        btnPerfiles = findViewById(R.id.btnPerfiles);
        agregarActividad = (ImageButton)findViewById(R.id.agregarActividad);
        agregarTarea = findViewById(R.id.agregarTarea);
        mostrarActividades = findViewById(R.id.mostrarActividades);
        mostrarTareas = findViewById(R.id.mostrarTareas);
        spnPerfiles = findViewById(R.id.spnPerfiles);
        beneficiarios = new ArrayList<>();
        nombresBeneficiarios = new ArrayList<>();
        user = mAuth.getCurrentUser();

        setUpView();
        setUpViewPageAdapter();
        cargarPerfilesB();

        btnPerfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),PerfilesActivity.class);
                i.putExtra("beneficiarios", (Serializable) beneficiarios);
                i.putExtra("nombreBeneficiarios", (Serializable) nombresBeneficiarios);
                startActivity(i);
            }
        });

        agregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getBaseContext(),AgregarActividadActivity.class);
                intent.putExtra("beneficiarios", (Serializable) beneficiarios);
                intent.putExtra("nombreBeneficiarios", (Serializable) nombresBeneficiarios);
                startActivity(intent);
            }
        });

        agregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAgrTarea = new Intent(getBaseContext(), AgregarTareaActivity.class);
                intentAgrTarea.putExtra("beneficiarios", (Serializable) beneficiarios);
                intentAgrTarea.putExtra("nombreBeneficiarios", (Serializable) nombresBeneficiarios);
                startActivity(intentAgrTarea);
            }
        });

        mostrarActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = dataBase.getReference();

                myRef.child(PATH_TAREAS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Tarea> tareas = new ArrayList<Tarea>();
                        ArrayList<Horario> horarios = new ArrayList<Horario>();
                        idBeneficiario = beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            Tarea tarea = ds.getValue(Tarea.class);
                            if(tarea.getIdBeneficiario().equals(idBeneficiario)){
                                horarios.addAll(tarea.getHorarios());
                            }

                        }
                        Intent intent= new Intent(getBaseContext(),PlaneacionActivity.class);
                        intent.putExtra("eventos", horarios);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
                Intent intent = new Intent(getBaseContext(),ActividadesActivity.class);
               //idBeneficiario = beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario();
                Usuario beneficiario = beneficiarios.get(spnPerfiles.getSelectedItemPosition());
               // intent.putExtra("idBeneficiario",idBeneficiario);
                intent.putExtra("Beneficiario",beneficiario);
                startActivity(intent);
            }
        });

        mostrarTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),TareasActivity.class);
                idBeneficiario = beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario();
                intent.putExtra("idBeneficiario",idBeneficiario);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked==R.id.menuCerrarSesion){
            mAuth.signOut();
            Intent i = new Intent(getBaseContext(),InicioActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
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
                spnPerfiles.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpView(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPage);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

    }

    private void setUpViewPageAdapter(){
        viewPageAdapter.addFragment(new MesFragment(),"Mes");
        viewPageAdapter.addFragment(new SemanaFragment(),"Semana");
        viewPageAdapter.addFragment(new DiaFragment(),"Dia");
        viewPager.setAdapter(viewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
