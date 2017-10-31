package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.R;

public class ItemsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        Toast.makeText(getActivity(), "I'm in Items", Toast.LENGTH_SHORT);

        // En principio no hacemos nada

        return view;
    }
}
