package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicioActivity extends AppCompatActivity {
    private TextView nombreUsuario, contenidoGasto1, contenidoGasto2, contenidoGasto3;
    private Button anadirCategoria, categoria1, categoria2, categoria3, verMas, generarInforme, anadirDinero, registrarGasto, masDetalles;
    private ImageView imagenGasto1, imagenGasto2, imagenGasto3;
    private LinearLayout contenedorRegistros;
    private int userId;
    private BaseDatos db;
    private List<Map<String, String>> categoriasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        nombreUsuario = findViewById(R.id.nombreUsuario);
        anadirCategoria = findViewById(R.id.addCategoria);
        categoria1 = findViewById(R.id.categoria1);
        categoria2 = findViewById(R.id.categoria2);
        categoria3 = findViewById(R.id.categoria3);
        verMas = findViewById(R.id.verMas);
        generarInforme = findViewById(R.id.boton_generar_informe);
        anadirDinero = findViewById(R.id.boton_anadir_dinero);
        registrarGasto = findViewById(R.id.boton_registrar_gasto);
        imagenGasto1 = findViewById(R.id.imagenGasto1);
        imagenGasto2 = findViewById(R.id.imagenGasto2);
        imagenGasto3 = findViewById(R.id.imagenGasto3);
        contenidoGasto1 = findViewById(R.id.contenidoGasto1);
        contenidoGasto2 = findViewById(R.id.contenidoGasto2);
        contenidoGasto3 = findViewById(R.id.contenidoGasto3);
        contenedorRegistros = findViewById(R.id.contenedorRegistros);
        masDetalles = findViewById(R.id.verDetalles);

        // Rescate de variables de entorno (BD, IdUser)
        db = new BaseDatos(this);
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        // Invocar métodos para generar datos
        cargarDatosUsuario(userId);
        cargarCategorias(userId);
        cargarGastos(userId);
        cargarResumen(userId);

        // Click en añadir categoría
        anadirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, AddCategoriaActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Click en ver más categorías
        verMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, CategoriasActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Click en generar Informe
        generarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, InformeActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Click en añadir Ingreso
        anadirDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, AddIngresoActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Click en registrar Gasto
        registrarGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, AddGastoActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Click en ver más
        masDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, InformeActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });

        // Configura la barra de navegación
        ClipsBar.setupToolbar(findViewById(R.id.toolbar4), this, userId);
    }

    private void cargarDatosUsuario(int userId) {
        Cursor cursor = null;
        try {
            cursor = db.getUsuarioById(userId);
            if (cursor != null && cursor.moveToFirst()) {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido"));

                nombreUsuario.setText(String.format("%s %s", nombre, apellido));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void cargarCategorias(int userId) {
        Cursor cursor = null;
        try {
            cursor = db.getCategoriasPorUsuario(userId);
            categoriasList = new ArrayList<>();
            if (cursor != null) {
                int index = 0;
                while (cursor.moveToNext() && index < 3) {
                    String nombreCategoria = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                    int idCategoria = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    Map<String, String> categoriaMap = new HashMap<>();
                    categoriaMap.put("nombre", nombreCategoria);
                    categoriaMap.put("id", String.valueOf(idCategoria));
                    categoriasList.add(categoriaMap);
                    // Actualizar los botones según el índice
                    switch (index) {
                        case 0:
                            categoria1.setText(nombreCategoria);
                            categoria1.setOnClickListener(new CategoriaClickListener(idCategoria));
                            break;
                        case 1:
                            categoria2.setText(nombreCategoria);
                            categoria2.setOnClickListener(new CategoriaClickListener(idCategoria));
                            break;
                        case 2:
                            categoria3.setText(nombreCategoria);
                            categoria3.setOnClickListener(new CategoriaClickListener(idCategoria));
                            break;
                    }
                    index++;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void cargarGastos(int userId) {
        Cursor cursor = null;
        try {
            cursor = db.getGastosPorUsuario(userId);
            if (cursor != null) {
                int index = 0;
                while (cursor.moveToNext() && index < 3) {
                    int categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow("id_categoria"));
                    String categoria = obtenerNombreCategoria(categoriaId);
                    double monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto_gastos"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_gastos"));

                    String contenidoGasto = categoria + " - " + monto + "$ - " + fecha;
                    int imageResource = getResources().getIdentifier(categoria.toLowerCase(), "drawable", getPackageName());

                    if (imageResource == 0) {
                        imageResource = R.drawable.ic_default;  // Imagen por defecto
                    }
                    switch (index) {
                        case 0:
                            contenidoGasto1.setText(contenidoGasto);
                            imagenGasto1.setImageResource(imageResource);
                            break;
                        case 1:
                            contenidoGasto2.setText(contenidoGasto);
                            imagenGasto2.setImageResource(imageResource);
                            break;
                        case 2:
                            contenidoGasto3.setText(contenidoGasto);
                            imagenGasto3.setImageResource(imageResource);
                            break;
                    }
                    index++;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String obtenerNombreCategoria(int idCategoria) {
        Cursor cursor = null;
        try {
            cursor = db.getCategoria(idCategoria);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "Desconocida";
    }

    private void cargarResumen(int userId) {
        List<Map<String, Object>> registros = new ArrayList<>();
        // Obtener gastos del usuario
        Cursor cursorGastos = db.getGastosPorUsuario(userId);
        if (cursorGastos != null) {
            try {
                while (cursorGastos.moveToNext()) {
                    double monto = cursorGastos.getDouble(cursorGastos.getColumnIndexOrThrow("monto_gastos"));
                    String fecha = cursorGastos.getString(cursorGastos.getColumnIndexOrThrow("fecha_gastos"));

                    Map<String, Object> registro = new HashMap<>();
                    registro.put("tipo", "Gasto");
                    registro.put("monto", monto);
                    registro.put("fecha", fecha);

                    registros.add(registro);
                }
            } finally {
                cursorGastos.close();
            }
        }
        // Obtener ingresos del usuario
        Cursor cursorIngresos = db.getIngresosPorUsuario(userId);
        if (cursorIngresos != null) {
            try {
                while (cursorIngresos.moveToNext()) {
                    double monto = cursorIngresos.getDouble(cursorIngresos.getColumnIndexOrThrow("monto"));
                    String fecha = cursorIngresos.getString(cursorIngresos.getColumnIndexOrThrow("fecha"));

                    Map<String, Object> registro = new HashMap<>();
                    registro.put("tipo", "Ingreso");
                    registro.put("monto", monto);
                    registro.put("fecha", fecha);

                    registros.add(registro);
                }
            } finally {
                cursorIngresos.close();
            }
        }

        // Ordenar registros por fecha descendente
        Collections.sort(registros, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> r1, Map<String, Object> r2) {
                return ((String) r2.get("fecha")).compareTo((String) r1.get("fecha")); // Suponiendo que fecha está en formato "YYYY-MM-DD"
            }
        });

        // Obtener el monto total del usuario
        double montoTotalUsuario = obtenerMontoTotalUsuario(userId);

        // Limpiar el contenedor
        contenedorRegistros.removeAllViews();

        // Inflar y añadir los 5 registros más recientes
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < Math.min(5, registros.size()); i++) {
            Map<String, Object> registro = registros.get(i);
            View registroView = inflater.inflate(R.layout.registro_layout, contenedorRegistros, false);

            TextView tipoRegistro = registroView.findViewById(R.id.tipoRegistro);
            TextView monto = registroView.findViewById(R.id.monto);
            TextView porcentaje = registroView.findViewById(R.id.porcentaje);
            tipoRegistro.setText((String) registro.get("tipo"));
            // Formatear el monto con signo de negativo si es un gasto
            if ("Gasto".equals(registro.get("tipo"))) {
                monto.setText(String.format("-$%.2f", (Double) registro.get("monto")));
            } else {
                monto.setText(String.format("+$%.2f", (Double) registro.get("monto")));
            }

            if (montoTotalUsuario > 0) {
                porcentaje.setText(String.format("%.2f%%", ((Double) registro.get("monto") / montoTotalUsuario) * 100));
            } else {
                porcentaje.setText("0%");
            }

            contenedorRegistros.addView(registroView);
        }
    }

    private double obtenerMontoTotalUsuario(int userId) {
        double montoTotal = 0;
        Cursor cursor = db.getUsuarioById(userId);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    montoTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("saldo_total"));
                }
            } finally {
                cursor.close();
            }
        }
        return montoTotal;
    }

    private class CategoriaClickListener implements View.OnClickListener {
        private int idCategoria;

        public CategoriaClickListener(int idCategoria) {
            this.idCategoria = idCategoria;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(InicioActivity.this, CategoriaDetalleActivity.class);
            intent.putExtra("CATEGORIA_ID", idCategoria);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        }
    }
}
