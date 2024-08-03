package com.example.proyecto;

import android.content.Intent;
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
    private ImageButton btnCerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        btnCerrar= findViewById(R.id.btnCerrarSaldo);
        btnCerrar.setOnClickListener(v -> finish());
        saldo = findViewById(R.id.saldo_total_value);
        mesActual = findViewById(R.id.mesAc);
        ultIng = findViewById(R.id.ultimosIngresos);
        i_valor=findViewById(R.id.ingresos_value);
        g_valor=findViewById(R.id.gastos_value);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        mostrarSaldo();
        mostrarGastos();

        Calendar calendario = Calendar.getInstance();
        int mes = calendario.get(Calendar.MONTH);
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        String nombreMes = nombresMeses[mes];
        mesActual.setText(nombreMes);
    }

    public void mostrarSaldo() {
        BaseDatos bd = new BaseDatos(this);
        double saldoTotal = bd.getSaldoTotalUsuario(userId);
        saldo.setText(String.format("$%.2f", saldoTotal));

        double iTotal= bd.getIngresoMes(userId);
        double gTotal= bd.getGastoMes(userId);
        i_valor.setText(String.format("$%.2f", iTotal));
        g_valor.setText(String.format("$%.2f", gTotal));
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