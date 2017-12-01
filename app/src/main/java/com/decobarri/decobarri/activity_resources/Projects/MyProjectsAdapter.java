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
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;

import java.util.List;

/**
 * Created by Marc G on 07/11/2017.
 */

public class MyProjectsAdapter extends RecyclerView.Adapter<MyProjectsAdapter.MyProjectsViewHolder> {
    private List<Project> projectList;
    private Context context;
    private RecyclerView recyclerView;

    public MyProjectsAdapter(List<Project> item, Context mContext, RecyclerView rec) {
        this.projectList = item;
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
                Project p = projectList.get(itemPosition);

                Intent projectMenu = new Intent(v.getContext(), ProjectMenuActivity.class);
                Bundle args = new Bundle();
                args.putString("project", p.get_name());

                //TODO: Delete this line once the projects are loaded
                args.putString("project", "5a0f1cbbb42109137235a5e6");

                projectMenu.putExtras(args);
                //projectMenu.putExtra("id",p.get_name()); //Pasar el id del proyecto
                context.startActivity(projectMenu);
                /*AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Abrir menu de gestión del proyecto " + p.get_name() + "?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent create = new Intent(this.context, CreateProjectActivity.class);
                                startActivity(create);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();*/
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
        viewHolder.imagen.setImageBitmap(projectList.get(i).get_Imagen());
        viewHolder.nombre.setText(projectList.get(i).get_name());
        viewHolder.descripcion.setText("Descripcion:" + String.valueOf(projectList.get(i).get_description()));
    }
}
