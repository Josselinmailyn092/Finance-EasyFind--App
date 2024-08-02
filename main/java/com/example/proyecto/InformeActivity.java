package com.example.proyecto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class InformeActivity extends AppCompatActivity {

    private Spinner spinnerPeriodo;
    private Button btnGenerarInforme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);

        spinnerPeriodo = findViewById(R.id.spinner_periodo);
        btnGenerarInforme = findViewById(R.id.btn_generar_informe);

        // Configurar spinner con opciones de periodo
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.periodos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodo.setAdapter(adapter);

        // Verificar permisos de almacenamiento
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
        // Lógica para obtener datos según el periodo seleccionado
        List<String> datosInforme = obtenerDatosInforme(periodo);

        try {
            // Ruta del archivo PDF
            File pdfFile = new File(Environment.getExternalStorageDirectory(), "Informe.pdf");
            FileOutputStream fos = new FileOutputStream(pdfFile);

            // Crear PDF
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Agregar contenido al PDF
            document.add(new Paragraph("Informe de " + periodo));
            for (String dato : datosInforme) {
                document.add(new Paragraph(dato));
            }

            // Cerrar documento
            document.close();

            Toast.makeText(this, "Informe generado en: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar el informe", Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> obtenerDatosInforme(String periodo) {
        // Aquí debes implementar la lógica para obtener los datos de informe según el periodo
        // Por ejemplo, consultar una API o base de datos y obtener los datos correspondientes
        return List.of("Dato 1", "Dato 2", "Dato 3"); // Datos de ejemplo
    }
}
