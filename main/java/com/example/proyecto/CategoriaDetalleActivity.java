package com.example.proyecto;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CategoriaDetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_detalle);

        String categoriaNombre = getIntent().getStringExtra("CATEGORIA_NOMBRE");

        TextView textView = findViewById(R.id.tv_categoria_nombre);
        textView.setText(categoriaNombre);
    }
}

