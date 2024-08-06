package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InformeActivity extends AppCompatActivity {

    private static final String TAG = "InformeActivity";
    private Spinner spinnerPeriodo;
    private Button btnGenerarInforme;
    private BaseDatos db;
    private int userId;
    // Lista para traer las categorías del usuario
    private List<String> categorias;
    // Lista para traer los gastos del usuario
    private List<Double> gastos;
    // Lista para traer las descripciones de los gastos del usuario
    private List<String> descripciones;
    // Lista para traer las fechas de los gastos del usuario
    private List<String> fechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);

        // Configuración de los botones del toolbar
        ClipsBar.setupToolbar(findViewById(R.id.toolbar4), InformeActivity.this, userId);

        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        SharedPreferences sharedPref = getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("id_user", 0);

        spinnerPeriodo = findViewById(R.id.spinner_periodo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.periodos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodo.setAdapter(adapter);

        db = new BaseDatos(this);
        btnGenerarInforme = findViewById(R.id.btn_generar_informe);

        btnGenerarInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarInforme();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onRestart();
    }

    private void generarInforme() {
        try {
            String periodoSeleccionado = spinnerPeriodo.getSelectedItem().toString();
            File pdfFile = crearPDF(periodoSeleccionado);

            if (pdfFile != null && pdfFile.exists()) {
                Uri pdfUri = FileProvider.getUriForFile(this, "com.example.proyecto.fileprovider", pdfFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(pdfUri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error al generar el informe: archivo no creado", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al generar informe: ", e);
            Toast.makeText(this, "Error al generar el informe: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private File crearPDF(String periodo) {
        File pdfFile = null;
        try {
            String fileName = "Informe_" + periodo + "_" + new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().getTime()) + ".pdf";
            File dir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "informes");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            pdfFile = new File(dir, fileName);

            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.setFont(font);

            document.add(new Paragraph("Informe de Gastos - " + periodo).setFontSize(18));
            document.add(new Paragraph("\n"));

            Table table = new Table(4);
            table.addCell("Categoría");
            table.addCell("Monto");
            table.addCell("Fecha");
            table.addCell("Descripción");

            Cursor cursor = getCursorForPeriod(userId, periodo);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    table.addCell(cursor.getString(cursor.getColumnIndexOrThrow("categoria")));
                    table.addCell(String.format("%.2f", cursor.getDouble(cursor.getColumnIndexOrThrow("monto_gastos"))));
                    table.addCell(cursor.getString(cursor.getColumnIndexOrThrow("fecha_gastos")));
                    table.addCell(cursor.getString(cursor.getColumnIndexOrThrow("descripcion_gastos")));
                } while (cursor.moveToNext());
                cursor.close();
            } else {
                document.add(new Paragraph("No se encontraron datos para el período seleccionado."));
            }

            document.add(table);
            document.close();

            Log.d(TAG, "PDF creado exitosamente: " + pdfFile.getAbsolutePath());

        } catch (Exception e) {
            Log.e(TAG, "Error al crear el PDF", e);
            Toast.makeText(this, "Error al crear el PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }

        return pdfFile;
    }

    private Cursor getCursorForPeriod(int userId, String periodo) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"categoria", "monto_gastos", "fecha_gastos", "descripcion_gastos"});

        // Datos de ejemplo (en una aplicación real, estos vendrían de la base de datos)
        List<String> dates = new ArrayList<>(Arrays.asList(db.getExpenseDates(userId)));
        List<String> amounts = new ArrayList<>();
        for (double amount : db.getExpenseAmounts(userId)) {
            amounts.add(String.valueOf(amount));
        }
        List<String> descriptions = new ArrayList<>(Arrays.asList(db.getExpenseDescriptions(userId)));
        List<String> categories = new ArrayList<>(Arrays.asList(db.getCategoryNames(userId)));

        // Registrar los tamaños de las listas
        Log.d(TAG, "Tamaños - Fechas: " + dates.size() + ", Montos: " + amounts.size() + ", Descripciones: " + descriptions.size() + ", Categorías: " + categories.size());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        Date startDate;
        switch (periodo) {
            case "Semana":
                cal.add(Calendar.DAY_OF_YEAR, -7);
                startDate = cal.getTime();
                break;
            case "Mes":
                cal.add(Calendar.MONTH, -1);
                startDate = cal.getTime();
                break;
            case "Año":
                cal.add(Calendar.YEAR, -1);
                startDate = cal.getTime();
                break;
            default:
                startDate = currentDate;
                break;
        }

        for (int i = 0; i < dates.size(); i++) {
            try {
                Date entryDate = sdf.parse(dates.get(i));

                if (entryDate != null && !entryDate.before(startDate) && !entryDate.after(currentDate)) {
                    if (i < categories.size() && i < amounts.size() && i < descriptions.size()) {
                        cursor.addRow(new Object[]{categories.get(i), amounts.get(i), dates.get(i), descriptions.get(i)});
                    } else {
                        Log.e(TAG, "Índice fuera de límites: " + i);
                    }
                }
            } catch (ParseException | java.text.ParseException e) {
                Log.e(TAG, "Error al analizar la fecha: " + dates.get(i), e);
            }
        }

        return cursor;
    }
}
