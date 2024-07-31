package com.example.sistemadeespera;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ClipsBar implements View.OnClickListener {
    private Context context;

    public ClipsBar(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonInicio:
                intent = new Intent(context, InicioActivity.class);
                break;
            case R.id.buttonSaldo:
                intent = new Intent(context, SaldoActivity.class);
                break;
            case R.id.buttonInforme:
                intent = new Intent(context, InformesActivity.class);
                break;
            case R.id.buttonConfiguracion:
                intent = new Intent(context, ConfiguracionActivity.class);
                break;
            default:
                return;
        }
        context.startActivity(intent);
    }

    public static void setupToolbar(View root, Context context) {
        ClipsBar handler = new ClipsBar(context);
        Button buttonInicio = root.findViewById(R.id.buttonInicio);
        Button buttonSaldo = root.findViewById(R.id.buttonSaldo);
        Button buttonInforme = root.findViewById(R.id.buttonInforme);
        Button buttonConfiguracion = root.findViewById(R.id.buttonConfiguracion);

        buttonInicio.setOnClickListener(handler);
        buttonSaldo.setOnClickListener(handler);
        buttonInforme.setOnClickListener(handler);
        buttonConfiguracion.setOnClickListener(handler);
    }
}
