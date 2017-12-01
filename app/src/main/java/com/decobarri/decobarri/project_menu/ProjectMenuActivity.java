package com.decobarri.decobarri.project_menu;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.decobarri.decobarri.BaseActivity;
import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.activity_resources.Materials.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectMenuActivity extends BaseActivity implements View.OnClickListener {

    private BottomSheetBehavior bottomDrawer;
    private LinearLayout bottomSheet;

    private List<Material> inventoryList;
    private static Boolean updatingInventoryList;
    private List<Material> needList;
    private static Boolean updatingNeedList;
    private List<Item> itemList;
    private static Boolean updatingItemList;

    private int previousBottomSheetClickedItem;
    private int lastBottomSheetClickedItem;

    public String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
        initVars();
        startMainFragment();
        setUpBottomSheet();

        Bundle arg = getIntent().getExtras();
        projectId = arg.getString("project", "");
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
        inventoryList = new ArrayList<>();
        updatingInventoryList = false;
        needList = new ArrayList<>();
        updatingNeedList = false;
        itemList = new ArrayList<>();
        updatingItemList = false;
        fillInvetoryList();
        fillNeedList();
        fillItemList();

        // Initialized by default
        previousBottomSheetClickedItem = R.id.bottom_sheet_info;
        lastBottomSheetClickedItem = R.id.bottom_sheet_info;
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
        activePlusFloatingButton(false);
        previousBottomSheetClickedItem = lastBottomSheetClickedItem;
        lastBottomSheetClickedItem = view.getId();
        switch (lastBottomSheetClickedItem) {
            case R.id.bottom_sheet_info: default:
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof InfoFragment))
                    transaction.replace(R.id.fragment_view, new InfoFragment());
                break;
            case R.id.bottom_sheet_notes:
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof NotesFragment))
                    transaction.replace(R.id.fragment_view, new NotesFragment());
                break;
            case R.id.bottom_sheet_inventory:
                activePlusFloatingButton(true);
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof InventoryFragment))
                    transaction.replace(R.id.fragment_view, new InventoryFragment());
                break;
            case R.id.bottom_sheet_need_list:
                activePlusFloatingButton(true);
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof NeedListFragment))
                    transaction.replace(R.id.fragment_view, new NeedListFragment());
                break;
            case R.id.bottom_sheet_items:
                activePlusFloatingButton(true);
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof ItemsFragment))
                    transaction.replace(R.id.fragment_view, new ItemsFragment());
                break;
            case R.id.bottom_sheet_map:
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof MapFragment))
                    transaction.replace(R.id.fragment_view, new MapFragment());
                break;
            case R.id.bottom_sheet_xat:
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof XatFragment))
                    transaction.replace(R.id.fragment_view, new XatFragment());
                break;
            case R.id.bottom_sheet_group:
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof ParticipantsFragment))
                    transaction.replace(R.id.fragment_view, new ParticipantsFragment());
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

    private void startMainFragment(){
        // Begin the transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_view,  new InfoFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        transaction.commit();
        bottomSheetButtonCliked();
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

    public static Boolean getUpdatingInventoryList() { return updatingInventoryList; }
    public static void setUpdatingInventoryList(Boolean updating) { updatingInventoryList = updating; }
    public List<Material> getInventoryList() { return inventoryList; }
    public Boolean inventoryIsEmpty() { return inventoryList.isEmpty(); }

    public static Boolean getUpdatingNeedList() { return updatingNeedList; }
    public static void setUpdatingNeedList(Boolean updating) { updatingNeedList = updating; }
    public List<Material> getNeedsList() { return needList; }
    public Boolean needListIsEmpty() { return needList.isEmpty(); }

    public static Boolean getUpdatingItemList() { return updatingItemList; }
    public static void setUpdatingItemList(Boolean updating) { updatingItemList = updating; }
    public List<Item> getItemList() { return itemList; }
    public Boolean itemsIsEmpty() { return itemList.isEmpty(); }

    public void fillInvetoryList() {
        /* examples */
        /* examples */
        /* examples */
        inventoryList.clear();
        inventoryList.add(new Material("1", "http://i.imgur.com/I86rTVl.jpg","InventoryItemA","Sillas sobrantes",true,5,"C/Exemple nº123"));
        inventoryList.add(new Material("2", "http://i.imgur.com/I86rTVl.jpg","InventoryItemB","Botellas sobrantes",false,5,"C/Exemple nº123"));
        inventoryList.add(new Material("3", "http://i.imgur.com/I86rTVl.jpg","InventoryItemC","Cables sobrantes",false,0,"C/Exemple nº123"));
        inventoryList.add(new Material("4", "http://i.imgur.com/I86rTVl.jpg","InventoryItemD","Cajas Grandes",false,20,"C/Exemple nº123"));
        inventoryList.add(new Material("5", "http://i.imgur.com/I86rTVl.jpg","InventoryItemE","Herramientas sobrantes",false,0,"C/Exemple nº123"));
        inventoryList.add(new Material("6", "http://i.imgur.com/I86rTVl.jpg","InventoryItemF","Neumaticos sobrantes",true,4,"C/Exemple nº123"));
        inventoryList.add(new Material("7", "http://i.imgur.com/I86rTVl.jpg","InventoryItemG","Pinturas roja, azul, verde y más...",true,0,"C/Exemple nº123"));
        inventoryList.add(new Material("8", "http://i.imgur.com/I86rTVl.jpg","InventoryItemH","Piscina hinchable pequeña",true,1,"C/Exemple nº123"));
        inventoryList.add(new Material("9", "http://i.imgur.com/I86rTVl.jpg","InventoryItemI","Cuanto más grande mejor",false,0,"C/Exemple nº123"));
        /* /examples */
        /* /examples */
        /* /examples */

        Collections.sort(inventoryList, new Comparator<Material>() {
            @Override
            public int compare(Material materialA, Material materialB) {
                int boolean_compare = Boolean.compare(materialB.isUrgent(), materialA.isUrgent());
                if (boolean_compare == 0)
                    return materialA.getName().compareToIgnoreCase(materialB.getName());
                else return boolean_compare;
            }
        });
    }

    public void fillNeedList() {
        /* examples */
        /* examples */
        /* examples */
        needList.clear();
        needList.add(new Material("1", "http://i.imgur.com/I86rTVl.jpg","NeedListItemA","Sillas sobrantes",true,5,"C/Exemple nº123"));
        needList.add(new Material("2", "http://i.imgur.com/I86rTVl.jpg","NeedListItemB","Botellas sobrantes",false,5,"C/Exemple nº123"));
        needList.add(new Material("3", "http://i.imgur.com/I86rTVl.jpg","NeedListItemC","Cables sobrantes",false,0,"C/Exemple nº123"));
        needList.add(new Material("4", "http://i.imgur.com/I86rTVl.jpg","NeedListItemD","Cajas Grandes",false,20,"C/Exemple nº123"));
        needList.add(new Material("5", "http://i.imgur.com/I86rTVl.jpg","NeedListItemE","Herramientas sobrantes",false,0,"C/Exemple nº123"));
        needList.add(new Material("6", "http://i.imgur.com/I86rTVl.jpg","NeedListItemF","Neumaticos sobrantes",true,4,"C/Exemple nº123"));
        needList.add(new Material("7", "http://i.imgur.com/I86rTVl.jpg","NeedListItemG","Pinturas roja, azul, verde y más...",true,0,"C/Exemple nº123"));
        needList.add(new Material("8", "http://i.imgur.com/I86rTVl.jpg","NeedListItemH","Piscina hinchable pequeña",true,1,"C/Exemple nº123"));
        needList.add(new Material("9", "http://i.imgur.com/I86rTVl.jpg","NeedListItemI","Cuanto más grande mejor",false,0,"C/Exemple nº123"));
        /* /examples */
        /* /examples */
        /* /examples */

        Collections.sort(needList, new Comparator<Material>() {
            @Override
            public int compare(Material materialA, Material materialB) {
                int boolean_compare = Boolean.compare(materialB.isUrgent(), materialA.isUrgent());
                if (boolean_compare == 0)
                    return materialA.getName().compareToIgnoreCase(materialB.getName());
                else return boolean_compare;
            }
        });
    }

    public void fillItemList() {
        /* examples */
        /* examples */
        /* examples */
        itemList.clear();
        itemList.add(new Item("1", "http://i.imgur.com/I86rTVl.jpg","Banco","Bancos hechos de palets","Null",new ArrayList<String>()));
        itemList.add(new Item("2", "http://i.imgur.com/I86rTVl.jpg","Cortina","Cortinas hechas con bolsas de basura","Null", new ArrayList<String>()));
        itemList.add(new Item("3", "http://i.imgur.com/I86rTVl.jpg","Flor","Flores hechas con botellas recicladas","Null",new ArrayList<String>()));
        /* /examples */
        /* /examples */
        /* /examples */

        Collections.sort(itemList, new Comparator<Item>() {
            @Override
            public int compare(Item itemA, Item itemB) {
                return itemA.getName().compareToIgnoreCase(itemB.getName());
            }
        });
    }
}

