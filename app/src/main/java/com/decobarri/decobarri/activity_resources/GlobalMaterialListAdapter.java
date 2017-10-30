package com.decobarri.decobarri.activity_resources;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;

public class GlobalMaterialListAdapter extends RecyclerView.Adapter<GlobalMaterialListAdapter.MaterialViewHolder> {

    private List<MaterialListItem> materialList;

    private RecyclerView recyclerView;

    public GlobalMaterialListAdapter(ArrayList<MaterialListItem> materialList, RecyclerView recyclerView) {
        this.materialList = materialList;
        this.recyclerView = recyclerView;
    }

    // View lookup cache
    public static class MaterialViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        //TextView description;
        ImageView urgent;
        TextView quantity;
        TextView address;

        public MaterialViewHolder (View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.global_material_imageView);
            name = (TextView) view.findViewById(R.id.global_material_name);
            //viewHolder.description = (TextView) convertView.findViewById(R.id.global_material_description);
            urgent = (ImageView) view.findViewById(R.id.global_material_warningImage);
            quantity = (TextView) view.findViewById(R.id.global_material_quantity);
            address = (TextView) view.findViewById(R.id.global_material_address);
        }
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_material_view, parent, false);
        view.setOnClickListener(onClickListener);
        MaterialViewHolder material = new MaterialViewHolder(view);
        return material;
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    @Override
    public void onBindViewHolder(MaterialViewHolder viewHolder, int position) {

        /* SET GLOBAL MATERIAL IMAGE */
        viewHolder.image.setImageBitmap(materialList.get(position).get_image());

        /* SET GLOBAL MATERIAL NAME */
        viewHolder.name.setText(materialList.get(position).get_name());

        /* SET GLOBAL MATERIAL ADRESS */
        viewHolder.address.setText(materialList.get(position).get_address());

        /* SET GLOBAL MATERIAL QUANTITY */
        if (materialList.get(position).get_quantity() <= 0)
            viewHolder.quantity.setText("-");
        else
            viewHolder.quantity.setText(Integer.toString(materialList.get(position).get_quantity()));

        /* SET GLOBAL MATERIAL URGENT ICON TO VISIBLE IF IT IS NECESSARY */
        if (materialList.get(position).is_urgent())
            viewHolder.urgent.setVisibility(View.VISIBLE);
    }

    private final OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int itemPosition = recyclerView.getChildLayoutPosition(view);
            MaterialListItem item = materialList.get(itemPosition);

            CharSequence text = "Item description: " + item.get_description() + "\n"
                    + "Item urgent: " + item.is_urgent();
            Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    };
}