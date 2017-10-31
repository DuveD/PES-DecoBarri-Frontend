package com.decobarri.decobarri.project_menu;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.decobarri.decobarri.Login;
import com.decobarri.decobarri.R;

public class ProjectMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ImageButton btnExpBottomSheet;
    private LinearLayout bottomSheet;
    private DrawerLayout drawerLayout;
    private BottomSheetBehavior bottomDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
        startNotesFragment();

        //viewPager = (ViewPager) findViewById(R.id.ProjectMenuViewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.ProjectMenuToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Bottom Sheets");
        drawerLayout = (DrawerLayout) findViewById(R.id.ProjectMenuDrawerLayout);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.ProjectMenuNavigationView);
        navigationView.setNavigationItemSelectedListener(this);

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
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ProjectMenuDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (bottomDrawer.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_account) {
            System.out.println("Selected: My account");
        } else if (id == R.id.chat) {
            System.out.println("Selected: Chat");
        } else if (id == R.id.contact_list) {
            System.out.println("Selected: Contact list");
        } else if (id == R.id.logout) {

            SharedPreferences settings = getSharedPreferences("LOGGED_USER", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.apply();

            Intent i = new Intent(this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ProjectMenuDrawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
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
        }
        transaction.addToBackStack(null);
        transaction.commit();
        bottomDrawer.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void startNotesFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        NotesFragment notesFragment = new NotesFragment();
        transaction.replace(R.id.ProjectMenuLayout,notesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

