package com.decobarri.decobarri.project_menu;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.decobarri.decobarri.BaseActivity;
import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Item;
import com.decobarri.decobarri.activity_resources.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProjectMenuActivity extends BaseActivity implements View.OnClickListener {

    private BottomSheetBehavior bottomDrawer;
    private LinearLayout bottomSheet;

    private ArrayList<Material> inventoryList;
    private static Boolean updatingInventoryList;
    private ArrayList<Material> needList;
    private static Boolean updatingNeedList;
    private ArrayList<Item> itemList;
    private static Boolean updatingItemList;

    private int previousBottomSheetClickedItem;
    private int lastBottomSheetClickedItem;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
        initVars();
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
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof InventoryFragment))
                    transaction.replace(R.id.fragment_view, new InventoryFragment());
                break;
            case R.id.bottom_sheet_need_list:
                if (!(getFragmentManager().findFragmentById(R.id.fragment_view) instanceof NeedListFragment))
                    transaction.replace(R.id.fragment_view, new NeedListFragment());
                break;
            case R.id.bottom_sheet_items:
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

        final FloatingActionButton btnExpBottomSheet = (FloatingActionButton) findViewById(R.id.btnExpBottomSheet);
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
    public ArrayList<Material> getInventoryList() { return inventoryList; }
    public Boolean inventoriIsEmpty() { return inventoryList.isEmpty(); }

    public static Boolean getUpdatingNeedList() { return updatingNeedList; }
    public static void setUpdatingNeedList(Boolean updating) { updatingNeedList = updating; }
    public ArrayList<Material> getNeedsList() { return needList; }
    public Boolean needListIsEmpty() { return needList.isEmpty(); }

    public static Boolean getUpdatingItemList() { return updatingItemList; }
    public static void setUpdatingItemList(Boolean updating) { updatingItemList = updating; }
    public ArrayList<Item> getItemList() { return itemList; }
    public Boolean itemsIsEmpty() { return itemList.isEmpty(); }



    // Recargamos nuestro ArrayList con el contenido actualizado con llamadas a servidor
    public void fillInvetoryList() {
        /* examples */
        /* examples */
        /* examples */
        inventoryList.clear();
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_sillas),"InventoryItemA","Sillas sobrantes",true,5,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_botellas),"InventoryItemB","Botellas sobrantes",false,5,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_cables),"InventoryItemC","Cables sobrantes",false,0,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_cajas),"InventoryItemD","Cajas Grandes",false,20,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_herramientas),"InventoryItemE","Herramientas sobrantes",false,0,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_neumaticos),"InventoryItemF","Neumaticos sobrantes",true,4,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_pinturas),"InventoryItemG","Pinturas roja, azul, verde y más...",true,0,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_piscina),"InventoryItemH","Piscina hinchable pequeña",true,1,"C/Exemple nº123"));
        inventoryList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_porexpan),"InventoryItemI","Cuanto más grande mejor",false,0,"C/Exemple nº123"));
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

    // Recargamos nuestro ArrayList con el contenido actualizado con llamadas a servidor
    public void fillNeedList() {
        /* examples */
        /* examples */
        /* examples */
        needList.clear();
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_sillas),"NeedListItemA","Sillas sobrantes",true,5,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_botellas),"NeedListItemB","Botellas sobrantes",false,5,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_cables),"NeedListItemC","Cables sobrantes",false,0,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_cajas),"NeedListItemD","Cajas Grandes",false,20,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_herramientas),"NeedListItemE","Herramientas sobrantes",false,0,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_neumaticos),"NeedListItemF","Neumaticos sobrantes",true,4,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_pinturas),"NeedListItemG","Pinturas roja, azul, verde y más...",true,0,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_piscina),"NeedListItemH","Piscina hinchable pequeña",true,1,"C/Exemple nº123"));
        needList.add(new Material(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_porexpan),"NeedListItemI","Cuanto más grande mejor",false,0,"C/Exemple nº123"));
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
        itemList.add(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.example_item_banco_palets),"Banco","Bancos hechos de palets","Null",new ArrayList<String>()));
        itemList.add(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.example_item_cortina_bolsa),"Cortina","Cortinas hechas con bolsas de basura","Null", new ArrayList<String>()));
        itemList.add(new Item(BitmapFactory.decodeResource(getResources(), R.drawable.example_item_flor_botella),"Flor","Flores hechas con botellas recicladas","Null",new ArrayList<String>()));
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

