package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.decobarri.decobarri.R;

public class XatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_xat, container, false);

        Toast.makeText(getActivity(), "I'm in Xat", Toast.LENGTH_SHORT);

        // En principio no hacemos nada

        return view;
    }

    @Override
    public void onStart() {
        ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_xat)).setAlpha(0.4f);

        super.onStart();
    }

    @Override
    public void onStop() {
        ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_xat)).setAlpha(1f);

        super.onStop();
    }
}
