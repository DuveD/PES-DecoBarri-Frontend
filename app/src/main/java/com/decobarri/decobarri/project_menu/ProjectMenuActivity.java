package com.decobarri.decobarri.project_menu;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.decobarri.decobarri.BaseActivity;
import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.project_menu.edit_items.EditItemFragment;
import com.decobarri.decobarri.project_menu.edit_items.EditMaterialFragment;
import com.decobarri.decobarri.project_menu.edit_items.EditNoteFragment;

public class ProjectMenuActivity extends BaseActivity implements View.OnClickListener {

    private BottomSheetBehavior bottomDrawer;
    private LinearLayout bottomSheet;

    ProjectClient client;
    Project project;

    private int previousBottomSheetClickedItem;
    private int lastBottomSheetClickedItem;
    static public String projectID;

    String currentFragment;

    InfoFragment infoFragment;
    InventoryFragment inventoryFragment;
    ItemsFragment itemsFragment;
    MapFragment mapFragment;
    NeedListFragment needListFragment;
    NotesFragment notesFragment;
    ParticipantsFragment participantsFragment;
    XatFragment xatFragment;

    private static final String TAG = ProjectMenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
        initVars();
        startMainFragment();
        setUpBottomSheet();
    }

    private void startMainFragment(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_view,  infoFragment);
        transaction.commit();
        bottomSheetButtonCliked();
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(null);

        LinearLayout tvSave = (LinearLayout) findViewById(R.id.Toolbar_icon);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.setDrawerIndicatorEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initVars() {
        // Initialized by default
        previousBottomSheetClickedItem = R.id.bottom_sheet_info;
        lastBottomSheetClickedItem = R.id.bottom_sheet_info;

        // Getting project ID
        projectID = getIntent().getStringExtra(Const.ID);

        infoFragment = new InfoFragment();
        inventoryFragment = new InventoryFragment();
        itemsFragment = new ItemsFragment();
        mapFragment = new MapFragment();
        needListFragment = new NeedListFragment();
        notesFragment = new NotesFragment();
        participantsFragment = new ParticipantsFragment();
        xatFragment = new XatFragment();

    }

    @Override
    public Boolean onBackPressedExtraAction() {
        if (bottomDrawer.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (bottomDrawer.getState()==BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottomSheet.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                    bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        previousBottomSheetClickedItem = lastBottomSheetClickedItem;
        lastBottomSheetClickedItem = view.getId();
        switch (lastBottomSheetClickedItem) {
            case R.id.bottom_sheet_info: default:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_info) {
                    activePlusFloatingButton(false);
                    transaction.replace(R.id.fragment_view, infoFragment);
                }
                break;
            case R.id.bottom_sheet_notes:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_notes)
                    activePlusFloatingButton(true);
                    transaction.replace(R.id.fragment_view, notesFragment);
                break;
            case R.id.bottom_sheet_inventory:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_inventory)
                    activePlusFloatingButton(true);
                    transaction.replace(R.id.fragment_view, inventoryFragment);
                break;
            case R.id.bottom_sheet_need_list:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_need_list)
                    activePlusFloatingButton(true);
                    transaction.replace(R.id.fragment_view, needListFragment);
                break;
            case R.id.bottom_sheet_items:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_items)
                    activePlusFloatingButton(true);
                    transaction.replace(R.id.fragment_view, itemsFragment);
                break;
            case R.id.bottom_sheet_map:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_map)
                    activePlusFloatingButton(false);
                    transaction.replace(R.id.fragment_view, mapFragment);
                break;
            case R.id.bottom_sheet_xat:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_xat)
                    activePlusFloatingButton(false);
                    transaction.replace(R.id.fragment_view, xatFragment);
                break;
            case R.id.bottom_sheet_group:
                if (previousBottomSheetClickedItem != R.id.bottom_sheet_group)
                    activePlusFloatingButton(false);
                    transaction.replace(R.id.fragment_view, participantsFragment);
                break;
        }
        bottomSheetButtonCliked();
        bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
        transaction.commit();
        resetViewPosition();
    }

    private void activePlusFloatingButton(boolean active) {
        if (active) ((FloatingActionButton) findViewById(R.id.fabPlus)).setVisibility(View.VISIBLE);
        else ((FloatingActionButton) findViewById(R.id.fabPlus)).setVisibility(View.GONE);
    }

    void bottomSheetButtonCliked(){
        ((LinearLayout) findViewById(previousBottomSheetClickedItem)).setAlpha(1f);
        ((LinearLayout) findViewById(lastBottomSheetClickedItem)).setAlpha(0.4f);
    }

    void resetViewPosition() {
        CoordinatorLayout coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appBarLayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        /* int[] consumed = new int[2];
         * behavior.onNestedPreScroll(coordinator, appbar, null, 0, -1000, consumed);
         */
        behavior.onNestedFling(coordinator, appbar, null, 0, -1000, true);
    }

    private void setUpBottomSheet() {
        bottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomDrawer = BottomSheetBehavior.from(bottomSheet);

        final FloatingActionButton btnExpBottomSheet = (FloatingActionButton) findViewById(R.id.fabBottomSheet);
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

        initBottomDrawerButtons();
    }


    private void initBottomDrawerButtons() {
        ((LinearLayout) findViewById(R.id.bottom_sheet_info)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_notes)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_inventory)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_need_list)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_items)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_map)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_xat)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.bottom_sheet_group)).setOnClickListener(this);
    }

    public void setCurrentFragment (String TAG){
        currentFragment = TAG;
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, currentFragment);
        if (currentFragment.equals(
                EditItemFragment.class.getSimpleName())) {
            ((EditItemFragment) getFragmentManager().findFragmentById(R.id.DrawerLayout)).onBackPressed();
        }
        else if (currentFragment.equals(
                EditMaterialFragment.class.getSimpleName())) {
            ((EditMaterialFragment) getFragmentManager().findFragmentById(R.id.DrawerLayout)).onBackPressed();
        }
        else if (currentFragment.equals(
                EditNoteFragment.class.getSimpleName())) {
            ((EditNoteFragment) getFragmentManager().findFragmentById(R.id.DrawerLayout)).onBackPressed();
        } else {
            back();
        }
    }

    public void back() {
        if (currentFragment.equals(EditItemFragment.class.getSimpleName())) {
            Log.i(TAG, "Back from EditItemFragment");
            super.onBackPressed();
            ItemsFragment fragment = (ItemsFragment) getFragmentManager().findFragmentById(R.id.fragment_view);
            fragment.fillItemList();
        }
        else if (currentFragment.equals(EditMaterialFragment.class.getSimpleName())) {
            EditMaterialFragment parentFragment = (EditMaterialFragment) getFragmentManager().findFragmentById(R.id.DrawerLayout);
            if (parentFragment.parentFragment.equals(InventoryFragment.class.getSimpleName())) {
                Log.i(TAG, "Back from EditMaterialFragment (InventoriFragment)");
                super.onBackPressed();
                InventoryFragment fragment = (InventoryFragment) getFragmentManager().findFragmentById(R.id.fragment_view);
                fragment.getInventory();
            } else if (parentFragment.parentFragment.equals(NeedListFragment.class.getSimpleName())) {
                Log.i(TAG, "Back from EditMaterialFragment (NeedListFragment)");
                super.onBackPressed();
                NeedListFragment fragment = (NeedListFragment) getFragmentManager().findFragmentById(R.id.fragment_view);
                fragment.getNeedList();
            }
        }
        else if (currentFragment.equals(EditNoteFragment.class.getSimpleName())) {
            Log.i(TAG, "Back from EditNoteFragment");
            super.onBackPressed();
            NotesFragment fragment = (NotesFragment) getFragmentManager().findFragmentById(R.id.fragment_view);
            fragment.getNotes();
        } else {
            super.onBackPressed();
        }
    }
}

