package com.decobarri.decobarri;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    static final int NUM_ITEMS = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MainMenu_ProjectSearch tab1 = new MainMenu_ProjectSearch();
                return tab1;
            case 1:
                MainMenu_MyProjects tab2 = new MainMenu_MyProjects();
                return tab2;
            case 2:
                MainMenu_GlobalMaterials tab3 = new MainMenu_GlobalMaterials();
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
