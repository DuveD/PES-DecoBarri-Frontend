package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;

public class ParticipantsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_participants, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Participants");
        return view;
    }
}