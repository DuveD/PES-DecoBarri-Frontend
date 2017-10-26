package com.decobarri.decobarri;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.decobarri.decobarri.db_resources.DB_library;

public class MainMenu_ProjectSearch extends Fragment {
    private DB_library httpDBlibrary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_project_search, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);
        String loggeduser = pref.getString("username", "");

        // BD_library init wiht activity context
        httpDBlibrary = new DB_library( this.getActivity() );

        return view;
    }
}
