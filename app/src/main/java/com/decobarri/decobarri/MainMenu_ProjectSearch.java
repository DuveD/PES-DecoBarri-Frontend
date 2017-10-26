package com.decobarri.decobarri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.ActivityResources.projectAdapter;
import com.decobarri.decobarri.ActivityResources.projectItem;
import com.decobarri.decobarri.db_resources.DB_library;

import java.util.ArrayList;
import java.util.List;

public class MainMenu_ProjectSearch extends Fragment {
    private DB_library httpDBlibrary;

    private RecyclerView rec ;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmanager;

    List items = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_project_search, container, false);
        items.add(new projectItem(R.drawable.example_vallespir, "Decoracio carrer Vallespi", "Aquest any tornarem a participar a" +
                " les festes de sants."));
        items.add(new projectItem(R.drawable.example_christmas_school, "Projecte de decoració de Nadal de l'escola Les Corts", "Decoració " +
                "ambientada en temàtica de nadal per als nens fins a 3r de primaria."));
        items.add(new projectItem(R.drawable.example_festes_esplugues, "Festes d' Esplugues", "Col·labora a fer millor les festes" +
                " del nostre barri i participa en la organització de les seves activitats."));
        items.add(new projectItem(R.drawable.example_street_gracia, "Decoració del carrer Rossend Arús", "Decorarem el nostre carrer" +
                " amb l'objectiu de tornar a quedar com el millor carrer de les festes de gràcia, tal com vam aconseguir l'any passat."));
        items.add(new projectItem(R.drawable.example_christmas_centre_cultural, "Decoració temàtica de Nadal del centre cultural Les Corts.", "" +
                " Ajuda a decorar el nostre centre i participa en els events que tenim preparats per aquest nadal."));

        rec = (RecyclerView) view.findViewById(R.id.recycler);
        lmanager = new LinearLayoutManager(getActivity());;
        rec.setLayoutManager(lmanager);

        adapter = new projectAdapter(items);
        rec.setAdapter(adapter);

        // BD_library init wiht activity context
        httpDBlibrary = new DB_library( this.getActivity() );

        return view;
    }

    public void onCreateTest( View view ){
        String call = "";
        String result = "You are on tab_fragment_my_projects"/*httpDBlibrary.db_call( call )*/;

        Toast toast = Toast.makeText(view.getContext(), result, Toast.LENGTH_SHORT);
        toast.show();
    }
}
