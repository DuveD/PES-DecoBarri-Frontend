package com.decobarri.decobarri;

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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ProjectMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Boolean SheetExpanded = false;
    private ImageButton btnExpBottomSheet;
    private LinearLayout bottomSheet;
    //private ViewPager viewPager;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);

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
        final BottomSheetBehavior bsb = BottomSheetBehavior.from(bottomSheet);

        btnExpBottomSheet = (ImageButton) findViewById(R.id.btnExpBottomSheet);
        btnExpBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SheetExpanded)
                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                else
                    bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        bsb.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    SheetExpanded = !SheetExpanded;
                    float rotation = (btnExpBottomSheet.getRotation() == 180F) ? 0F : 180F;
                    btnExpBottomSheet.animate().rotation(rotation).setInterpolator(new AccelerateDecelerateInterpolator());
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ProjectMenuDrawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            CharSequence textt = "Selected: My account";
            Toast toast = Toast.makeText(this, textt, Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.chat) {
            CharSequence textt = "Selected: Chat";
            Toast toast = Toast.makeText(this, textt, Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.contact_list) {
            CharSequence textt = "Selected: Contact list";
            Toast toast = Toast.makeText(this, textt, Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.logout) {

            SharedPreferences settings = getSharedPreferences("LOGGED_USER", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.apply();

            Intent i = new Intent(this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.ProjectMenuDrawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

