package com.decobarri.decobarri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.decobarri.decobarri.drawe_menu.AccountSettingsActivity;
import com.decobarri.decobarri.drawe_menu.ChatActivity;
import com.decobarri.decobarri.drawe_menu.ContactListActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open_description, R.string.navigation_drawer_close_description){
            @Override
            public void onDrawerOpened(View draweView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (itemSelected != null) {
                    switch (itemSelected) {
                        case R.id.my_account:
                            startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class));
                            break;
                        case R.id.chat:
                            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                            break;
                        case R.id.contact_list:
                            startActivity(new Intent(getApplicationContext(), ContactListActivity.class));
                            break;
                        case R.id.logout:
                            SharedPreferences settings = getSharedPreferences("LOGGED_USER", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username", "");
                            editor.putString("password", "");
                            editor.apply();
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            break;
                        default:
                            break;
                    }
                    itemSelected = null;
                }
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (onBackPressedStatement()) {
        } else {
            super.onBackPressed();
        }
    }

    public Boolean onBackPressedStatement() {
        return false;
    }

    private Integer itemSelected;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        itemSelected = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    // OTHER FUNCTIONS

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 60, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }
}
