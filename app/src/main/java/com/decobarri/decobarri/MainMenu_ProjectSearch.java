package com.decobarri.decobarri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.db_resources.DB_library;

public class MainMenu_ProjectSearch extends Fragment {
    private DB_library httpDBlibrary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_project_search, container, false);

        // BD_library init wiht activity context
        httpDBlibrary = new DB_library( this.getActivity() );

        /* test */
        /* test */
        /* examples */
        onCreateTest( view );
        /* test */
        /* test */
        /* test */

        return view;
    }

    public void onCreateTest( View view ){
        String call = "";
        String result = "You are on tab_fragment_project_search"/*httpDBlibrary.db_call( call )*/;

        Toast toast = Toast.makeText(view.getContext(), result, Toast.LENGTH_SHORT);
        toast.show();
    }
}
