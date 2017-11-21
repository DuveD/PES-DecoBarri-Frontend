package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.UserClient;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParticipantsFragment extends Fragment {

    String project_id;
    UserClient client;
    RecyclerView member_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_participants, container, false);
        getActivity().setTitle("Participants");

        member_list = (RecyclerView) getActivity().findViewById(R.id.lista_participantes);

        Bundle arg = this.getArguments();
        //project_id = arg.getString("project");

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        client = retrofit.create(UserClient.class);

        //TODO: Retrofit call to get project info
        //TODO: Get participants from project info
        //TODO: Put all participants into the adapter

        return view;
    }
}