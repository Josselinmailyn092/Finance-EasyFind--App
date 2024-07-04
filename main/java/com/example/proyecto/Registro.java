package com.example.proyecto;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class Registro extends AppCompatActivity {

    private EditText usuario, nombre, apellido, celular, contraseña, confirmarContraseña;
    private Spinner ocupacion;
    private Button registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

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
                    // Aquí se envían los datos al servidor
                    new RegisterUser().execute(usuarioText, nombreText, apellidoText, celularText, contraseñaText, ocupacionText);
                }
            }
        });
    }

    private class RegisterUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String nombre = params[1];
            String apellido = params[2];
            String celular = params[3];
            String contraseña = params[4];
            String ocupacion = params[5];

            HashMap<String, String> postDataParams = new HashMap<>();
            postDataParams.put("usuario", usuario);
            postDataParams.put("nombre", nombre);
            postDataParams.put("apellido", apellido);
            postDataParams.put("celular", celular);
            postDataParams.put("contraseña", contraseña);
            postDataParams.put("ocupacion", ocupacion);

            return makeServiceCall("http://192.168.100.221/easyfind/register.php", postDataParams); // Cambia la URL aquí
        }

        // Dentro del método onPostExecute del AsyncTask
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // Mostrar Toast personalizado
                showToast("Registro completado exitosamente");
                if (result.equals("Registro exitoso")) {
                    Intent intent = new Intent(Registro.this, MainActivity.class);
                    startActivity(intent);
                    //finish(); // Finalizar la actividad de registro
                }
            } else {
                showToast("Error en el registro");
            }
        }
        // Método para mostrar el Toast personalizado
        private void showToast(String message) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout,
                    findViewById(R.id.custom_toast_container));

            TextView textView = layout.findViewById(R.id.textViewToast);
            textView.setText(message);

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG); // Duración larga o corta según necesites
            toast.setView(layout);
            toast.show();
        }


        private String makeServiceCall(String requestURL, HashMap<String, String> postDataParams) {
            URL url;
            String response = "";
            try {
                url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    response = br.readLine();
                } else {
                    response = "Error: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        private String getPostDataString(HashMap<String, String> params) throws Exception {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }
}


