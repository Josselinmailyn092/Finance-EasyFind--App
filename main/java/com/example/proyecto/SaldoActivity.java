package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class SaldoActivity extends AppCompatActivity {

    public TextView saldo;
    private int userId;
    public TextView mesActual;
    public TextView ultIng;
    public TextView i_valor;
    public TextView g_valor;
    public BaseDatos bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        ClipsBar.setupToolbar(findViewById(R.id.toolbar4), SaldoActivity.this, userId);

        saldo = findViewById(R.id.saldo_total_value);
        mesActual = findViewById(R.id.mesAc);
        ultIng = findViewById(R.id.ultimosIngresos);
        i_valor=findViewById(R.id.ingresos_value);
        g_valor=findViewById(R.id.gastos_value);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        SharedPreferences sharedPref = getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("id_user", 0);

        mostrarSaldo();
        mostrarGastos();

        Calendar calendario = Calendar.getInstance();
        int mes = calendario.get(Calendar.MONTH);
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String nombreMes = nombresMeses[mes];
        mesActual.setText(nombreMes);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mostrarSaldo();
        mostrarGastos();
    }
    public void mostrarSaldo() {
        bd = new BaseDatos(this);


        double iTotal= bd.getIngresoMes(userId);
        double gTotal= bd.getGastoMes(userId);
        i_valor.setText(String.format("$%.2f", iTotal));
        g_valor.setText(String.format("$%.2f", gTotal));
        saldo.setText(String.format("$%.2f", iTotal-gTotal));
    }

    public void mostrarGastos() {
        BaseDatos bd = new BaseDatos(this);
        Cursor cursor = bd.getGastosPorUsuario(userId);

        StringBuilder sb = new StringBuilder();

        if (cursor != null && cursor.moveToFirst()) {
            int descriptionIndex = cursor.getColumnIndex("descripcion_gastos");
            int montoIndex = cursor.getColumnIndex("monto_gastos");
            int fechaIndex = cursor.getColumnIndex("fecha_gastos");

            do {
                String descripcion = cursor.getString(descriptionIndex);
                double monto = cursor.getDouble(montoIndex);
                String fecha = cursor.getString(fechaIndex);

                sb.append(descripcion).append(": $").append(String.format("%.2f", monto))
                        .append(" - ").append(fecha).append("\n");
            } while (cursor.moveToNext());

            cursor.close();
        }

        if (sb.length() > 0) {
            ultIng.setText(sb.toString().trim());
        } else {
            ultIng.setText("No hay gastos recientes");
        }
    }
}