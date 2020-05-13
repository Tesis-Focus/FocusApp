package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class InstruccionesTest extends AppCompatActivity {

    Button btnEmpezar;
    VideoView videoView;
    Usuario beneficiario;
    Integer codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones_test);

        beneficiario = (Usuario) getIntent().getSerializableExtra("beneficiario");
        codigo =(Integer) getIntent().getSerializableExtra("codigo");
        btnEmpezar = findViewById(R.id.btnEmpezar);
        /*
        videoView = findViewById(R.id.videoView);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.ejemplo_test;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

         */

        btnEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), TestAprendizajeActivity.class);
                intent.putExtra("beneficiario", beneficiario);
                intent.putExtra("codigo",0);
                startActivity(intent);
            }
        });
    }
}
