package com.decobarri.decobarri.drawe_menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.ContactsAdapter;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactListActivity extends AppCompatActivity {

    List<User> userList;
    String username, password; //User logged
    User userlogged;
    RecyclerView list;
    UserClient client;
    RecyclerView.LayoutManager layoutManager;
    private ContactsAdapter adapter;
    LinearLayout emptyText;
    private SearchView searchView;

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
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));

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

        Call<List<User>> call = client.GetContacts(username);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                System.out.println("contacts: " + response.body());
                if(response.isSuccessful() && !response.body().isEmpty()) {
                    userList = response.body();
                    list.setVisibility(View.VISIBLE);

                    layoutManager = new LinearLayoutManager(ContactListActivity.this);
                    list.setLayoutManager(layoutManager);

                    adapter = new ContactsAdapter(userList, list, ContactListActivity.this, "contacts", null, null);
                    list.setAdapter(adapter);
                }else {
                    emptyText.setVisibility(View.VISIBLE);
                }
                System.out.println("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Internal error", Toast.LENGTH_SHORT);
                emptyText.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionsMenu) {
        getMenuInflater().inflate(R.menu.menu_search, optionsMenu);

        MenuItem myActionMenuItem = optionsMenu.findItem(R.id.options_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<User> filteredModelList = filter(userList, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }

        });

        return super.onCreateOptionsMenu(optionsMenu);
    }

    private List<User> filter(List<User>userList, String query) {
        query = query.toLowerCase();final List<User> filteredModelList = new ArrayList<>();
        for (User c : userList) {
            final String text = c.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(c);
            }
        }
        return filteredModelList;
    }
}
