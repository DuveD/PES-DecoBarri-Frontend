package com.decobarri.decobarri.project_menu;

import android.annotation.SuppressLint;
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

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
        startMainFragment();

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
        ((LinearLayout) findViewById(R.id.bottom_sheet_info)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_peopleList)).setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //this.bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        switch (view.getId()) {
            case R.id.bottom_sheet_info: default:
                InfoFragment infoFragment = new InfoFragment();
                transaction.replace(R.id.ProjectMenuLayout, infoFragment);
                break;
            case R.id.bottom_sheet_notes:
                NotesFragment notesFragment = new NotesFragment();
                transaction.replace(R.id.ProjectMenuLayout,notesFragment);
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
            case R.id.bottom_sheet_xat:
                XatFragment xatFragment = new XatFragment();
                transaction.replace(R.id.ProjectMenuLayout,xatFragment);
                break;
            case R.id.bottom_sheet_peopleList:
                ParticipantsFragment participantsFragment = new ParticipantsFragment();
                transaction.replace(R.id.ProjectMenuLayout,participantsFragment);
                break;
        }
        this.bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
        transaction.commit();
    }

    private void startMainFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        InfoFragment infoFragment = new InfoFragment();
        transaction.replace(R.id.ProjectMenuLayout, infoFragment);
        transaction.commit();
    }
}

