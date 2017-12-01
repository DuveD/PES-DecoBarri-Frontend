package com.decobarri.decobarri.activity_resources.Items;

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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;
import com.decobarri.decobarri.project_menu.edit_items.EditItemActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter
        extends Adapter<ItemAdapter.ItemViewHolder>
        implements OnClickListener, View.OnLongClickListener {

    private List<Item> itemList;
    private RecyclerView recyclerView;
    Context context;
    private static final String TAG = "ItemAdapter";

    public ItemAdapter(List<Item> itemList, RecyclerView recyclerView, Context context) {
        this.itemList = itemList;
        this.recyclerView = recyclerView;
        this.context = context;
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
        view.setOnLongClickListener(this);
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
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        Item item = itemList.get(itemPosition);

        CharSequence text = "Item description: " + item.getDescription();
        Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onLongClick(View view) {
        final int itemPosition = recyclerView.getChildLayoutPosition(view);

        final CharSequence[] items = {"Edit", "Delete"};
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
                        onLongClickDelete(itemPosition);
                        break;
                }
            }
        });
        builder.show();
        return true;
    }

    private void onLongClickEdit( int itemPosition ) {
        Log.i(TAG, "Edit Item");
        Intent intent = new Intent(context, EditItemActivity.class);
        intent.putExtra(Const.EDIT_ITEM, true);
        intent.putExtra(Const.ID, getItem( itemPosition ).getID());
        context.startActivity(intent);

        customNotifyDataSetChanged();
    }

    private void onLongClickDelete( int itemPosition ) {
        Log.i(TAG, "Delete Material");
        deleteItem(itemPosition);
        customNotifyDataSetChanged();
    }

    public void customNotifyDataSetChanged(){
        notifyDataSetChanged();
    }
}