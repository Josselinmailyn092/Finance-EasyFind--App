package com.example.proyecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


    public class login extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registro);
            Spinner spinner = findViewById(R.id.ocupacion);

            // Create an ArrayAdapter using the string array and a custom spinner item layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.ocupacion_array, R.layout.spinner_item);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
        }
    }
