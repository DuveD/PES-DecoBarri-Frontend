package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    String project_id;
    ProjectClient client;
    RecyclerView member_list;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
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

        Bundle arg = this.getArguments();
        //project_id = arg.getString("project");

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));

        Retrofit retrofit = builder.build();
        client = retrofit.create(ProjectClient.class);

        //------------Prueba-----------------
        //TODO: Remove this part
        Call<List<Project>> call = client.FindAllProjects();
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                loadMembers(response.body().get(0).getId());
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {

            }
        });

        //TODO: Get Project Id from arguments
        //loadMembers(ProjectId);

    }

    private void loadMembers(String ProjectId) {
        Call<Project> call = client.FindProjectById(ProjectId);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.isSuccessful()) {
                    List<String> list = response.body().getMembers();
                    layoutManager = new LinearLayoutManager(context);
                    member_list.setLayoutManager(layoutManager);

                    adapter = new ContactsAdapter(list, member_list, getActivity(), "members");
                    member_list.setAdapter(adapter);
                }
                else {
                    System.out.println("Error: " + response.errorBody());
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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