package com.decobarri.decobarri.activity_resources;

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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;

    private RecyclerView recyclerView;

    public ItemAdapter(ArrayList<Item> itemList, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.recyclerView = recyclerView;
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

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        view.setOnClickListener(onClickListener);
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
        viewHolder.image.setImageBitmap(itemList.get(position).getImage());

        /* SET ITEM NAME */
        viewHolder.name.setText(itemList.get(position).getName());

        /* SET ITEM ADRESS */
        viewHolder.description.setText(itemList.get(position).getDescription());
    }

    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            int itemPosition = recyclerView.getChildLayoutPosition(view);
            Item item = itemList.get(itemPosition);

            CharSequence text = "Item description: " + item.getDescription();
            Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    };
}