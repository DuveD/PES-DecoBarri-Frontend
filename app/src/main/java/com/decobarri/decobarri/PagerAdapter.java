package com.decobarri.decobarri;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
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
                MainMenu_ProjectSearch defaultTab = new MainMenu_ProjectSearch();
                return defaultTab;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
