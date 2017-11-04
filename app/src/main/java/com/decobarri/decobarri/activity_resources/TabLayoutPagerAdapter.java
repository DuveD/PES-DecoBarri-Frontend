package com.decobarri.decobarri.activity_resources;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.decobarri.decobarri.main_menu.GlobalMaterialsFragment;
import com.decobarri.decobarri.main_menu.MyProjectsFragment;
import com.decobarri.decobarri.main_menu.ProjectSearchFragment;

public class TabLayoutPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_ITEMS = 3;

    public TabLayoutPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProjectSearchFragment tab1 = new ProjectSearchFragment();
                return tab1;
            case 1:
                MyProjectsFragment tab2 = new MyProjectsFragment();
                return tab2;
            case 2:
                GlobalMaterialsFragment tab3 = new GlobalMaterialsFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
