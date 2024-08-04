package com.example.proyecto;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class TerminosCondicionesActivity extends AppCompatActivity {
    private ImageButton btnCerrar;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminoscondiciones);

        btnCerrar= findViewById(R.id.btnCerrar_terminosC);
        btnCerrar.setOnClickListener(v -> finish());
    }
}
