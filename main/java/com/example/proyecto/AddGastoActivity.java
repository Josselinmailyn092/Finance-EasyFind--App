package com.example.proyecto;

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
import java.util.HashMap;
import java.util.Locale;
public class AddGastoActivity extends AppCompatActivity {

    private EditText etMonto, etDescripcion;
    private Spinner spCategoria;
    private Button btnRegistrarIngreso;
    private ImageButton btnClose;
    private int userId;
    private BaseDatos baseDatos;
    private ImageButton btnCerrar;
    private HashMap<String, Integer> categoriaMap; // Para mapear categorías con sus IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_gasto);

        // Inicializar vistas
        btnCerrar = findViewById(R.id.btnCerrargasto);
        btnCerrar.setOnClickListener(v -> finish());

        etMonto = findViewById(R.id.monto);
        etDescripcion = findViewById(R.id.descripcion);
        spCategoria = findViewById(R.id.categoria);
        btnRegistrarIngreso = findViewById(R.id.btnRegistrarIngreso);

        // Inicializar base de datos
        baseDatos = new BaseDatos(this);

        // Obtener ID del usuario desde la intención
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error al recibir el ID del usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar categorías en el Spinner
        cargarCategorias();

        // Configurar el botón de registrar gasto
        btnRegistrarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarGasto();
            }
        });
    }

    private void cargarCategorias() {
        ArrayList<String> categorias;
      categorias= (ArrayList<String>) baseDatos.getAllCategorias(userId);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapter);
    }

    private void registrarGasto() {
        String montoStr = etMonto.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spCategoria.getSelectedItem().toString().trim();

        if (montoStr.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto = Double.parseDouble(montoStr);
        int idCategoria = (int) baseDatos.getCategoriaNombreyUsuario(categoria,userId);
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Insertar el gasto en la base de datos
        baseDatos.insertGasto(idCategoria, userId, monto, fechaActual, descripcion);

        Toast.makeText(this, "Gasto registrado exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }

}
