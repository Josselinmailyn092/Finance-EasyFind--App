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
        Cursor cursorGastos = null;
        try {
            // Definir ruta y nombre del archivo PDF
            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Informe_" + periodo + ".pdf";
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Consultar la base de datos para obtener la información según el periodo
            BaseDatos db = new BaseDatos(this);
            int userId = getUserId();

            // Título del informe
            document.add(new Paragraph("Informe de Gastos " + periodo).setFontSize(18).setBold());
            document.add(new Paragraph("\n"));

            switch (periodo) {
                case "Semanal":
                    String startDate = getStartDateOfWeek();
                    String endDate = getEndDateOfWeek();
                    cursorGastos = db.getGastosPorSemana(userId, startDate, endDate);
                    document.add(new Paragraph("Período: " + startDate + " a " + endDate));
                    break;
                case "Mensual":
                    int mes = getCurrentMonth();
                    int año = getCurrentYear();
                    cursorGastos = db.getGastosPorMes(userId, mes, año);
                    document.add(new Paragraph("Mes: " + mes + "/" + año));
                    break;
                case "Anual":
                    int añoActual = getCurrentYear();
                    cursorGastos = db.getGastosPorAño(userId, añoActual);
                    document.add(new Paragraph("Año: " + añoActual));
                    break;
            }

            document.add(new Paragraph("\n"));

            // Agregar datos al PDF
            if (cursorGastos != null && cursorGastos.moveToFirst()) {
                String categoriaActual = "";
                float totalCategoria = 0;
                float totalGeneral = 0;

                do {
                    String categoria = cursorGastos.getString(cursorGastos.getColumnIndexOrThrow("nombre"));
                    double monto = cursorGastos.getDouble(cursorGastos.getColumnIndexOrThrow("monto_gastos"));
                    String fecha = cursorGastos.getString(cursorGastos.getColumnIndexOrThrow("fecha_gastos"));
                    String descripcion = cursorGastos.getString(cursorGastos.getColumnIndexOrThrow("descripcion_gastos"));

                    if (!categoria.equals(categoriaActual)) {
                        if (!categoriaActual.isEmpty()) {
                            document.add(new Paragraph("Total " + categoriaActual + ": $" + String.format("%.2f", totalCategoria)).setFontSize(12).setBold());
                            document.add(new Paragraph("\n"));
                        }
                        document.add(new Paragraph(categoria).setFontSize(14).setBold());
                        categoriaActual = categoria;
                        totalCategoria = 0;
                    }

                    document.add(new Paragraph(descripcion + ": $" + String.format("%.2f", monto) + " - " + fecha));
                    totalCategoria += monto;
                    totalGeneral += monto;

                } while (cursorGastos.moveToNext());

                // Añadir el total de la última categoría
                document.add(new Paragraph("Total " + categoriaActual + ": $" + String.format("%.2f", totalCategoria)).setFontSize(12).setBold());
                document.add(new Paragraph("\n"));

                // Añadir el total general
                document.add(new Paragraph("Total General: $" + String.format("%.2f", totalGeneral)).setFontSize(14).setBold());
            } else {
                document.add(new Paragraph("No se encontraron gastos para el período seleccionado."));
            }

            document.close();
            Toast.makeText(this, "Informe generado en: " + pdfPath, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar informe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursorGastos != null) {
                cursorGastos.close();
            }
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