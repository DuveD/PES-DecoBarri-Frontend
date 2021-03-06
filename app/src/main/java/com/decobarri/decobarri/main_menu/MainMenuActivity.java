package com.decobarri.decobarri.main_menu;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.decobarri.decobarri.BaseActivity;
import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.TabLayoutPagerAdapter;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainMenuActivity extends BaseActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        viewPager = (ViewPager) findViewById(R.id.MainMenuViewPager);
        viewPager.setOffscreenPageLimit(2);

        setUpTabLayout();
    }

    private void setUpTabLayout() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.MainMenuTabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Projects").setIcon(R.drawable.ic_world));
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.tabLayout_selected_item_color), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setText("My projects").setIcon(R.drawable.ic_project));
        tabLayout.getTabAt(1).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.tabLayout_unselected_item_color), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setText("Materials").setIcon(R.drawable.ic_box));
        tabLayout.getTabAt(2).getIcon().setColorFilter(ContextCompat.getColor(this, R.color.tabLayout_unselected_item_color), PorterDuff.Mode.SRC_IN);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Set up the ViewPager with the sections adapter.
        final TabLayoutPagerAdapter adapter = new TabLayoutPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabLayout_selected_item_color), PorterDuff.Mode.SRC_IN);
                View focus = getCurrentFocus();
                if (focus != null) {
                    hiddenKeyboard(focus);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) ((GlobalMaterialsFragment)getFragmentManager().findFragmentById(R.id.MainMenuViewPager)).stopUpdatingAnimation();
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabLayout_unselected_item_color), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
