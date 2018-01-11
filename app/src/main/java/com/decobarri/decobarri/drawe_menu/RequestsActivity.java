package com.decobarri.decobarri.drawe_menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.Request;
import com.decobarri.decobarri.activity_resources.RequestAdapter;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestsActivity extends AppCompatActivity {

    List<Request> requestList;
    String username, password;
    RecyclerView list;
    ProjectClient client;
    RecyclerView.LayoutManager layoutManager;
    private RequestAdapter adapter;
    LinearLayout emptyText;
    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reload_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startUpdatingAnimation();
                loadRequests();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chatToolbar);
        toolbar.setTitle("Solicitudes");
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
        client = retrofit.create(ProjectClient.class);

        //Get logged user
        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
        password = pref.getString("password", "");

        //Fill the view
        if(Objects.equals(username, "") && Objects.equals(password, "")){
            Toast.makeText(this, "Not logged", Toast.LENGTH_LONG).show();
        }
        else {
            loadRequests();
        }
    }

    private void loadRequests() {
        //TODO: Test this
        Call<List<Project>> call = client.getRequests(username);
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {

                if (response.isSuccessful() && !response.body().isEmpty()) {
                    System.out.println("Response: " + response.code());
                    System.out.println("Response: " + response.message());
                    requestList = new ArrayList<Request>();
                    for (Project p : response.body()) {
                        for ( String r : p.getRequests()) {
                            if (r!="") requestList.add(new Request(r, p.getName(), p.getId()));
                        }
                    }
                    list.setVisibility(View.VISIBLE);
                    layoutManager = new LinearLayoutManager(RequestsActivity.this);
                    list.setLayoutManager(layoutManager);
                    adapter = new RequestAdapter(requestList, list, RequestsActivity.this);
                    list.setAdapter(adapter);
                    stopUpdatingAnimation();
                }
                else {
                    emptyText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                System.out.println("Request : " + t);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void startUpdatingAnimation() {
        // Get our refresh item from the menu if it are initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem != null && menuItem.getActionView() == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout iv = (RelativeLayout) inflater.inflate(R.layout.ic_refresh, null);
                Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                menuItem.setActionView(iv);
            }
        }
    }

    public void stopUpdatingAnimation() {
        // Get our refresh item from the menu if it are initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem != null && menuItem.getActionView() != null) {
                // Remove the animation.
                menuItem.getActionView().clearAnimation();
                menuItem.setActionView(null);
            }
        }
    }
}
