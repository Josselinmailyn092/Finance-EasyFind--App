package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private EditText usuarioEditText;
    private EditText contraseñaEditText;
    private Button iniciarSesionButton;
    private TextView registroTextView;
    private TextView sinCuentaTextView;
    private BaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db= new BaseDatos(this);

        // Asociar vistas de XML con variables Java
        usuarioEditText = findViewById(R.id.usuario);
        contraseñaEditText = findViewById(R.id.contraseña);
        iniciarSesionButton = findViewById(R.id.Iniciar);
        registroTextView = findViewById(R.id.Registro);
        sinCuentaTextView = findViewById(R.id.sin_cuenta);

        // Configurar acciones cuando se hace clic en "Iniciar Sesión"
        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = usuarioEditText.getText().toString();
                String contraseña = contraseñaEditText.getText().toString();

                // Validar usuario y contraseña (puedes implementar tu lógica aquí)
                if (usuario.isEmpty() || contraseña.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = db.getUsuario(usuario);
                    if (cursor.moveToFirst()) {
                        String dbContraseña = cursor.getString(cursor.getColumnIndex("contraseña"));
                        if (dbContraseña.equals(contraseña)) {
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            // Aquí puedes redirigir a otra actividad si lo deseas
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
                // Aquí podrías realizar alguna acción adicional si deseas
            }
        });
    }


}

