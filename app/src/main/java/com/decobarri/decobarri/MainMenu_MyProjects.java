package com.decobarri.decobarri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.ActivityResources.projectItem;
import com.decobarri.decobarri.db_resources.DB_library;

import java.util.ArrayList;
import java.util.List;

import static com.decobarri.decobarri.R.id.recycler;

public class MainMenu_MyProjects extends Fragment {
    private DB_library httpDBlibrary;

    private RecyclerView rec ;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lmanager;

    List items = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_my_projects, container, false);
        items.add(new projectItem(R.drawable.logo, "Proyecto 1", "Descripcion 1"));
        items.add(new projectItem(R.drawable.logo, "Proyecto 2", "Descripcion 2"));

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
