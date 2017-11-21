package com.decobarri.decobarri.activity_resources;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Asus on 18/11/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>
        implements View.OnClickListener {

    private List<String> contactList;
    private RecyclerView recyclerView;
    Context context;
    String LIST;
    String username;
    UserClient client;

    public ContactsAdapter(List<String> contactList, RecyclerView recyclerView, Context context, String l) {
        this.contactList = contactList;
        this.recyclerView = recyclerView;
        this.context = context;
        this.LIST = l;
        SharedPreferences pref = context.getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ContactsViewHolder (View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.contact_name);
        }
    }

    public boolean deleteContact (String contact) {
        return contactList.remove(contact);
    }

    public String deleteContact (int index) {
        return contactList.remove(index);
    }

    public String getContact (int index) {
        return contactList.get(index);
    }

    @Override
    public ContactsAdapter.ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_view, parent, false);
        ContactsViewHolder vh = new ContactsViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ContactsViewHolder holder, int position) {
        holder.name.setText(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onClick(View view) {
        if ( LIST.equals("members") ) {
            //TODO: The delete member option ha to do an admin check
            //TODO: Test this functionality
            final int itemPosition = recyclerView.getChildLayoutPosition(view);
            final CharSequence[] items = {"Add to contacts", "Remove from project"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select an action");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0:
                            addContact(itemPosition);
                            break;
                        case 1:
                            removeMember(itemPosition);
                            break;
                    }
                }
            });
            builder.show();
        }
    }

    private void removeMember(int itemPosition) {

    }

    private void addContact(int itemPosition) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        client = retrofit.create(UserClient.class);

        User newContact = new User();
        newContact.setId(getContact(itemPosition));
        Call<String> call = client.AddContact(username, newContact);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Success: " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT);
            }
        });
    }
}
