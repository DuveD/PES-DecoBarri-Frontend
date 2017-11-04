package com.decobarri.decobarri.project_menu;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.decobarri.decobarri.BaseActivity;
import com.decobarri.decobarri.R;

public class ProjectMenuActivity extends BaseActivity implements View.OnClickListener {

    private BottomSheetBehavior bottomDrawer;
    private LinearLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
        startMainFragment();
        setUpBottomSheet();
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
        switch (view.getId()) {
            case R.id.bottom_sheet_info: default:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof InfoFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new InfoFragment());
                break;
            case R.id.bottom_sheet_notes:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof NotesFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new NotesFragment());
                break;
            case R.id.bottom_sheet_inventory:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof InventoryFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new InventoryFragment());
                break;
            case R.id.bottom_sheet_need_list:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof NeedListFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new NeedListFragment());
                break;
            case R.id.bottom_sheet_items:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof ItemsFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new ItemsFragment());
                break;
            case R.id.bottom_sheet_map:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof MapFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new MapFragment());
                break;
            case R.id.bottom_sheet_xat:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof XatFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new XatFragment());
                break;
            case R.id.bottom_sheet_participants:
                if (!(getFragmentManager().findFragmentById(R.id.ProjectMenuLayout) instanceof ParticipantsFragment))
                    transaction.replace(R.id.ProjectMenuLayout, new ParticipantsFragment());
                break;
        }
        this.bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
        transaction.commit();
    }

    private void startMainFragment(){
        // Begin the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.ProjectMenuLayout,  new InfoFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        transaction.commit();
    }

    private void setUpBottomSheet() {
        bottomSheet = (LinearLayout) findViewById(R.id.bottomSheet);
        bottomDrawer = BottomSheetBehavior.from(bottomSheet);

        final ImageButton btnExpBottomSheet = (ImageButton) findViewById(R.id.btnExpBottomSheet);
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
        ((LinearLayout) findViewById(R.id.bottom_sheet_participants)).setOnClickListener(this);
    }
}

