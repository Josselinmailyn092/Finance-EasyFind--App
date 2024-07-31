package com.example.proyecto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Registro extends AppCompatActivity {

    private EditText usuario, nombre, apellido, celular, contraseña, confirmarContraseña;
    private Spinner ocupacion;
    private Button registrarse;
    private BaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = new BaseDatos(this);

        // Inicializar los componentes
        usuario = findViewById(R.id.usuario);
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        celular = findViewById(R.id.celular);
        contraseña = findViewById(R.id.contraseña2);
        confirmarContraseña = findViewById(R.id.confirmar_contraseña);
        ocupacion = findViewById(R.id.ocupacion);
        registrarse = findViewById(R.id.registrarse);

        // Configurar el spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ocupacion_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ocupacion.setAdapter(adapter);

        // Configurar el evento de clic del botón
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados
                String usuarioText = usuario.getText().toString();
                String nombreText = nombre.getText().toString();
                String apellidoText = apellido.getText().toString();
                String celularText = celular.getText().toString();
                String contraseñaText = contraseña.getText().toString();
                String confirmarContraseñaText = confirmarContraseña.getText().toString();
                String ocupacionText = ocupacion.getSelectedItem().toString();

                // Validar los campos
                if (usuarioText.isEmpty() || nombreText.isEmpty() || apellidoText.isEmpty() ||
                        celularText.isEmpty() || contraseñaText.isEmpty() || confirmarContraseñaText.isEmpty()) {
                    Toast.makeText(Registro.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                } else if (!contraseñaText.equals(confirmarContraseñaText)) {
                    Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else if (ocupacionText.equals("Seleccione su ocupación")) {
                    Toast.makeText(Registro.this, "Seleccione una ocupación válida", Toast.LENGTH_SHORT).show();
                } else {
                    long id = db.addUsuario(usuarioText, nombreText, apellidoText, celularText, contraseñaText, ocupacionText);
                    if (id > 0) {
                        showToast("Registro completado exitosamente");
                        Intent intent = new Intent(Registro.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("Error en el registro");


                    }
                }
            }
        });
    }
    private void showToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, findViewById(R.id.custom_toast_container));

        TextView textView = layout.findViewById(R.id.textViewToast);
        textView.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
