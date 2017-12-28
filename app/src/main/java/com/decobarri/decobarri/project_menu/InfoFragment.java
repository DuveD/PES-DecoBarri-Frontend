package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private TextView projNameText;
    private TextView projDescriptionText;

    private String projName;
    private String projDescription;
    private String projectID;

    private static final String TAG = InfoFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    private void initVars() {
        projectID = ((ProjectMenuActivity)this.getActivity()).projectID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_info, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Project Info");
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);
        // TODO: Llamada a servidor para pedir la info del proyecto
        //******************************************************************************************
        //Null pointer exception
        //projName = this.getArguments().getString("projName");
        //projDescription = this.getArguments().getString("projDescription");
        //******************************************************************************************
        return view;
    }

    @Override
    public void onStart() {
        projNameText = (TextView) getActivity().findViewById(R.id.textProjName);
        projDescriptionText = (TextView) getActivity().findViewById(R.id.descriptionText);
        super.onStart();
    }
}