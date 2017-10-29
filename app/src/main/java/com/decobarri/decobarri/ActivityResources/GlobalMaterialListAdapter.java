package com.decobarri.decobarri.ActivityResources;

import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.R;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class GlobalMaterialListAdapter extends BaseAdapter {

    private ArrayList<?> entradas;
    private int R_layout_IdView;
    private Context context;

    public GlobalMaterialListAdapter(Context context, int R_layout_IdView, ArrayList<?> entradas) {
        this.context = context;
        this.entradas = entradas;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        onItem (entradas.get(posicion), view);
        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Object getItem(int posicion) {
        return entradas.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public abstract void onItem (Object item, View view);

    public void remove(int position) {
        entradas.remove(position);
    }
/*
    public static class GlovalMaterialViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public CircleImageView image;
        public TextView name;
        public TextView adress;
        public TextView quantity;
        public int warning;

        public GlovalMaterialViewHolder(View v) {
            super(v);
            image = (CircleImageView) v.findViewById(R.id.global_material_imageView);
            name = (TextView) v.findViewById(R.id.global_material_name);
            adress = (TextView) v.findViewById(R.id.global_material_address);
            quantity = (TextView) v.findViewById(R.id.global_material_quantity);
            warning = v.findViewById(R.id.global_material_warningImage).getVisibility();

        }
    }

    @Override
    public GlovalMaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_material_view, parent, false);
        GlovalMaterialViewHolder project = new GlovalMaterialViewHolder(v);
        return project;
    }
    */

}