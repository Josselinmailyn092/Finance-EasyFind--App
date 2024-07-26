package com.example.sistemadeespera;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.Queue;
import androidx.appcompat.app.AppCompatActivity;



public class EstadoTurnosActivity extends AppCompatActivity {
    private TextView tvTurnoActual;
    private TextView[] tvTurnosProximos = new TextView[4]; // Array para los 4 turnos próximos
    private Button btnProcesarElemento;
    private Button btnVaciarCola, btnNewElemento;
    private ImageButton btnBack;
    private Queue<String> colaEspera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estados);

        // Referencias a los elementos de la interfaz
        tvTurnoActual = findViewById(R.id.tvTurnoActual);
        tvTurnosProximos[0] = findViewById(R.id.tvTurno1);
        tvTurnosProximos[1] = findViewById(R.id.tvTurno2);
        tvTurnosProximos[2] = findViewById(R.id.tvTurno3);
        tvTurnosProximos[3] = findViewById(R.id.tvTurno4);
        btnProcesarElemento = findViewById(R.id.btnProcesarElemento);
        btnVaciarCola = findViewById(R.id.btnVaciarCola);
        btnBack = findViewById(R.id.btnBack);
        btnNewElemento=findViewById(R.id.btnNewElemento);
        // Recibir la cola de espera desde MainActivity
        Intent intent = getIntent();
        if (intent.hasExtra("colaEspera")) {
            colaEspera = new LinkedList<>(intent.getStringArrayListExtra("colaEspera"));
        } else {
            colaEspera = new LinkedList<>();
        }
        // Actualizar la interfaz con los primeros turnos de la cola
        mostrarTurnos();

        // Listener para el botón Procesar Elemento
        btnProcesarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarElementoCola();
            }
        });
        // Listener para el botón Vaciar Cola
        btnVaciarCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaciarCola();
            }
        });
        // Listener para el botón Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Listener para el botón Nuevo ticket
        btnNewElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    // Método para mostrar los turnos actuales y próximos
    private void mostrarTurnos() {
        if (!colaEspera.isEmpty()) {
            // Mostrar el turno actual
            String turnoActual = colaEspera.peek();
            tvTurnoActual.setText(turnoActual);
            // Mostrar los próximos 4 turnos
            int contador = 0;
            for (String turno : colaEspera) {
                if (contador < 5) {
                    if (contador == 0) {
                        tvTurnosProximos[contador].setText(""); // El primer elemento de turnos próximos se vacía
                    } else {
                        tvTurnosProximos[contador - 1].setText(turno); // Los siguientes se asignan a los TextViews
                    }
                    contador++;
                }
            }
            // Vaciar cualquier TextView restante si hay menos de 4 turnos en la cola
            while (contador <= 4) {
                tvTurnosProximos[contador - 1].setText("");
                contador++;
            }
        } else {
            // No hay turnos en la cola, mostrar mensaje vacío o manejar según tu diseño
            tvTurnoActual.setText("Vacio");
            for (TextView textView : tvTurnosProximos) {
                textView.setText("");
            }
        }
    }
    // Método para procesar un elemento de la cola
    private void procesarElementoCola() {
        if (!colaEspera.isEmpty()) {
            colaEspera.poll(); // Elimina el primer elemento de la cola
            mostrarTurnos(); // Actualiza la vista con los nuevos turnos
        }
    }
    // Método para vaciar la cola
    private void vaciarCola() {
        colaEspera.clear(); // Elimina todos los elementos de la cola
        mostrarTurnos(); // Actualiza la vista para reflejar la cola vacía
    }
}
