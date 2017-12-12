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
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;
import com.decobarri.decobarri.activity_resources.Materials.MaterialAdapter;
import com.decobarri.decobarri.project_menu.edit_items.EditMaterialActivity;

public class NeedListFragment extends Fragment implements View.OnClickListener {

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
        View view = inflater.inflate(R.layout.fragment_project_need_list, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Need list");
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
        recyclerView = (RecyclerView) getView().findViewById(R.id.need_list_recycler);
        emptyView = (LinearLayout) getView().findViewById(R.id.empty_need_list_layout);
        setContentView();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload_menu_gray, optionsMenu);
        menu = optionsMenu;

        if (ProjectMenuActivity.getUpdatingNeedList())
            startUpdatingAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getNeedList();
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
            adapter = new MaterialAdapter(
                    ((ProjectMenuActivity) this.getActivity()).getNeedsList(),
                    recyclerView,
                    getActivity(),
                    Const.NEED_LIST_MATERIAL){

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
        if (((ProjectMenuActivity) this.getActivity()).needListIsEmpty()) {
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
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem != null && menuItem.getActionView() == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout iv = (RelativeLayout) inflater.inflate(R.layout.ic_refresh_gray, null);
                Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                menuItem.setActionView(iv);
            }
        }
    }

    public void stopUpdatingAnimation() {
        // Get our refresh item from the menu if it are initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem != null && menuItem.getActionView() != null) {
                // Remove the animation.
                menuItem.getActionView().clearAnimation();
                menuItem.setActionView(null);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void getNeedList(){
        (new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute(){
                System.out.println("Loading Need List...");
                startUpdatingAnimation();
                ProjectMenuActivity.setUpdatingNeedList(true);
            }
            @Override
            protected Void doInBackground(Void... voids) {
                ((ProjectMenuActivity)getActivity()).fillNeedList();
                return null;
            }
            @Override
            public void onPostExecute( Void nope ) {
                setContentView();
                ProjectMenuActivity.setUpdatingNeedList(false);
                stopUpdatingAnimation();
                System.out.println("Done");
            }
        }).execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), EditMaterialActivity.class);
        intent.putExtra(Const.FROM, Const.NEED_LIST_MATERIAL);
        intent.putExtra(Const.ID, "ITEM_ID");
        this.startActivity(intent);
    }
}
