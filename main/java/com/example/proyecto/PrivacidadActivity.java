package com.example.proyecto;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacidadActivity extends AppCompatActivity {

    private ImageButton btnClosePrivacidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacidad);

        btnClosePrivacidad = findViewById(R.id.btn_close_privacidad);

        btnClosePrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity
                finish();
            }
        });
    }
}
