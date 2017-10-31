package com.decobarri.decobarri.main_menu;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.ProjectAdapter;
import com.decobarri.decobarri.activity_resources.ProjectItem;
import com.decobarri.decobarri.db_resources.DB_library;

import java.util.ArrayList;
import java.util.List;

public class MyProjectsFragment extends Fragment {
    List items = new ArrayList();
    private DB_library httpDBlibrary;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmanager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RecyclerView rec = (RecyclerView) getView().findViewById(R.id.myprojects_recycler);

        items.add(new ProjectItem(BitmapFactory.decodeResource(getResources(),
                R.drawable.example_street_gracia),
                "Decoració del carrer Rossend Arús", "Decorarem el nostre carrer" +
                " amb l'objectiu de tornar a quedar com el millor carrer de les festes de gràcia, tal com vam aconseguir l'any passat."));
        items.add(new ProjectItem(BitmapFactory.decodeResource(getResources(),
                R.drawable.example_festes_esplugues),
                "Festes d' Esplugues", "Col·labora a fer millor les festes" +
                " del nostre barri i participa en la organització de les seves activitats."));

        lmanager = new LinearLayoutManager(getActivity());
        ;
        rec.setLayoutManager(lmanager);

        adapter = new ProjectAdapter(items);
        rec.setAdapter(adapter);

        // BD_library init wiht activity context
        httpDBlibrary = new DB_library(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_projects, container, false);
        return view;
    }

    public void onCreateTest(View view) {
        String call = "";
        String result = "You are on fragment_my_projects"/*httpDBlibrary.db_call( call )*/;

        Toast toast = Toast.makeText(view.getContext(), result, Toast.LENGTH_SHORT);
        toast.show();
    }
}