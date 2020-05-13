package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeAppActivity extends AppCompatActivity implements IpantallaCompleta {
    private ImageView imageView;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    Spinner spnPerfiles;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btnActividades,btnTareas;
    Button btnPerfiles;
    List<Usuario> beneficiarios;
    List<String> nombresBeneficiarios;
    List<String> idsBeneficiarios;
    private final static String PATH_USUARIOS = "usuarios/";
    private final static String PATH_TAREAS = "tareas/";
    ArrayAdapter<String> adapter;
    String idBeneficiario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_app);
        Activity a = getParent();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        btnPerfiles = findViewById(R.id.btnPerfiles);
        btnActividades = findViewById(R.id.btnActividades);
        spnPerfiles = findViewById(R.id.spnPerfiles);
        btnTareas = findViewById(R.id.btnTareas);
        beneficiarios = new ArrayList<>();
        nombresBeneficiarios = new ArrayList<>();
        idsBeneficiarios = new ArrayList<>();
        user = mAuth.getCurrentUser();

        btnPerfiles.setEnabled(false);
        btnActividades.setEnabled(false);
        btnTareas.setEnabled(false);

        setUpView();
        setUpViewPageAdapter("");

        spnPerfiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spnPerfiles.getSelectedItem().toString().equals("Integrada"))
                    setUpViewPageAdapter(beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario());
                else
                    setUpViewPageAdapter("Integrada");
                //UtilsFocus.planeacion(beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario(),HomeAppActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPerfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),PerfilesActivity.class);
                i.putExtra("beneficiarios", (Serializable) beneficiarios);
                i.putExtra("nombreBeneficiarios", (Serializable) nombresBeneficiarios);
                startActivity(i);
            }
        });



        btnTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),TareasActivity.class);
                i.putExtra("idBeneficiario",beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario());
                i.putExtra("beneficiarios", (Serializable) beneficiarios);
                i.putExtra("nombreBeneficiarios", (Serializable) (new ArrayList<>( nombresBeneficiarios.subList(0,nombresBeneficiarios.size()-1))));
                startActivity(i);
            }
        });

        btnActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),ActividadesActivity.class);
                i.putExtra("idBeneficiario",beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario());
                i.putExtra("beneficiarios", (Serializable) beneficiarios);
                i.putExtra("nombreBeneficiarios", (Serializable) (new ArrayList<>( nombresBeneficiarios.subList(0,nombresBeneficiarios.size()-1))));
                startActivity(i);
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
        if(itemClicked == R.id.mnuAregarActividad){
            Intent intent= new Intent(getBaseContext(),AgregarActividadActivity.class);
            intent.putExtra("beneficiarios", (Serializable) beneficiarios);
            intent.putExtra("nombreBeneficiarios", (Serializable) (new ArrayList<>( nombresBeneficiarios.subList(0,nombresBeneficiarios.size()-1))));
            intent.putExtra("codigo",0);
            startActivity(intent);
        }
        if(itemClicked == R.id.mnuAregarTarea){
            Intent intentAgrTarea = new Intent(getBaseContext(), AgregarTareaActivity.class);
            intentAgrTarea.putExtra("beneficiarios", (Serializable) beneficiarios);
            intentAgrTarea.putExtra("nombreBeneficiarios", (Serializable) (new ArrayList<>( nombresBeneficiarios.subList(0,nombresBeneficiarios.size()-1))));
            intentAgrTarea.putExtra("codigo",0);
            startActivity(intentAgrTarea);
        }
        if(itemClicked == R.id.menuPerfiles){
            Intent i = new Intent(getBaseContext(),PerfilesActivity.class);
            i.putExtra("beneficiarios", (Serializable) beneficiarios);
            i.putExtra("nombreBeneficiarios", (Serializable) nombresBeneficiarios);
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
                        idsBeneficiarios.add(beneficiario.getIdBeneficiario());
                        Log.i("beneficiarios", "onDataChange: "+(beneficiario.getNombres()+" "+beneficiario.getApellidos()));
                    }
                }
                nombresBeneficiarios.add("Integrada");
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nombresBeneficiarios);
                spnPerfiles.setAdapter(adapter);
                if(!(nombresBeneficiarios.size()<2)){
                    btnActividades.setEnabled(true);
                    btnTareas.setEnabled(true);
                }
                btnPerfiles.setEnabled(true);
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


    private void setUpViewPageAdapter(String idBeneficiario){
        //actualizarTiempoTareas(idBeneficiario);
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        Fragment detalle = new SemanaFragment();

        Bundle bundle = new Bundle();

        bundle.putStringArrayList("idsBeneficiarios", (ArrayList<String>) idsBeneficiarios);
        bundle.putString("idBeneficiario",idBeneficiario);
        detalle.setArguments(bundle);

        fragments.add(detalle);
        names.add("Planeación");

        viewPageAdapter.setFragmentList(fragments);
        viewPageAdapter.setFragmentTitles(names);


        viewPager.setAdapter(viewPageAdapter);
        viewPager.setPagingEnabled(false);


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

    private void actualizarTiempoTareas(String idBeneficiario) {
        //cuando ya paso el total del tiempo de la tarea
        //cuando esta en la mitad del tiemp
        //horario debe tener estado
        Date fechaHoy = new Date();
        if(idBeneficiario != "") {
            myRef = database.getReference(PATH_TAREAS);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Tarea tarea = ds.getValue(Tarea.class);
                        List<Horario> horariosTarea = new ArrayList<Horario>();
                        if (idBeneficiario.equals(tarea.getIdBeneficiario())) {

                            horariosTarea.addAll(tarea.getHorarios());
                            for (int i = 0; i < horariosTarea.size(); i++) {
                                float diferencia;
                                myRef = database.getReference(PATH_TAREAS + tarea.getIdTarea());
                                Horario horario = horariosTarea.get(i);

                                horario.getmStartTime().setYear(horario.getmStartTime().getYear()-1900);
                                horario.getmEndTime().setYear(horario.getmEndTime().getYear()-1900);
                                Log.i("Tiempo", "horario tarea start "+horario.getmStartTime()+" end "+horario.getmEndTime()+" / fechahoy "+fechaHoy);

                                if (horario.getmStartTime().before(fechaHoy) && horario.getmEndTime().after(fechaHoy) && !horario.isActualizado()) {
                                    diferencia = ((fechaHoy.getTime() - horario.getmStartTime().getTime()) / (60*1000));
                                    tarea.setTiempoPromedio(tarea.getTiempoPromedio() - diferencia);
                                    Log.i("Tiempo", "tiempo medio" + diferencia);
                                    tarea.getHorarios().get(i).setActualizado(false);
                                    myRef.setValue(tarea);

                                } else if (horario.getmStartTime().before(fechaHoy) && horario.getmEndTime().before(fechaHoy) && !horario.isActualizado()) {
                                    diferencia = (horario.getmEndTime().getTime() - horario.getmStartTime().getTime()) / (60*1000);
                                    tarea.setTiempoPromedio(tarea.getTiempoPromedio() - diferencia);
                                    Log.i("Tiempo", "tiempo despues" + diferencia);
                                    horario.setActualizado(true);
                                    tarea.getHorarios().get(i).setActualizado(true);
                                    myRef.setValue(tarea);
                                }else{
                                    Log.i("Tiempo", "onDataChange: noentra a ninguno");
                                }

                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpViewPageAdapter("");
        beneficiarios.clear();
        nombresBeneficiarios.clear();
        idsBeneficiarios.clear();
        cargarPerfilesB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUpViewPageAdapter("");
        beneficiarios.clear();
        nombresBeneficiarios.clear();
        idsBeneficiarios.clear();
        cargarPerfilesB();
    }

    @Override
    public void onPantallaCompletaClicked() {
        Intent i = new Intent(HomeAppActivity.this,CalendarioPantallaCompletaActivity.class);
        Bundle b = new Bundle();
        if(spnPerfiles.getSelectedItem().toString().equals("Integrada")){
            b.putString("idBeneficiario","Integrada");
        }else{
            b.putString("idBeneficiario",beneficiarios.get(spnPerfiles.getSelectedItemPosition()).getIdBeneficiario());
        }
        b.putStringArrayList("idsBeneficiarios", (ArrayList<String>) idsBeneficiarios);
        i.putExtras(b);
        startActivity(i);
    }

}

