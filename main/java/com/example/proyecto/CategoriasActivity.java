package com.example.proyecto;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        categorias = new ArrayList<>();
        // Puedes agregar categorías iniciales si lo deseas
        categorias.add("Alimentación");
        categorias.add("Entretenimiento");

        RecyclerView recyclerView = findViewById(R.id.rv_categorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoriasAdapter(categorias);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CategoriasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Lógica para manejar el clic en una categoría
                Intent intent = new Intent(CategoriasActivity.this, CategoriaDetalleActivity.class);
                intent.putExtra("CATEGORIA_NOMBRE", categorias.get(position));
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                // Lógica para eliminar la categoría
                categorias.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, categorias.size());
            }
        });

        Button btnAddCategoria = findViewById(R.id.btn_add_categoria);
        btnAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarCategoria();
            }
        });
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
}
