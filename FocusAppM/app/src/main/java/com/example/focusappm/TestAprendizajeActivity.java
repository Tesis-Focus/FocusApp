package com.example.focusappm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class TestAprendizajeActivity extends AppCompatActivity {

    ArrayList<String> listDatos;
    RecyclerView recycler;
    AdapterDatos adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aprendizaje);

        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false ));

        listDatos= new ArrayList<String>();

        for (int i=0; i<= 50; i++) {
            listDatos.add("Dato # "+i+" ");
        }

        adapter = new AdapterDatos(listDatos);
       // new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recycler);
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setmTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recycler);
        recycler.setAdapter(adapter);
    }

    /*ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            listDatos.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();

        }
    };*/
}
