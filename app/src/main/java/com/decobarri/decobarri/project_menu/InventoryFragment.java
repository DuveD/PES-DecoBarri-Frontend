package com.decobarri.decobarri.project_menu;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Material;
import com.decobarri.decobarri.activity_resources.MaterialAdapter;

import java.util.ArrayList;

public class InventoryFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private ArrayList<Material> contentList;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_inventory, container, false);
        getActivity().setTitle("Invetory");
        return view;
    }

    @Override
    public void onDestroyView() {
        stopUpdatingAnimation();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.inventory_recycler);
        emptyView = (LinearLayout) getView().findViewById(R.id.empty_inventory_layout);

        setContentView();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload_menu, optionsMenu);
        menu = optionsMenu;

        if (ProjectMenuActivity.getUpdatingInventoryList())
            startUpdatingAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getInventory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setContentView() {
        if (isVisible()) {
            if (((ProjectMenuActivity) this.getActivity()).inventoriIsEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }

            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            contentList = ((ProjectMenuActivity) this.getActivity()).getInventoryList();
            adapter = new MaterialAdapter(contentList, recyclerView);

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void startUpdatingAnimation() {
        // Get our refresh item from the menu if it are initialized
        if (menu != null) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout iv = (RelativeLayout)inflater.inflate(R.layout.ic_refresh, null);
            Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_refresh);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);
            menu.findItem(R.id.action_refresh).setActionView(iv);
        }
    }

    public void stopUpdatingAnimation() {
        // Get our refresh item from the menu if it are initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem.getActionView() != null) {
                // Remove the animation.
                menuItem.getActionView().clearAnimation();
                menuItem.setActionView(null);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getInventory(){
        (new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute(){
                System.out.println("Loading Inventory...");
                startUpdatingAnimation();
                ProjectMenuActivity.setUpdatingInventoryList(true);
            }
            @Override
            protected Void doInBackground(Void... voids) {
                ((ProjectMenuActivity)getActivity()).fillInvetoryList();
                return null;
            }
            @Override
            public void onPostExecute( Void nope ) {
                setContentView();
                ProjectMenuActivity.setUpdatingInventoryList(false);
                stopUpdatingAnimation();
                System.out.println("Done");
            }
        }).execute();
    }
}
