package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InformeActivity extends AppCompatActivity {

    private static final String TAG = "InformeActivity";
    private Spinner spinnerPeriodo;
    private Button btnGenerarInforme;
    private ImageButton btnCerrar;
    private BaseDatos db;
    private int userId = 1; // Asume que tienes un userId, ajusta según sea necesario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);

        try {
            db = new BaseDatos(this);
            spinnerPeriodo = findViewById(R.id.spinner_periodo);
            btnGenerarInforme = findViewById(R.id.btn_generar_informe);
            btnCerrar = findViewById(R.id.btnCerrarPriv);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.periodos_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPeriodo.setAdapter(adapter);

            btnGenerarInforme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generarInforme();
                }
            });

            btnCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error en onCreate: ", e);
            Toast.makeText(this, "Error al inicializar la actividad", Toast.LENGTH_LONG).show();
        }
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
        List<String> dates = Arrays.asList("2024-08-01", "2024-08-02", "2024-08-04");
        List<String> amounts = Arrays.asList("10.00", "20.00", "30.00"); // Example amounts
        List<String> descriptions = Arrays.asList("Compra A", "Compra B", "Compra C"); // Example descriptions
        List<String> categories = Arrays.asList("Categoría A", "Categoría B", "Categoría C"); // Example categories

        for (int i = 0; i < dates.size(); i++) {
            cursor.addRow(new Object[]{categories.get(i), amounts.get(i), dates.get(i), descriptions.get(i)});
        }

        return cursor;
    }
}
