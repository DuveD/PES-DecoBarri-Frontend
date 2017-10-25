package com.decobarri.decobarri;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.decobarri.decobarri.db_resources.DB_library;

public class MainMenu_ProjectSearch extends Fragment {
    private DB_library httpDBlibrary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_project_search, container, false);

        // BD_library init wiht activity context
        httpDBlibrary = new DB_library( this.getActivity() );

        return view;
    }
}
