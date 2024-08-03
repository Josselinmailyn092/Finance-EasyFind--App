package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class CentroSoporteActivity extends AppCompatActivity {

    private ImageButton btnCerrarSoporte;
    private Button btnPreguntasFrecuentes;
    private Button btnContactarSoporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        btnCerrarSoporte = findViewById(R.id.btn_cerrar_soporte);
        btnPreguntasFrecuentes = findViewById(R.id.btn_preguntas_frecuentes);
        btnContactarSoporte = findViewById(R.id.btn_contactar_soporte);

        btnCerrarSoporte.setOnClickListener(v -> finish());


    }
}
