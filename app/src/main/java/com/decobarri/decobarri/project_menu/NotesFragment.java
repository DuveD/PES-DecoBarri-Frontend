package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.decobarri.decobarri.R;

public class NotesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_notes, container, false);

        Toast.makeText(getActivity(), "I'm in Notes", Toast.LENGTH_SHORT);

        // En principio no hacemos nada

        return view;
    }

    @Override
    public void onStart() {
        ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_notes)).setAlpha(0.4f);

        super.onStart();
    }

    @Override
    public void onStop() {
        ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_notes)).setAlpha(1f);

        super.onStop();
    }
}
