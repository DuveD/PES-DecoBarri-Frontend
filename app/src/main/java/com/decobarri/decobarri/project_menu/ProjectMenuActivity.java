package com.decobarri.decobarri.project_menu;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.decobarri.decobarri.BaseActivity;
import com.decobarri.decobarri.R;

public class ProjectMenuActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton btnExpBottomSheet;
    private BottomSheetBehavior bottomDrawer;
    private LinearLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
        startProjectInfoFragment();

        bottomSheet = (LinearLayout) findViewById(R.id.bottomSheet);
        bottomDrawer = BottomSheetBehavior.from(bottomSheet);

        btnExpBottomSheet = (ImageButton) findViewById(R.id.btnExpBottomSheet);
        btnExpBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomDrawer.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomDrawer.setState(BottomSheetBehavior.STATE_EXPANDED);
                else
                    bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        bottomDrawer.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                btnExpBottomSheet.animate().rotation(slideOffset * 180).setInterpolator(new AccelerateDecelerateInterpolator());
            }
        });

        ((LinearLayout) findViewById(R.id.bottom_sheet_notes)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_xat)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_inventory)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_needList)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_items)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_map)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_project_info)).setOnClickListener(this);
    }

    @Override
    public Boolean onBackPressedStatement() {
        if (bottomDrawer.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(@NonNull View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int button = view.getId();
        switch (button) {
            case R.id.bottom_sheet_notes: default:
                NotesFragment notesFragment = new NotesFragment();
                transaction.replace(R.id.ProjectMenuLayout,notesFragment);
                break;
            case R.id.bottom_sheet_xat:
                XatFragment xatFragment = new XatFragment();
                transaction.replace(R.id.ProjectMenuLayout,xatFragment);
                break;
            case R.id.bottom_sheet_inventory:
                InventoryFragment inventoryFragment = new InventoryFragment();
                transaction.replace(R.id.ProjectMenuLayout,inventoryFragment);
                break;
            case R.id.bottom_sheet_needList:
                NeedListFragment needListFragment = new NeedListFragment();
                transaction.replace(R.id.ProjectMenuLayout,needListFragment);
                break;
            case R.id.bottom_sheet_items:
                ItemsFragment itemsFragment = new ItemsFragment();
                transaction.replace(R.id.ProjectMenuLayout,itemsFragment);
                break;
            case R.id.bottom_sheet_map:
                MapFragment mapFragment = new MapFragment();
                transaction.replace(R.id.ProjectMenuLayout,mapFragment);
                break;
            case R.id.bottom_project_info:
                ProjectFragment projectFragment = new ProjectFragment();
                transaction.replace(R.id.ProjectMenuLayout,projectFragment);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
        this.bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void startProjectInfoFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ProjectFragment projectFragment = new ProjectFragment();
        transaction.replace(R.id.ProjectMenuLayout,projectFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

