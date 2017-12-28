package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;

public class MapFragment extends Fragment {

    private String projectID;

    private static final String TAG = MapFragment.class.getSimpleName();

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
        View view = inflater.inflate(R.layout.fragment_project_map, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Map");
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);
        return view;
    }
}
