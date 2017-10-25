package com.decobarri.decobarri;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.decobarri.decobarri.ActivityResources.tabLayoutPagerAdapter;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        viewPager = (ViewPager) findViewById(R.id.MainMenuViewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.MainMenuToolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Bottom Sheets");
        drawerLayout = (DrawerLayout) findViewById(R.id.MainMenuDrawerLayout);


        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the tabs for ViewPager
        tabLayout = (TabLayout) findViewById(R.id.MainMenuTabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Projects").setIcon(R.drawable.tab_project_search_icon));
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.tabSelectedColor), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setText("My projects").setIcon(R.drawable.tab_my_projects_icon));
        tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.tabUnselectedColor), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setText("Materials").setIcon(R.drawable.tab_wanted_materials_icon));
        tabLayout.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.tabUnselectedColor), PorterDuff.Mode.SRC_IN);

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.MainMenuNavigationView);
        navigationView.setNavigationItemSelectedListener(this);

        // Set up the ViewPager with the sections adapter.
        final tabLayoutPagerAdapter adapter = new tabLayoutPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabSelectedColor), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabUnselectedColor), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate()
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.MainMenuDrawerLayout);
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
            CharSequence textt = "Selected: Logout";
            Toast toast = Toast.makeText(this, textt, Toast.LENGTH_SHORT);
            toast.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.MainMenuDrawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
