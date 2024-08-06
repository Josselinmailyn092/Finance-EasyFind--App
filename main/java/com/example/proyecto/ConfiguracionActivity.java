package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionActivity extends AppCompatActivity {
    private int usuarioId;
    private BaseDatos bd;
    TextView nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        Intent intent = getIntent();
        usuarioId = intent.getIntExtra("USER_ID", -1);
        bd = new BaseDatos(this);
        // Configuración del botón de cierre
        ImageButton btnClose = findViewById(R.id.btn_cerrar);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nombreUsuario = findViewById(R.id.tv_user_name);
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

        cargarDatosUsuario(usuarioId);
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
                intent.putExtra("USER_ID", usuarioId);
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
                SharedPreferences sharedPref = getSharedPreferences("id", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear().apply();
                startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
                finish();
                onDestroy();

            }
        });



    }

    private void cargarDatosUsuario(int userId) {
        Cursor cursor = null;
        try {
            cursor = bd.getUsuarioById(userId);
            if (cursor != null && cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"));

                nombreUsuario.setText(String.format("%s %s", nombre, apellido));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}