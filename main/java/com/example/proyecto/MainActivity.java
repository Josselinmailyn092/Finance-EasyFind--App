package com.example.probarproyecto;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private EditText usuarioEditText,contrasenaEditText;
    private Button iniciarSesionButton;
    private TextView registroTextView,sinCuentaTextView;
    private BaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db= new BaseDatos(this);

        // Asociar vistas de XML con variables Java
        usuarioEditText = findViewById(R.id.usuario);
        contrasenaEditText = findViewById(R.id.contrasena);
        iniciarSesionButton = findViewById(R.id.Iniciar);
        registroTextView = findViewById(R.id.Registro);
        sinCuentaTextView = findViewById(R.id.sin_cuenta);

        // Configurar acciones cuando se hace clic en "Iniciar Sesión"
        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = usuarioEditText.getText().toString();
                String contrasena = contrasenaEditText.getText().toString();

                // Validar usuario y contraseña (puedes implementar tu lógica aquí)
                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = db.getUsuario(usuario);
                    if (cursor !=null && cursor.moveToFirst()) {
                        int idColumnIndex = cursor.getColumnIndexOrThrow("id");
                        int contrasenaColumnIndex = cursor.getColumnIndexOrThrow("contrasena");

                        String dbContraseña = cursor.getString(contrasenaColumnIndex);

                        if (dbContraseña.equals(contrasena)) {

                            int userId=cursor.getInt(idColumnIndex);
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            // Navegar a InicioActivity con el ID del usuario
                            Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                            finish(); // Cierra la actividad de login para que el usuario no pueda volver a ella con el botón de retroceso
                        } else {
                            Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
            }
        });

        // Configurar acción cuando se hace clic en "Registrarse"
        registroTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la nueva actividad
                Intent intent = new Intent(MainActivity.this, Registro.class);
                // Iniciar la nueva actividad
                startActivity(intent);
            }
        });

        // Configurar acción cuando se hace clic en "Acceder sin tener cuenta"
        sinCuentaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Acceso temporal no disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

