package com.decobarri.decobarri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.db_resources.DB_library;

public class MainMenu_MyProjects extends Fragment {
    private DB_library httpDBlibrary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_my_projects, container, false);

        // BD_library init wiht activity context
        httpDBlibrary = new DB_library(this.getActivity());
        String result = httpDBlibrary.db_call("/user/findAll");


        Toast toast = Toast.makeText(view.getContext(), result, Toast.LENGTH_SHORT);
        toast.show();

        return view;
    }
}
