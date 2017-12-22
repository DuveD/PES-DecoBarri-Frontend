package com.decobarri.decobarri.project_menu;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.ContactsAdapter;
import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.drawe_menu.ContactListActivity;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticipantsFragment extends Fragment {

    private String project_id;
    private ProjectClient client;
    private UserClient userClient;
    private Retrofit retrofit;
    private RecyclerView member_list;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Context context;
    private Menu menu;
    private List<User> userList;
    private static final String TAG = ParticipantsFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload_menu_gray, optionsMenu);
        menu = optionsMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startUpdatingAnimation();
                loadMembers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startUpdatingAnimation() {
        // Get our refresh item from the menu if it is initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem != null && menuItem.getActionView() == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout iv = (RelativeLayout) inflater.inflate(R.layout.ic_refresh_gray, null);
                Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                menuItem.setActionView(iv);
            }
        }
    }

    public void stopUpdatingAnimation() {
        // Get our refresh item from the menu if it is initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem.getActionView() != null) {
                // Remove the animation.
                menuItem.getActionView().clearAnimation();
                menuItem.setActionView(null);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_participants, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Participants");
        member_list = (RecyclerView) view.findViewById(R.id.lista_participantes);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        project_id = ProjectMenuActivity.projectId;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));

        retrofit = builder.build();
        client = retrofit.create(ProjectClient.class);

        loadMembers();

    }

    private void loadMembers() {
        Call<List<User>> call = client.GetMembers(project_id);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    userList = response.body();

                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User itemA, User itemB) {
                            return itemA.getName().compareToIgnoreCase(itemB.getName());
                        }
                    });

                    System.out.println("Response: " + response.body());
                    layoutManager = new LinearLayoutManager(context);
                    member_list.setLayoutManager(layoutManager);

                    userClient = retrofit.create(UserClient.class);

                    adapter = new ContactsAdapter(userList, member_list, getActivity(), "members", project_id);
                    member_list.setAdapter(adapter);
                }
                else {
                    System.out.println("Error code: " + response.code());
                    System.out.println("Error msg: " + response.message());
                }
                stopUpdatingAnimation();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Error msg: " + t.getMessage());
                stopUpdatingAnimation();
            }
        });
        /*
        Call<Project> call = client.FindProjectById(project_id);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.isSuccessful()) {
                    List<String> list = response.body().getMembers();
                    layoutManager = new LinearLayoutManager(context);
                    member_list.setLayoutManager(layoutManager);

                    userClient = retrofit.create(UserClient.class);

                    userList = new ArrayList<>();
                    adapter = new ContactsAdapter(userList, member_list, getActivity(), "members", project_id);
                    member_list.setAdapter(adapter);

                    for (String u:list) {
                        Call<User> userCall = userClient.FindByID(u);
                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    userList.add(response.body());
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }


                }
                else {
                    System.out.println("Error: " + response.code());
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

}