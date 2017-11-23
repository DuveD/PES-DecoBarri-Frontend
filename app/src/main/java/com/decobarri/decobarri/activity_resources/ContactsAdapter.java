package com.decobarri.decobarri.activity_resources;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.google.gson.GsonBuilder;

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
        implements View.OnClickListener, View.OnLongClickListener {

    private List<String> contactList; //Contact/Member ids
    private RecyclerView recyclerView;
    Context context;
    String LIST;
    String username;
    Retrofit retrofit;

    public ContactsAdapter(List<String> contactList, RecyclerView recyclerView, Context context, String l) {
        this.contactList = contactList;
        this.recyclerView = recyclerView;
        this.context = context;
        this.LIST = l;
        SharedPreferences pref = context.getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView name, id;
        LinearLayout item_container;

        public ContactsViewHolder (View view) {
            super(view);
            item_container = (LinearLayout) view.findViewById(R.id.item_container);
            name = (TextView) view.findViewById(R.id.contact_name);
            id = (TextView) view.findViewById(R.id.user_id);
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
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ContactsAdapter.ContactsViewHolder holder, final int position) {
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        UserClient client = retrofit.create(UserClient.class);
        Call<User> call = client.FindByID(contactList.get(position));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    holder.id.setText(contactList.get(position));
                    holder.name.setText(response.body().getName());
                }
                holder.item_container.setVisibility(View.VISIBLE);
                System.out.println("Success: " + response.body());
                System.out.println("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onClick(View view) {

        System.out.println("OnClick members");
        if ( LIST.equals("members") ) {
            //TODO: The delete member option has to do an admin check
            //TODO: Test this functionality

            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();

            final int itemPosition = recyclerView.getChildLayoutPosition(view);

            UserClient client = retrofit.create(UserClient.class);
            Call<User> call = client.FindByID(username);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Select an action");
                    if (response.body().getContacts().contains(getContact(itemPosition))) {
                        final CharSequence[] items = {"Remove from project"};
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        removeMember(itemPosition);
                                        break;
                                }
                            }
                        });
                    }
                    else {
                        final CharSequence[] items = {"Add to contacts", "Remove from project"};
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
                    }
                    builder.show();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
        if ( LIST.equals("contacts")){
            //TODO: Redirect to chat with the contact
        }
    }

    private void removeMember(int itemPosition) {
        ProjectClient client = retrofit.create(ProjectClient.class);
        //TODO: Delete member from project member list
    }

    private void addContact(int itemPosition) {
        UserClient client = retrofit.create(UserClient.class);

        User newContact = new User();
        newContact.setId(getContact(itemPosition));
        Call<String> call = client.AddContact(username, newContact);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Success: " + response.body());
                System.out.println("Code: " + response.code());
                //TODO: Redirect to chat with this contact
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onLongClick(View view) {
        if (LIST.equals( "contacts" )) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();

            final int itemPosition = recyclerView.getChildLayoutPosition(view);
            final CharSequence[] items = {"Delete contact"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:
                            removeContact(itemPosition);
                            break;
                    }
                }
            });
            builder.show();
        }
        return false;
    }

    private void removeContact(int itemPosition) {
        UserClient client = retrofit.create(UserClient.class);
        User newContact = new User();
        newContact.setId(getContact(itemPosition));
        Call<String> call = client.DeleteContact(username, newContact);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Success: " + response.body());
                if(response.isSuccessful()){
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
