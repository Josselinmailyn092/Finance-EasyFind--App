package com.example.proyecto;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InformeActivity extends AppCompatActivity {

   private Spinner spinnerPeriodo;
    private Button btnGenerarInforme;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ImageButton btnCerrarInforme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        btnCerrarInforme = findViewById(R.id.btnCerrarPriv);
        btnCerrarInforme.setOnClickListener(v -> finish());
        spinnerPeriodo = findViewById(R.id.spinner_periodo);
        btnGenerarInforme = findViewById(R.id.btn_generar_informe);

        // Configurar spinner con opciones de periodo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.periodos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodo.setAdapter(adapter);

        // Verificar permisos de almacenamiento
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
           // generarInforme();
        }

        btnGenerarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String periodo = spinnerPeriodo.getSelectedItem().toString();
                generarInforme(periodo);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generarInforme(String periodo) {
        try {
            // Definir ruta y nombre del archivo PDF
            String pdfPath = Environment.getExternalStorageDirectory() + "/Informe.pdf";
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Consultar la base de datos para obtener la información según el periodo
            BaseDatos db = new BaseDatos(this);
            int userId = Integer.parseInt(String.valueOf(getUserId())); // Obtener el ID del usuario actual
            Cursor cursorIngresos = null;
            Cursor cursorGastos = null;
/*
            switch (periodo) {
                case "Mensual":
                    int mes = getCurrentMonth();
                    int año = getCurrentYear();
                    cursorIngresos = db.getIngresosPorMes(userId, mes, año);
                    cursorGastos = db.getGastosPorMes(userId, mes, año);
                    break;
                case "Semanal":
                    String startDate = getStartDateOfWeek();
                    String endDate = getEndDateOfWeek();
                    cursorIngresos = db.getIngresosPorSemana(userId, startDate, endDate);
                    cursorGastos = db.getGastosPorSemana(userId, startDate, endDate);
                    break;
                case "Anual":
                    int añoActual = getCurrentYear();
                    cursorIngresos = db.getIngresosPorAño(userId, añoActual);
                    cursorGastos = db.getGastosPorAño(userId, añoActual);
                    break;
            }*/

            // Agregar datos al PDF
            if (cursorIngresos != null) {
                document.add(new Paragraph("Ingresos:"));
                int descripcionIndex = cursorIngresos.getColumnIndex("descripcion");
                int montoIndex = cursorIngresos.getColumnIndex("monto");
                int fechaIndex = cursorIngresos.getColumnIndex("fecha");

                if (descripcionIndex >= 0 && montoIndex >= 0 && fechaIndex >= 0) {
                    while (cursorIngresos.moveToNext()) {
                        String descripcion = cursorIngresos.getString(descripcionIndex);
                        double monto = cursorIngresos.getDouble(montoIndex);
                        String fecha = cursorIngresos.getString(fechaIndex);
                        document.add(new Paragraph(descripcion + ": $" + monto + " - " + fecha));
                    }
                }
                cursorIngresos.close();
            }

            if (cursorGastos != null) {
                document.add(new Paragraph("Gastos:"));
                int descripcionGastosIndex = cursorGastos.getColumnIndex("descripcion");
                int montoGastosIndex = cursorGastos.getColumnIndex("monto");
                int fechaGastosIndex = cursorGastos.getColumnIndex("fecha");

                if (descripcionGastosIndex >= 0 && montoGastosIndex >= 0 && fechaGastosIndex >= 0) {
                    while (cursorGastos.moveToNext()) {
                        String descripcion = cursorGastos.getString(descripcionGastosIndex);
                        double monto = cursorGastos.getDouble(montoGastosIndex);
                        String fecha = cursorGastos.getString(fechaGastosIndex);
                        document.add(new Paragraph(descripcion + ": $" + monto + " - " + fecha));
                    }
                }
                cursorGastos.close();
            }

            document.close();
            Toast.makeText(this, "Informe generado en: " + pdfPath, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar informe", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    private int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    private String getStartDateOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private String getEndDateOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario", null);  // Obtén el email almacenado en las preferencias compartidas
        if (usuario != null) {
            BaseDatos db = new BaseDatos(this);
           // return db.getUserIdByusuario(usuario);  // Usa el método para obtener el ID del usuario por su email
        }
        return -1;  // Retorna -1 si no se encuentra el email
    }

}