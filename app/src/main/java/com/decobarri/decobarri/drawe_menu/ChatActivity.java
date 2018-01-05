package com.decobarri.decobarri.drawe_menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Request;
import com.decobarri.decobarri.activity_resources.RequestAdapter;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    List<Request> requestList;
    String username, password;
    RecyclerView list;
    ProjectClient client;
    RecyclerView.LayoutManager layoutManager;
    private RequestAdapter adapter;
    LinearLayout emptyText;

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
        Call<Map<String,List<String>>> call = client.getRequests(username);
        call.enqueue(new Callback<Map<String,List<String>>>() {
            @Override
            public void onResponse(Call<Map<String,List<String>>> call, Response<Map<String,List<String>>> response) {
                System.out.println("Request : " + response.body());
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    /*for (ProjectRequests p : response.body()) {
                        for ( String r : p.getUsers()) {
                            requestList.add(new Request(p.getId(), r));
                        }
                    }*/
                    for (Map.Entry<String, List<String>> e : response.body().entrySet()){
                        for ( String s : e.getValue()) {
                            requestList.add(new Request(e.getKey(), s));
                        }
                    }
                    list.setVisibility(View.VISIBLE);
                    layoutManager = new LinearLayoutManager(ChatActivity.this);
                    list.setLayoutManager(layoutManager);
                    adapter = new RequestAdapter(requestList, list, ChatActivity.this);
                    list.setAdapter(adapter);
                }
                else {
                    emptyText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Map<String,List<String>>> call, Throwable t) {
                System.out.println("Request : " + t);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
