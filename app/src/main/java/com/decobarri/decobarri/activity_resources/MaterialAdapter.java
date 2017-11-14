package com.decobarri.decobarri.activity_resources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.project_menu.edit_items.EditMaterialActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MaterialAdapter
        extends Adapter<MaterialAdapter.MaterialViewHolder>
        implements OnClickListener, OnLongClickListener {

    private List<Material> materialList;
    private RecyclerView recyclerView;
    Context context;

    private String LIST;

    public MaterialAdapter(ArrayList<Material> materialList, RecyclerView recyclerView, Context context, String LIST) {
        this.materialList = materialList;
        this.recyclerView = recyclerView;
        this.context = context;
        this.LIST = LIST;
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

    public boolean deleteMaterial (Material material) {
        return materialList.remove(material);
    }

    public Material deleteMaterial (int index) {
        return materialList.remove(index);
    }

    public Material getMaterial (int index) {
        return materialList.get(index);
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_view, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
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


    public void onClick(View view) {

        int itemPosition = recyclerView.getChildLayoutPosition(view);
        Material item = materialList.get(itemPosition);

        CharSequence text = "Item description: " + item.getDescription() + "\n"
                + "Item urgent: " + item.isUrgent();
        Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onLongClick(View view) {
        if (LIST.equals(null)){
            onLongClickRead( view );
        } else {
            onLongClickEdit( view );
        }
        return true;
    }

    private void onLongClickRead(View view) {
    }

    private void onLongClickEdit(View view) {
        final int itemPosition = recyclerView.getChildLayoutPosition(view);

        final CharSequence[] items = {"Delete", "Edit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select an action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item){
                    case 0:
                        doOnDelete(itemPosition);
                        break;
                    case 1:
                        doOnEdit(itemPosition);
                        break;
                }
            }
        });
        builder.show();
    }

    private void doOnDelete( int itemPosition ) {
        deleteMaterial(itemPosition);
        customNotifyDataSetChanged();
    }

    private void doOnEdit( int itemPosition ) {
        Intent intent = new Intent(context, EditMaterialActivity.class);
        intent.putExtra(Const.FROM, LIST);
        intent.putExtra(Const.EDIT_MATERIAL, true);
        intent.putExtra(Const.ID, getMaterial( itemPosition ).getID());
        context.startActivity(intent);

        customNotifyDataSetChanged();
    }

    public void customNotifyDataSetChanged(){
        notifyDataSetChanged();
    }
}