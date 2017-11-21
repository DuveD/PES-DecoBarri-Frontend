package com.decobarri.decobarri.drawe_menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.ContactsAdapter;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactListActivity extends AppCompatActivity {

    String username, password; //User logged
    User userlogged;
    RecyclerView list;
    UserClient client;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    LinearLayout emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.contactListToolbar);
        toolbar.setTitle("Contatct List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        list = (RecyclerView) findViewById(R.id.list);
        emptyText = (LinearLayout) findViewById(R.id.emptyText);

        //Initialize Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        client = retrofit.create(UserClient.class);

        //Get logged user
        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
        password = pref.getString("password", "");

        //Fill the view
        if(Objects.equals(username, "") && Objects.equals(password, "")){
            Toast.makeText(this, "Not logged", Toast.LENGTH_LONG).show();
        }
        else {
            loadContacts();
        }

    }

    private void loadContacts() {

        userlogged = new User();

        Call<User> call = client.FindByID(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userlogged = response.body();
                System.out.println("Success: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ContactListActivity.this, "Error", Toast.LENGTH_SHORT);
                System.out.println("Error: " + t.getMessage());
            }
        });

        List<String> contactlist = userlogged.getContacts();

        //--------------Prueba------------
        contactlist = new ArrayList<String>();
        contactlist.add("Jordi");

        if(contactlist!=null) {

            emptyText.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

            layoutManager = new LinearLayoutManager(this);
            list.setLayoutManager(layoutManager);

            adapter = new ContactsAdapter(contactlist, list, this, "contacts");
            list.setAdapter(adapter);
        }
        System.out.println("Success!!!");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
