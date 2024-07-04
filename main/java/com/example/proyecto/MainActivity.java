package com.example.proyecto;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    // Ejecutar AsyncTask para verificar credenciales en la base de datos
                    new AuthenticateUser().execute(usuario, contraseña);
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

    // AsyncTask para autenticar usuario en la base de datos
    private class AuthenticateUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String contraseña = params[1];
            String url = "http://localhost/easyfind/registro.php?usuario=" + usuario + "&contraseña=" + contraseña;

            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");

                // Leer la respuesta
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Devolver la respuesta como cadena JSON
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return "Error de conexión";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                // Procesar la respuesta JSON
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String usuario = jsonObject.getString("usuario");
                    String nombre = jsonObject.getString("nombre");
                    String apellido = jsonObject.getString("apellido");
                    String celular = jsonObject.getString("celular");

                    // Aquí puedes usar los datos como desees, por ejemplo, mostrarlos en un Toast
                    Toast.makeText(MainActivity.this, "ID: " + id + ", Usuario: " + usuario + ", Nombre: " + nombre, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

