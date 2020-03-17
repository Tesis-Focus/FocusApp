package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeAppActivity extends AppCompatActivity {

    private ImageView imageView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private FirebaseAuth mAuth;
    ImageButton btnPerfiles;
    Button btnTareas;
    ImageButton btnAgregarTarea;
    Button btnActividades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_app);
        mAuth = FirebaseAuth.getInstance();
        btnPerfiles = findViewById(R.id.btnPerfiles);
        btnTareas = findViewById(R.id.btnTareas);
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea);
        btnActividades = findViewById(R.id.btnActividades);
        setUpView();
        setUpViewPageAdapter();
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

        btnPerfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent detallePerfil = new Intent(view.getContext(), DetallePerfilActivity.class);
                startActivity(detallePerfil);
            }
        });

        btnTareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent verTarea = new Intent(view.getContext(), VisualizarTareasActivity.class);
                startActivity(verTarea);
            }
        });

        btnAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent agrTarea = new Intent(view.getContext(), AgregarTareaActivity.class);
                startActivity(agrTarea);
            }
        });


    }
}
