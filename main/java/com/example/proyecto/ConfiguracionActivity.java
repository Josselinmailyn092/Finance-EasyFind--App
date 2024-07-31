package com.example.sistemadeespera;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
<<<<<<< HEAD
=======

import androidx.appcompat.app.AppCompatActivity;
>>>>>>> cloned-repo/main

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Configuración del botón de cierre
        ImageButton btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configuración de los botones del toolbar
        ClipsBar.setupToolbar(findViewById(R.id.toolbar4), ConfiguracionActivity.this);

        // Configuración de los botones de menú
        Button btnCategorias = findViewById(R.id.btn_categorias);
        Button btnInformes = findViewById(R.id.btn_informes);
        Button btnDetallesPerfil = findViewById(R.id.btn_detalles_perfil);
        Button btnPrivacidad = findViewById(R.id.btn_privacidad);
        Button btnCentroSoporte = findViewById(R.id.btn_centro_soporte);
        Button btnTerminosCondiciones = findViewById(R.id.btn_terminos_condiciones);
        Button btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);

        btnCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfiguracionActivity.this, CategoriasActivity.class));
            }
        });

        btnInformes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfiguracionActivity.this, InformeActivity.class));
            }
        });

        btnDetallesPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfiguracionActivity.this, DetallesPerfilActivity.class));
            }
        });

        btnPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfiguracionActivity.this, PrivacidadActivity.class));
            }
        });

        btnCentroSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfiguracionActivity.this, CentroSoporteActivity.class));
            }
        });

        btnTerminosCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfiguracionActivity.this, TerminosCondicionesActivity.class));
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para cerrar sesión, como limpiar datos del usuario
                startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
