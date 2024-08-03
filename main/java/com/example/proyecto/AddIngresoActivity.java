package com.example.probarproyecto;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngresoActivity extends AppCompatActivity {

    private EditText editMonto, editTitulo, editDescripcion;
    private Button btnRegistrarIngreso;
    private ImageButton btnClose;
    private int userId;
    private BaseDatos bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_dinero);

        // Obtener el ID del usuario de la actividad anterior
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        // Verificar si el ID del usuario es válido
        if (userId == -1) {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar base de datos
        bd = new BaseDatos(this);

        // Referenciar vistas
        editMonto = findViewById(R.id.monto);
        editTitulo = findViewById(R.id.titulo);
        editDescripcion = findViewById(R.id.descripcion);
        btnRegistrarIngreso = findViewById(R.id.btnRegistrarIngreso);
        btnClose = findViewById(R.id.btn_close);

        // Establecer onClickListener para el botón de registrar ingreso
        btnRegistrarIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarIngreso();
            }
        });

        // Establecer onClickListener para el botón de cerrar
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registrarIngreso() {
        // Obtener valores de los campos de texto
        String montoString = editMonto.getText().toString();
        String titulo = editTitulo.getText().toString();
        String descripcion = editDescripcion.getText().toString();

        // Validar que los campos no estén vacíos
        if (montoString.isEmpty() || titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir monto a double
        double monto;
        try {
            monto = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        // Obtener la fecha actual del sistema
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Insertar ingreso en la base de datos
        bd.insertIngreso(userId, monto, fechaActual, descripcion);

        // Mostrar mensaje de éxito
        Toast.makeText(this, "Ingreso registrado correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AddIngresoActivity.this, InicioActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Traer la actividad al frente si ya existe
        startActivity(intent);
    }
}