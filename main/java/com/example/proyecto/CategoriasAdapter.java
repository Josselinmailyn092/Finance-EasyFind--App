package com.example.proyecto;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaViewHolder> {

    private List<String> categorias;
    private OnItemClickListener listener;

    public CategoriasAdapter(List<String> categorias) {
        this.categorias = categorias;
    }

    // Interface para manejar los eventos de clic
    public interface OnItemClickListener {
        void onItemClick(int posicion);
        void onDeleteClick(int posicion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int posicion) {
        String categoria = categorias.get(posicion);
        holder.textoCategoria.setText(categoria);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {

        TextView textoCategoria;
        Button btnEliminar;

        public CategoriaViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            textoCategoria = itemView.findViewById(R.id.tv_categoria);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);

            // Configura el evento de clic para el ítem completo
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION) {
                        listener.onItemClick(posicion);
                    }
                }
            });

            // Configura el evento de clic para el botón eliminar
            btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    int posicion = getAdapterPosition();
                    if (posicion != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(posicion);
                    }
                }
            });
        }
    }
}
