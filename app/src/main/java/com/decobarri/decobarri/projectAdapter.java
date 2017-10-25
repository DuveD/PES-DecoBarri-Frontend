package com.decobarri.decobarri;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.ActivityResources.projectItem;

/**
 * Created by Marc G on 24/10/2017.
 */

public class projectAdapter extends RecyclerView.Adapter<projectAdapter.ProjectViewHolder> {
    private List<projectItem> item;

    public projectAdapter(List<projectItem> item) {
        this.item = item;
    }


    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;

        public ProjectViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombreProyecto);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
        }
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        ProjectViewHolder project = new ProjectViewHolder(v);
        return project;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageResource(item.get(i).get_idImagen());
        viewHolder.nombre.setText(item.get(i).get_name());
        viewHolder.descripcion.setText("Descripcion:" + String.valueOf(item.get(i).get_description()));
    }
}