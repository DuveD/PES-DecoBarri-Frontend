package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;

public class InfoFragment extends Fragment {

    private TextView projNameText;
    private TextView projDescriptionText;

    private String projName;
    private String projDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_info, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Project Info");
        //******************************************************************************************
        //Null pointer exception
        //projName = this.getArguments().getString("projName");
        //projDescription = this.getArguments().getString("projDescription");
        //******************************************************************************************
        return view;
    }

    @Override
    public void onStart() {
        bottomSheetButtonCliked(true);
        projNameText = (TextView) getActivity().findViewById(R.id.textProjName);
        projDescriptionText = (TextView) getActivity().findViewById(R.id.descriptionText);

        super.onStart();
    }

    @Override
    public void onStop() {
        bottomSheetButtonCliked(false);
        super.onStop();
    }

    void bottomSheetButtonCliked(Boolean clicked){
        if (clicked){
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setAlpha(0.4f);
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_background_selected_color));
        } else {
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setAlpha(1f);
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_background));
        }
    }
}