package com.decobarri.decobarri.activity_resources.Projects;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.*;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Marc G on 24/10/2017.
 */

public class AllProjectsAdapter extends RecyclerView.Adapter<AllProjectsAdapter.AllProjectsViewHolder> implements Filterable{
    private List<com.decobarri.decobarri.db_resources.Project> projectList;
    private Context context;
    private RecyclerView recyclerView;
    private String idSolicitud;
    private String username;

    public AllProjectsAdapter(List<com.decobarri.decobarri.db_resources.Project> item, Context mContext, RecyclerView rec) {
        this.projectList = item;
        this.context = mContext;
        this.recyclerView = rec;
    }

    @Override
    public Filter getFilter() {
        return null;
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
        SharedPreferences pref = context.getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Afegir imatge i dos botons per enciar solicitud o cancelar.
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                com.decobarri.decobarri.db_resources.Project p = projectList.get(itemPosition);
                //crear popup con la info del proyecto
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_project_info);
                dialog.setTitle("Información del proyecto");
                TextView description = (TextView) dialog.findViewById(R.id.descripcion_popup);
                ImageView imagen = (ImageView) dialog.findViewById(R.id.imageView1);
                Button buttonSolicitar = (Button) dialog.findViewById((R.id.button_solicitar));
                //imagen.setImageBitmap(p.get_Imagen());
                description.setText(p.getDescription());
                idSolicitud = p.getId();
                dialog.show();
                buttonSolicitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCall();
                        dialog.cancel();
                    }
                });
            }

        });
        return project;
    }

    @Override
    public int getItemCount() {
        if (projectList != null) return projectList.size();
        else return 0;
    }

    private void requestCall(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder
                .client(httpClient.build())
                .build();
        ProjectClient client =  retrofit.create(ProjectClient.class);
        //*********************************************************************************
        //Descomentar para añadir llamada addRequest
        User user = new User(username,"");
        Call<String> call = client.addRequest(idSolicitud, user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                if (response.isSuccessful()) {
                    //
                    System.out.println("Success : " + response.body());
                }
                else {
                    System.out.println("Error code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //
                System.out.println("Call failed: " + call.request());
            }
        });
    }

    public void setFilter(List<com.decobarri.decobarri.db_resources.Project> filteredList) {
        this.projectList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AllProjectsViewHolder viewHolder, int i) {
        //viewHolder.imagen.setImageBitmap(projectList.get(i).get_Imagen());
        if (projectList.size() > 0) {
            viewHolder.nombre.setText(projectList.get(i).getName());
            viewHolder.descripcion.setText("Descripcion:" + String.valueOf(projectList.get(i).getDescription()));
        }
    }
}