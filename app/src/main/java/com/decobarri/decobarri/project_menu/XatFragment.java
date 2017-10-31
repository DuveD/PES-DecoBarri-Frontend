package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.R;

public class XatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xat, container, false);

        Toast.makeText(getActivity(), "I'm in Xat", Toast.LENGTH_SHORT);

        // En principio no hacemos nada

        return view;
    }
}
