package com.decobarri.decobarri.activity_resources;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    private List<Material> materialList;

    private RecyclerView recyclerView;

    public MaterialAdapter(ArrayList<Material> materialList, RecyclerView recyclerView) {
        this.materialList = materialList;
        this.recyclerView = recyclerView;
    }

    // View lookup cache
    public static class MaterialViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;
        //TextView description;
        ImageView urgent;
        TextView quantity;
        TextView address;

        public MaterialViewHolder (View view) {
            super(view);
            image = (CircleImageView) view.findViewById(R.id.global_material_imageView);
            name = (TextView) view.findViewById(R.id.global_material_name);
            //viewHolder.description = (TextView) convertView.findViewById(R.id.global_material_description);
            urgent = (ImageView) view.findViewById(R.id.global_material_warningImage);
            quantity = (TextView) view.findViewById(R.id.global_material_quantity);
            address = (TextView) view.findViewById(R.id.global_material_address);
        }
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_view, parent, false);
        view.setOnClickListener(onClickListener);
        MaterialViewHolder material = new MaterialViewHolder(view);
        return material;
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MaterialViewHolder viewHolder, int position) {

        /* SET GLOBAL MATERIAL IMAGE */
        viewHolder.image.setImageBitmap(materialList.get(position).getImage());

        /* SET GLOBAL MATERIAL NAME */
        viewHolder.name.setText(materialList.get(position).getName());

        /* SET GLOBAL MATERIAL ADRESS */
        viewHolder.address.setText(materialList.get(position).getAddress());

        /* SET GLOBAL MATERIAL QUANTITY */
        if (materialList.get(position).getQuantity() <= 0)
            viewHolder.quantity.setText("-");
        else
            viewHolder.quantity.setText(Integer.toString(materialList.get(position).getQuantity()));

        /* SET GLOBAL MATERIAL URGENT ICON TO VISIBLE IF IT IS NECESSARY */
        if (materialList.get(position).isUrgent()) {
            viewHolder.urgent.setVisibility(View.VISIBLE);
            viewHolder.image.setBorderColorResource(R.color.urgent_color);
        }
    }

    private final OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int itemPosition = recyclerView.getChildLayoutPosition(view);
            Material item = materialList.get(itemPosition);

            CharSequence text = "Item description: " + item.getDescription() + "\n"
                    + "Item urgent: " + item.isUrgent();
            Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    };
}