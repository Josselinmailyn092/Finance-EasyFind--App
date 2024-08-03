package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionActivity extends AppCompatActivity {
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        Intent intent = getIntent();
        usuarioId = intent.getIntExtra("USER_ID", -1);

        // Configuración del botón de cierre
        ImageButton btnClose = findViewById(R.id.btn_cerrar);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configuración de los botones del toolbar
        ClipsBar.setupToolbar(findViewById(R.id.toolbar4), ConfiguracionActivity.this, usuarioId);

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
                Intent intent = new Intent(ConfiguracionActivity.this, CategoriasActivity.class);
                intent.putExtra("USER_ID", usuarioId);
                startActivity(intent);
            }
        });

        btnInformes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfiguracionActivity.this, InformeActivity.class);
                startActivity(intent);
            }
        });

        btnDetallesPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfiguracionActivity.this, DetallesPerfilActivity.class);
                startActivity(intent);
            }
        });

        btnPrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfiguracionActivity.this, PrivacidadActivity.class);
                startActivity(intent);
            }
        });

        btnCentroSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfiguracionActivity.this, CentroSoporteActivity.class);
                startActivity(intent);
            }
        });

        btnTerminosCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfiguracionActivity.this, TerminosCondicionesActivity.class);
                startActivity(intent);
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
