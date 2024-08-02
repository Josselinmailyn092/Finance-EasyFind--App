package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoriasActivity extends AppCompatActivity {

    private List<String> categorias;
    private CategoriasAdapter adapter;
    private BaseDatos baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        baseDatos = new BaseDatos(this); // Inicializar base de datos
        categorias = new ArrayList<>();
        cargarCategoriasDesdeBaseDatos(); // Cargar categorías de la base de datos

        RecyclerView recyclerView = findViewById(R.id.rv_categorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoriasAdapter(categorias);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CategoriasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CategoriasActivity.this, CategoriaDetalleActivity.class);
                intent.putExtra("CATEGORIA_NOMBRE", categorias.get(position));
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                eliminarCategoria(position);
            }
        });

        Button btnAddCategoria = findViewById(R.id.btnn_agregar_categoria);
        btnAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarCategoria();
            }
        });
    }

    private void cargarCategoriasDesdeBaseDatos() {
        Cursor cursor = baseDatos.getAllCategorias();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombreCategoria = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                categorias.add(nombreCategoria);
            } while (cursor.moveToNext());
            cursor.close(); // Asegúrate de cerrar el cursor
        }
    }

    private void mostrarDialogoAgregarCategoria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Categoría");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String nuevaCategoria = input.getText().toString().trim();
            if (!nuevaCategoria.isEmpty()) {
                categorias.add(nuevaCategoria);
                adapter.notifyItemInserted(categorias.size() - 1);


            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Método para obtener el ID del usuario (debes implementarlo)



    private void eliminarCategoria(int position) {
        String categoria = categorias.get(position);
        categorias.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, categorias.size());

        Cursor cursor = baseDatos.getCategoriaNombre(categoria);
        if (cursor != null && cursor.moveToFirst()) {
            int idCategoria = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            baseDatos.deleteCategoria(idCategoria);
            cursor.close(); // Asegúrate de cerrar el cursor
        }
    }
}
