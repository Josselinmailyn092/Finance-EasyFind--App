package com.example.proyecto;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetallesPerfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgfotoPerfil;
    private EditText usuario, nombreUsu, apellidoUsu, celularUsu, contrasenaUsu;
    private Spinner ocupacionUsu;
    private Button btnGuardar;
    private BaseDatos baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_user);

        // Inicializar vistas
        imgfotoPerfil = findViewById(R.id.imgfotoPerfil);
        usuario = findViewById(R.id.usuario1);
        nombreUsu = findViewById(R.id.nombre1);
        apellidoUsu = findViewById(R.id.apellido1);
        celularUsu = findViewById(R.id.celular1);
        contrasenaUsu = findViewById(R.id.contrasena1);
        ocupacionUsu = findViewById(R.id.ocupacion1);
        btnGuardar = findViewById(R.id.btnguardar);

        // Configurar el Spinner de ocupación
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ocupacion_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ocupacionUsu.setAdapter(adapter);

        // Inicializar la base de datos
        baseDatos = new BaseDatos(this);

        // Cargar datos del usuario actual
        cargarDatosUsuario();

        // Configurar el botón de guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarUsuario();
            }
        });
    }

    private void cargarDatosUsuario() {
        // TODO: Implementar la carga de datos del usuario actual
        // Esto dependerá de cómo almacenas y recuperas los datos del usuario actual
        // Por ejemplo:
        // String usuarioActual = obtenerUsuarioActual(); // Método que debes implementar
        // Usuario user = baseDatos.obtenerUsuarioPorNombre(usuarioActual);
        // if (user != null) {
        //     usuario.setText(user.getUsuario());
        //     nombreUsu.setText(user.getNombre());
        //     apellidoUsu.setText(user.getApellido());
        //     celularUsu.setText(user.getCelular());
        //     contrasenaUsu.setText(user.getContrasena());
        //
        //     ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) ocupacionUsu.getAdapter();
        //     int spinnerPosition = adapter.getPosition(user.getOcupacion());
        //     ocupacionUsu.setSelection(spinnerPosition);
        // }
    }

    private void actualizarUsuario() {
        String usuarioStr = usuario.getText().toString();
        String nombreStr = nombreUsu.getText().toString();
        String apellidoStr = apellidoUsu.getText().toString();
        String celularStr = celularUsu.getText().toString();
        String contrasenaStr = contrasenaUsu.getText().toString();
        String ocupacionStr = ocupacionUsu.getSelectedItem().toString();

        // Verifica que los campos no estén vacíos
        if (usuarioStr.isEmpty() || nombreStr.isEmpty() || apellidoStr.isEmpty() ||
                celularStr.isEmpty() || contrasenaStr.isEmpty() || ocupacionStr.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualiza el usuario en la base de datos
        int filasActualizadas = baseDatos.updateUsuario(usuarioStr, nombreStr, apellidoStr,
                celularStr, contrasenaStr, ocupacionStr);

        if (filasActualizadas > 0) {
            Toast.makeText(this, "Usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad después de actualizar
        } else {
            Toast.makeText(this, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
        }
    }
}