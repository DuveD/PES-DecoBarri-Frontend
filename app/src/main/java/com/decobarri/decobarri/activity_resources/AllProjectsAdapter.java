package com.decobarri.decobarri.activity_resources;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.main_menu.MainMenuActivity;
import com.decobarri.decobarri.main_menu.ProjectsSearchFragment;

import java.util.List;

/**
 * Created by Marc G on 24/10/2017.
 */

public class AllProjectsAdapter extends RecyclerView.Adapter<AllProjectsAdapter.AllProjectsViewHolder> {
    private List<Project> projectList;
    private Context context;
    private RecyclerView recyclerView;

    public AllProjectsAdapter(List<Project> item, Context mContext, RecyclerView rec) {
        this.projectList = item;
        this.context = mContext;
        this.recyclerView = rec;
    }


    public static class AllProjectsViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un projectList
        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;

        public AllProjectsViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombreProyecto);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
        }
    }

    @Override
    public AllProjectsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        AllProjectsViewHolder project = new AllProjectsViewHolder(v);
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                Project p = projectList.get(itemPosition);
                //crear popup con la info del proyecto
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_project_info);
                dialog.setTitle("Title...");
                dialog.show();
                /*AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Quieres enviar una solicitud para participar en el proyecto " + p.get_name() + "?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CharSequence text = "Solicitud enviada";
                                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                                toast.show();
                                dialog.cancel();
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
    public void onBindViewHolder(AllProjectsViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageBitmap(projectList.get(i).get_Imagen());
        viewHolder.nombre.setText(projectList.get(i).get_name());
        viewHolder.descripcion.setText("Descripcion:" + String.valueOf(projectList.get(i).get_description()));
    }
}