package com.decobarri.decobarri.activity_resources;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.R;

import java.util.List;

/**
 * Created by Marc G on 24/10/2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private List<Project> item;

    public ProjectAdapter(List<Project> item) {
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
        viewHolder.imagen.setImageBitmap(item.get(i).get_Imagen());
        viewHolder.nombre.setText(item.get(i).get_name());
        viewHolder.descripcion.setText("Descripcion:" + String.valueOf(item.get(i).get_description()));
    }
}