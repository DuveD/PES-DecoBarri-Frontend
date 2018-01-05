package com.decobarri.decobarri.activity_resources;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
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
import com.decobarri.decobarri.db_resources.UserProject;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
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

    private List<User> contactList; //Contact/Member ids
    private RecyclerView recyclerView;
    Context context;
    String LIST;
    String username;
    Retrofit retrofit;
    String projectId; //Null if Contact list
    String admin;

    public ContactsAdapter(List<User> contactList, RecyclerView recyclerView, Context context, String l, String project, String admin) {
        this.contactList = contactList;
        this.recyclerView = recyclerView;
        this.context = context;
        this.LIST = l;
        this.projectId = project;
        this.admin = admin;
        SharedPreferences pref = context.getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView name, id;
        ImageView friend, profileImage;
        LinearLayout item_container;

        public ContactsViewHolder (View view) {
            super(view);
            item_container = (LinearLayout) view.findViewById(R.id.item_container);
            name = (TextView) view.findViewById(R.id.contact_name);
            id = (TextView) view.findViewById(R.id.user_id);
            friend = (ImageView) view.findViewById(R.id.friend_image);
            profileImage = (ImageView) view.findViewById(R.id.item_imageView);
        }
    }

    public boolean deleteContact (String contact) {
        return contactList.remove(contact);
    }

    public User deleteContact (int index) {
        return contactList.remove(index);
    }

    public User getContact (int index) {
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
        final UserClient client = retrofit.create(UserClient.class);
        Call<User> call;
        holder.id.setText(contactList.get(position).getId());
        System.out.println("Id: " + contactList.get(position).getId());
        holder.name.setText(contactList.get(position).getName());
        holder.item_container.setVisibility(View.VISIBLE);

        if (Objects.equals(LIST, "members")){
            if (!Objects.equals(username, contactList.get(position))) {

                call = client.FindByID(username);
                call.enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getContacts().contains(contactList.get(position).getId())) {
                                holder.friend.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("Error: " + t);
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                Call<ResponseBody> image_call = client.downloadImage(username);
                image_call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                            if(bm!=null)holder.profileImage.setImageBitmap(
                                    Bitmap.createScaledBitmap(bm, holder.profileImage.getWidth(), holder.profileImage.getHeight(), false));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onClick(View view) {

        System.out.println("OnClick members");
        if ( LIST.equals("members") ) {

            final int itemPosition = recyclerView.getChildLayoutPosition(view);

            if(!Objects.equals(getContact(itemPosition).getId(), username)) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(context.getResources().getString(R.string.db_URL))
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                        .build();

                UserClient client = retrofit.create(UserClient.class);
                Call<User> call = client.FindByID(username);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Select an action");
                        if (response.body().getContacts().contains(getContact(itemPosition)) && Objects.equals(username, admin)) {
                            final CharSequence[] items = {"Remove from project"};
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            removeMember(itemPosition);
                                            break;
                                    }
                                }
                            });
                        } else if (Objects.equals(username, admin)){
                            final CharSequence[] items = {"Add to contacts", "Remove from project"};
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            addContact(itemPosition);
                                            break;
                                        case 1:
                                            removeMember(itemPosition);
                                            break;
                                    }
                                }
                            });
                        } else {
                            final CharSequence[] items = {"Add to contacts"};
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i) {
                                        case 0:
                                            addContact(itemPosition);
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
        }
        if ( LIST.equals("contacts")){
            //TODO: Redirect to chat with the contact
        }
    }

    private void removeMember(final int itemPosition) {
        UserClient client = retrofit.create(UserClient.class);
        System.out.println("Project id: " + projectId);
        System.out.println("User id: " + getContact(itemPosition));
        UserProject p = new UserProject(projectId);
        Call<String> call = client.DeleteProject(getContact(itemPosition).getId(), p);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    deleteContact(itemPosition);
                    notifyDataSetChanged();
                }
                System.out.println("Success: " + response.body());
                System.out.println("Code: " + response.code());
                System.out.println("Error: " + response.message());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addContact(final int itemPosition) {
        UserClient client = retrofit.create(UserClient.class);

        User newContact = new User();
        newContact.setId(getContact(itemPosition).getId());
        Call<String> call = client.AddContact(username, newContact);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Success: " + response.body());
                System.out.println("Code: " + response.code());

                notifyDataSetChanged();
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

    private void removeContact(final int itemPosition) {
        UserClient client = retrofit.create(UserClient.class);
        User newContact = new User();
        newContact.setId(getContact(itemPosition).getId());
        Call<String> call = client.DeleteContact(username, newContact);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Success: " + response.body());
                if(response.isSuccessful()){
                    deleteContact(itemPosition);
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

    public void setFilter(List<User> filteredList) {
        this.contactList = filteredList;
        notifyDataSetChanged();
    }
}
