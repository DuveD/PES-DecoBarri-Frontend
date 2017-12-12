package com.decobarri.decobarri.activity_resources.Projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.*;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;

import java.util.List;

/**
 * Created by Marc G on 07/11/2017.
 */

public class MyProjectsAdapter extends RecyclerView.Adapter<MyProjectsAdapter.MyProjectsViewHolder> {
    private List<com.decobarri.decobarri.db_resources.Project> projectList;
    private Context context;
    private RecyclerView recyclerView;

    public MyProjectsAdapter(List<com.decobarri.decobarri.db_resources.Project> projectsList, Context mContext, RecyclerView rec) {
        this.projectList = projectsList;
        this.context = mContext;
        this.recyclerView = rec;
    }


    public static class MyProjectsViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un projectList
        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;

        public MyProjectsViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombreProyecto);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
        }
    }

    @Override
    public MyProjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        MyProjectsViewHolder project = new MyProjectsViewHolder(v);
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                com.decobarri.decobarri.db_resources.Project p = projectList.get(itemPosition);

                Intent projectMenu = new Intent(v.getContext(), ProjectMenuActivity.class);
                projectMenu.putExtra("id",p.getId()); //Pasar el id del proyecto
                context.startActivity(projectMenu);
            }

        });
        return project;
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    @Override
    public void onBindViewHolder(MyProjectsViewHolder viewHolder, int i) {
        //viewHolder.imagen.setImageBitmap(projectList.get(i).get_Imagen());
        viewHolder.nombre.setText(projectList.get(i).getName());
        viewHolder.descripcion.setText("Descripcion:" + String.valueOf(projectList.get(i).getDescription()));
    }
}
