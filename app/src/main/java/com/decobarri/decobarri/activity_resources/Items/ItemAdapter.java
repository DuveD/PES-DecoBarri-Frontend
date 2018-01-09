package com.decobarri.decobarri.activity_resources.Items;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.ItemClient;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.project_menu.ItemsFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.decobarri.decobarri.project_menu.edit_items.EditItemFragment;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemAdapter
        extends Adapter<ItemAdapter.ItemViewHolder>
        implements OnClickListener {

    private String projectId;
    private List<Item> itemList;
    private RecyclerView recyclerView;
    Context context;
    private static final String TAG = "ItemAdapter";

    public ItemAdapter(List<Item> itemList, RecyclerView recyclerView, Context context, String project) {
        this.itemList = itemList;
        this.recyclerView = recyclerView;
        this.context = context;
        this.projectId = project;
    }

    // View lookup cache
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView description;

        public ItemViewHolder (View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.item_imageView);
            name = (TextView) view.findViewById(R.id.item_name);
            description = (TextView) view.findViewById(R.id.item_description);
        }
    }

    public boolean deleteItem (Item item) {
        return itemList.remove(item);
    }

    public Item deleteItem (int index) {
        return itemList.remove(index);
    }

    public Item getItem (int index) {
        return itemList.get(index);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        view.setOnClickListener(this);
        ItemViewHolder item = new ItemViewHolder(view);
        return item;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {

        /* SET ITEM IMAGE */
        Picasso.with(context)
                .load(itemList.get(position).getImage())
                .resize(70, 70)
                .centerCrop()
                .into(viewHolder.image);

        /* SET ITEM NAME */
        viewHolder.name.setText(itemList.get(position).getName());

        /* SET ITEM ADRESS */
        viewHolder.description.setText(itemList.get(position).getDescription());
    }

    @Override
    public void onClick(View view) {
        final int itemPosition = recyclerView.getChildLayoutPosition(view);

        final CharSequence[] items = {"Edit", "Delete"};
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
                        onClickDelete(itemPosition);
                        break;
                }
            }
        });
        builder.show();
    }

    private void onClickEdit( int itemPosition ) {
        Log.i(TAG, "Edit Item");
        ProjectMenuActivity activity = (ProjectMenuActivity)context;
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.add(R.id.DrawerLayout, EditItemFragment.newInstance(getItem(itemPosition)));
        transaction.addToBackStack(null);
        transaction.commit();

        customNotifyDataSetChanged();
    }

    private void onClickDelete( int itemPosition ) {
        Log.i(TAG, "Delete Material");

        Item item= new Item();
        item.setId(getItem(itemPosition).getID());


        System.out.println("Project id: " + projectId);
        System.out.println("Item id: " + item.getID());

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
        Retrofit retrofit = builder.build();
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<String> call = client.DeleteItem(projectId, item);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());
                if (response.isSuccessful()){
                    System.out.println("Response: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error: " + t.toString());
            }
        });
        deleteItem(itemPosition);
        customNotifyDataSetChanged();
    }

    public void customNotifyDataSetChanged(){
        notifyDataSetChanged();
    }
}