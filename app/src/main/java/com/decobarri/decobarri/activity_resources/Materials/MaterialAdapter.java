package com.decobarri.decobarri.activity_resources.Materials;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Base64;
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
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.main_menu.GlobalMaterialsFragment;
import com.decobarri.decobarri.project_menu.InventoryFragment;
import com.decobarri.decobarri.project_menu.NeedListFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.decobarri.decobarri.project_menu.edit_items.EditMaterialFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MaterialAdapter
        extends Adapter<MaterialAdapter.MaterialViewHolder>
        implements OnClickListener {

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

        /* SET MATERIAL IMAGE */
        Material material = materialList.get(position);

        /* SET MATERIAL IMAGE */
        viewHolder.image.setImageBitmap(decodeFromBase64(material.getImage()));

        /* SET MATERIAL NAME */
        viewHolder.name.setText(material.getName());

        /* SET MATERIAL ADRESS */
        viewHolder.address.setText(material.getAddress());

        /* SET MATERIAL QUANTITY */
        if (material.getQuantity() <= 0)
            viewHolder.quantity.setText("-");
        else
            viewHolder.quantity.setText(Integer.toString(material.getQuantity()));

        /* SET MATERIAL URGENT ICON TO VISIBLE IF IT IS NECESSARY */
        if (material.isUrgent() && !parentFragment.equals(InventoryFragment.class.getSimpleName())) {
            viewHolder.urgent.setVisibility(View.VISIBLE);
            viewHolder.image.setBorderColorResource(R.color.urgent_color);
        }
    }


    public void onClick(View view) {
        if (parentFragment.equals(GlobalMaterialsFragment.class.getSimpleName())){
            // TODO Thing to do in Global Materials Fragment
        } else {
            onClickOptions( view );
        }
    }

    private void onClickOptions(View view) {
        final int itemPosition = recyclerView.getChildLayoutPosition(view);

        String listToMove = "NULL";
        if (parentFragment.equals(InventoryFragment.class.getSimpleName())) listToMove = "Need List";
        else if (parentFragment.equals(NeedListFragment.class.getSimpleName())) listToMove = "Inventory";

        final CharSequence[] items = {"Edit",  /*"Move to "+listToMove,*/ "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Select an action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item){
                    case 0:
                        onClickEdit(itemPosition);
                        break;
                    case 1:
                        /*onClickMove(itemPosition);
                        break;
                    case 2:*/
                        onClickDelete(itemPosition);
                        break;
                }
            }
        });
        builder.show();
    }

    private void onClickEdit(int itemPosition ) {
        Log.i(TAG+" "+parentFragment, "Edit Material");

        ProjectMenuActivity activity = (ProjectMenuActivity)context;
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.add(R.id.editFragmentsLayout, EditMaterialFragment.newInstance(getMaterial(itemPosition), parentFragment));
        transaction.addToBackStack(null);
        transaction.commit();

        customNotifyDataSetChanged();
    }

    private void onClickMove(int itemPosition ) {
        Log.i(TAG+" "+parentFragment, "Move Material");
        //TODO: CALL MOVE
    }

    private void onClickDelete(final int itemPosition ) {
        Log.i(TAG+" "+parentFragment, "Delete Material");
        if (parentFragment.equals(InventoryFragment.class.getSimpleName())) {
            deleteInvetoryMaterial( itemPosition );
        } else if (parentFragment.equals(NeedListFragment.class.getSimpleName())) {
            deleteNeedListMaterial( itemPosition );
        }
    }

    private void deleteInvetoryMaterial(final int itemPosition) {

        ProjectClient client = retrofit.create(ProjectClient.class);

        Call<String> call = client.deleteInvetoryMaterial(projectID, getMaterial(itemPosition));

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());

                    deleteMaterial(itemPosition);
                    customNotifyDataSetChanged();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    if (response.body() != null)
                        Log.i(TAG, response.body());

                    Toast.makeText(context, R.string.note_delete_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");
            }
        });
    }

    private void deleteNeedListMaterial(final int itemPosition) {

        ProjectClient client = retrofit.create(ProjectClient.class);

        Call<String> call = client.deleteNeedListMaterial(projectID, getMaterial(itemPosition));

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());

                    deleteMaterial(itemPosition);
                    customNotifyDataSetChanged();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    if (response.body() != null)
                        Log.i(TAG, response.body());

                    Toast.makeText(context, R.string.note_delete_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");
            }
        });
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void customNotifyDataSetChanged(){
        notifyDataSetChanged();
        if (parentFragment.equals(InventoryFragment.class.getSimpleName())) {
            InventoryFragment fragment = (InventoryFragment) ((Activity)context).getFragmentManager().findFragmentById(R.id.projectFragmentsLayout);
            fragment.setVisibleList();
        } else if (parentFragment.equals(NeedListFragment.class.getSimpleName())) {
            NeedListFragment fragment = (NeedListFragment) ((Activity)context).getFragmentManager().findFragmentById(R.id.projectFragmentsLayout);
            fragment.setVisibleList();
        }


    }

    public Bitmap decodeFromBase64(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}