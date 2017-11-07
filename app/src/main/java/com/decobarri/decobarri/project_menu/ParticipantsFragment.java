package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.decobarri.decobarri.R;

public class ParticipantsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_participants, container, false);

        Toast.makeText(getActivity(), "I'm in Participants", Toast.LENGTH_SHORT);

        // En principio no hacemos nada

        return view;
    }

    @Override
    public void onStart() {
        bottomSheetButtonCliked(true);
        super.onStart();
    }

    @Override
    public void onStop() {
        bottomSheetButtonCliked(false);
        super.onStop();
    }

    void bottomSheetButtonCliked(Boolean clicked){
        if (clicked){
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_participants)).setAlpha(0.4f);
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_participants)).setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_background_selected_color));
        } else {
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_participants)).setAlpha(1f);
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_participants)).setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_background));
        }
    }
}