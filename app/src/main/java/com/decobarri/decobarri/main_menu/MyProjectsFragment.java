package com.decobarri.decobarri.main_menu;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Projects.AllProjectsAdapter;
import com.decobarri.decobarri.activity_resources.Projects.MyProjectsAdapter;
import com.decobarri.decobarri.activity_resources.Projects.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.BIND_IMPORTANT;
import static android.content.Context.MODE_PRIVATE;

public class MyProjectsFragment extends Fragment {
    List<String> items = new ArrayList();
    List<com.decobarri.decobarri.db_resources.Project> projectList = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmanager;
    private RecyclerView rec;
    private String username;
    private static final String TAG = "MyProjectsFragment";
    private User user;
    private Retrofit retrofit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initVars();
    }

    private void initVars() {
        items = new ArrayList<>();
        retrofit = ((MainMenuActivity)this.getActivity()).retrofit;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rec = (RecyclerView) getView().findViewById(R.id.myprojects_recycler);
        SharedPreferences pref = this.getActivity().getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
        fillList();
        setView();
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create = new Intent(getActivity().getApplicationContext(), CreateProjectActivity.class);
                startActivity(create);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_projects, container, false);
        return view;
    }

    private void fillList() {
        UserClient userClient = retrofit.create(UserClient.class);
        ProjectClient projectClient = retrofit.create(ProjectClient.class);
        getUser(userClient, username, projectClient);
    }

    private void setView() {
        lmanager = new LinearLayoutManager(getActivity());
        rec.setLayoutManager(lmanager);
        adapter = new MyProjectsAdapter(projectList, getActivity(), rec);
        rec.setAdapter(adapter);
    }

    private void getUser(UserClient userClient, String username, final ProjectClient projectClient){
        Call<User> call = userClient.FindByID(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // The network call was a success and we got a response
                if (response.isSuccessful()) {
                    Log.i(TAG, "Call Success: " + call.request());
                    Log.i(TAG, "Success : " + response.body());
                    user = response.body();
                    Log.i(TAG, "getProjects: " + user.getProjects());
                    items = user.getProjects();
                    Log.i(TAG, "Items Size in call: " + items.size());
                    getProjects(projectClient);
                    setView();
                } else {
                    Log.e(TAG, "Response failed: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Call failed: " + call.request());
            }
        });
    }

    private void getProjects(ProjectClient projectClient) {
        for (int i = 0; i < items.size(); ++i) {
            Call<com.decobarri.decobarri.db_resources.Project> callProjects = projectClient.FindProjectById(items.get(i));
            callProjects.enqueue(new Callback<com.decobarri.decobarri.db_resources.Project>() {
                @Override
                public void onResponse(Call<com.decobarri.decobarri.db_resources.Project> callProjects, Response<com.decobarri.decobarri.db_resources.Project> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "Call Success: " + callProjects.request());
                        projectList.add(response.body());
                        setView();
                    }
                }
                @Override
                public void onFailure(Call<com.decobarri.decobarri.db_resources.Project> callProjects, Throwable t) {
                    Log.e(TAG, "Call failed: " + callProjects.request());
                }
            });
        }
    }
}