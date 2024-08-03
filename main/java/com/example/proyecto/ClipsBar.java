package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ClipsBar implements View.OnClickListener {
    private Context context;
    private int userId;

    public ClipsBar(Context context, int userId) {
        this.context = context;
        this.userId=userId;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String tag = (String) v.getTag();
        if (tag != null) {
            switch (tag) {
                case "inicio":
                    // Evitar navegar si ya estamos en InicioActivity
                    if (!(context instanceof InicioActivity)) {
                        intent = new Intent(context, InicioActivity.class);
                    }
                    break;
                case "saldo":
                    // Evitar navegar si ya estamos en SaldoActivity

                    if (!(context instanceof SaldoActivity)) {
                        intent = new Intent(context, SaldoActivity.class);
                    }

                    break;
                case "informe":
                    // Evitar navegar si ya estamos en InformeActivity
                    if (!(context instanceof InformeActivity)) {
                        intent = new Intent(context, InformeActivity.class);
                    }
                    break;
                case "configuracion":
                    // Evitar navegar si ya estamos en ConfiguracionActivity
                    if (!(context instanceof ConfiguracionActivity)) {
                        intent = new Intent(context, ConfiguracionActivity.class);
                    }
                    break;
                default:
                    return;
            }
        }
        if (intent != null) {
            intent.putExtra("USER_ID",userId);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Traer la actividad al frente si ya existe
            context.startActivity(intent);
        }
    }

    public static void setupToolbar(View root, Context context, int userId) {
        if (context == null || root == null) {
            throw new IllegalArgumentException("Context or root view cannot be null");
        }

        ClipsBar handler = new ClipsBar(context, userId);

        // Encontrar los botones y asignar tags
        Button buttonInicio = root.findViewById(R.id.buttonInicio);
        Button buttonSaldo = root.findViewById(R.id.buttonSaldo);
        Button buttonInforme = root.findViewById(R.id.buttonInforme);
        Button buttonConfiguracion = root.findViewById(R.id.buttonConfiguracion);

        // Asignar tags a los botones
        buttonInicio.setTag("inicio");
        buttonSaldo.setTag("saldo");
        buttonInforme.setTag("informe");
        buttonConfiguracion.setTag("configuracion");

        // Configurar los OnClickListeners
        buttonInicio.setOnClickListener(handler);
        buttonSaldo.setOnClickListener(handler);
        buttonInforme.setOnClickListener(handler);
        buttonConfiguracion.setOnClickListener(handler);
    }
}
