package com.decobarri.decobarri.activity_resources.Materials;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;
import com.decobarri.decobarri.project_menu.InventoryFragment;
import com.decobarri.decobarri.project_menu.NeedListFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.decobarri.decobarri.project_menu.edit_items.EditMaterialFragment;
import com.decobarri.decobarri.project_menu.edit_items.EditNoteFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class MaterialAdapter
        extends Adapter<MaterialAdapter.MaterialViewHolder>
        implements OnClickListener, OnLongClickListener {

    private List<Material> materialList;
    private RecyclerView recyclerView;
    private Context context;
    private String parentFragment;
    private static final String TAG = "MaterialAdapter";

    private Retrofit retrofit;
    private String projectID;

    public MaterialAdapter(List<Material> materialList, RecyclerView recyclerView, Context context, String parentFragment) {
        this.materialList = materialList;
        this.recyclerView = recyclerView;
        this.context = context;
        this.parentFragment = parentFragment;
        if ( parentFragment.equals(InventoryFragment.class.getSimpleName()) ||
             parentFragment.equals(NeedListFragment.class.getSimpleName())) {

            retrofit = ((ProjectMenuActivity) context).retrofit;
            projectID = ((ProjectMenuActivity) context).projectID;
        }
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
        Picasso.with(context)
                .load(materialList.get(position).getImage())
                .resize(70, 70)
                .centerCrop()
                .into(viewHolder.image);


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
        if (parentFragment == null){
            onLongClickRead( view );
        } else {
            onLongClickOptions( view );
        }
        return true;
    }

    private void onLongClickRead(View view) {
    }

    private void onLongClickOptions(View view) {
        final int itemPosition = recyclerView.getChildLayoutPosition(view);

        String listToMove = "NULL";
        if (parentFragment.equals(InventoryFragment.class.getSimpleName())) listToMove = "Need List";
        else if (parentFragment.equals(NeedListFragment.class.getSimpleName())) listToMove = "Inventory";

        final CharSequence[] items = {"Edit",  "Move to "+listToMove, "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Select an action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item){
                    case 0:
                        onLongClickEdit(itemPosition);
                        break;
                    case 1:
                        onLongClickMove(itemPosition);
                        break;
                    case 2:
                        onLongClickDelete(itemPosition);
                        break;
                }
            }
        });
        builder.show();
    }

    private void onLongClickEdit( int itemPosition ) {
        Log.i(TAG+" "+parentFragment, "Edit Material");

        ProjectMenuActivity activity = (ProjectMenuActivity)context;
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.add(R.id.DrawerLayout, EditMaterialFragment.newInstance(getMaterial(itemPosition), TAG));
        transaction.addToBackStack(null);
        transaction.commit();

        customNotifyDataSetChanged();
    }

    private void onLongClickMove( int itemPosition ) {
        Log.i(TAG+" "+parentFragment, "Move Material");
        //TODO: CALL MOVE
    }

    private void onLongClickDelete( int itemPosition ) {
        Log.i(TAG+" "+parentFragment, "Delete Material");
        //TODO: CALL DELETE
        deleteMaterial(itemPosition);
        customNotifyDataSetChanged();
    }

    public void customNotifyDataSetChanged(){
        notifyDataSetChanged();
    }
}