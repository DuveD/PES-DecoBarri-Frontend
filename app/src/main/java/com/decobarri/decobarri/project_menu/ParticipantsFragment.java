package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.drawe_menu.ContactListActivity;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticipantsFragment extends Fragment {

    private String project_id;
    private ProjectClient client;
    private RecyclerView member_list;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Context context;
    private Menu menu;

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
        inflater.inflate(R.menu.reload_menu, optionsMenu);
        menu = optionsMenu;

        if (ProjectMenuActivity.getUpdatingItemList())
            startUpdatingAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadMembers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startUpdatingAnimation() {
        // Get our refresh item from the menu if it is initialized
        if (menu != null) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout iv = (RelativeLayout)inflater.inflate(R.layout.ic_refresh, null);
            Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_refresh);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);
            menu.findItem(R.id.action_refresh).setActionView(iv);
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

        project_id = this.getArguments().getString("project", "");

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));

        Retrofit retrofit = builder.build();
        client = retrofit.create(ProjectClient.class);

        loadMembers();

    }

    private void loadMembers() {
        Call<Project> call = client.FindProjectById(project_id);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.isSuccessful()) {
                    List<String> list = response.body().getMembers();
                    layoutManager = new LinearLayoutManager(context);
                    member_list.setLayoutManager(layoutManager);

                    adapter = new ContactsAdapter(list, member_list, getActivity(), "members", project_id);
                    member_list.setAdapter(adapter);
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
    }
}