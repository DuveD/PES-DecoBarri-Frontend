package com.decobarri.decobarri.project_menu;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
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
import com.decobarri.decobarri.activity_resources.Item;
import com.decobarri.decobarri.activity_resources.ItemAdapter;
import com.decobarri.decobarri.project_menu.edit_items.EditItemActivity;

import java.util.ArrayList;

public class ItemsFragment extends Fragment implements View.OnClickListener {

    private Adapter adapter;
    private LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.fabPlus).setOnClickListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_items, container, false);
        getActivity().setTitle("Items");
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

        recyclerView = (RecyclerView) getView().findViewById(R.id.item_recycler);
        emptyView = (LinearLayout) getView().findViewById(R.id.empty_item_layout);

        setContentView();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload_menu, optionsMenu);
        menu = optionsMenu;

        if (ProjectMenuActivity.getUpdatingItemList())
            startUpdatingAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setContentView() {
        if (isVisible()) {
            setVisibleList();

            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ItemAdapter(
                    ((ProjectMenuActivity) this.getActivity()).getItemList(),
                    recyclerView,
                    getActivity()){

                @Override
                public void customNotifyDataSetChanged(){
                    setVisibleList();
                    super.customNotifyDataSetChanged();
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void setVisibleList() {
        if (((ProjectMenuActivity) this.getActivity()).itemsIsEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
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
    public void getItems(){
        (new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute(){
                System.out.println("Loading Items...");
                startUpdatingAnimation();
                ProjectMenuActivity.setUpdatingItemList(true);
            }
            @Override
            protected Void doInBackground(Void... voids) {
                ((ProjectMenuActivity)getActivity()).fillItemList();
                return null;
            }
            @Override
            public void onPostExecute( Void nope ) {
                setContentView();
                ProjectMenuActivity.setUpdatingItemList(false);
                stopUpdatingAnimation();
                System.out.println("Done");
            }
        }).execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), EditItemActivity.class);
        this.startActivity(intent);
    }
}
