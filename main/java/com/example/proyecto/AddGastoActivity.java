package com.example.probarproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddGastoActivity extends AppCompatActivity {

    private EditText etMonto, etDescripcion;
    private Spinner spCategoria;
    private Button btnRegistrarIngreso;
    private ImageButton btnClose;
    private int userId;
    private BaseDatos baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_gasto);

        // Inicializar la base de datos
        baseDatos = new BaseDatos(this);

        // Obtener el ID del usuario desde la intención
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        // Verificar si se recibió correctamente el ID del usuario
        if (userId == -1) {
            Toast.makeText(this, "Error al recibir el ID del usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Vincular vistas
        etMonto = findViewById(R.id.monto);
        etDescripcion = findViewById(R.id.descripcion);
        spCategoria = findViewById(R.id.categoria);
        btnRegistrarIngreso = findViewById(R.id.btnRegistrarIngreso);
        btnClose = findViewById(R.id.btn_close);

        // Configurar el botón de cerrar
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Cargar categorías en el Spinner
        cargarCategorias();

        // Configurar el botón de registrar ingreso
        btnRegistrarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarIngreso();
            }
        });
    }

    private void cargarCategorias() {
        ArrayList<String> categorias = new ArrayList<>();
        Cursor cursor = baseDatos.getCategoriasPorUsuario(userId);

        if (cursor.moveToFirst()) {
            do {
                String categoria = cursor.getString(cursor.getColumnIndexOrThrow("nombre_columna_categoria"));
                categorias.add(categoria);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }

    private void registrarIngreso() {
        // Obtener valores de los campos de texto
        String montoStr = etMonto.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spCategoria.getSelectedItem().toString().trim();

        // Validar los campos
        if (montoStr.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto = Double.parseDouble(montoStr);

        // Obtener la fecha actual del sistema
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Insertar el ingreso en la base de datos
        baseDatos.insertIngreso(userId, monto, fechaActual, descripcion);

        // Mostrar mensaje de éxito y finalizar la actividad
        Toast.makeText(this, "Ingreso registrado exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}
