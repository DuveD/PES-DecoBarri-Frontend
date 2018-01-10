package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
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
import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.activity_resources.Materials.MaterialAdapter;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.project_menu.edit_items.EditMaterialFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InventoryFragment extends Fragment {

    private Adapter adapter;
    private LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private Menu menu;
    private Retrofit retrofit;

    private List<Material> inventoryList;

    private String projectID;

    private static final String TAG = InventoryFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.fabPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.DrawerLayout, EditMaterialFragment.newInstance(TAG));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        super.onCreate(savedInstanceState);
        initVars();
    }

    private void initVars() {
        retrofit = ((ProjectMenuActivity)getActivity()).retrofit;
        projectID = ((ProjectMenuActivity)this.getActivity()).projectID;
        inventoryList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_inventory, container, false);
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Inventory");


        inventoryList = new ArrayList<>();
        // TODO: Uncomment
        //getInventory();
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
        inflater.inflate(R.menu.reload_menu_gray, optionsMenu);
        menu = optionsMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // TODO: Uncomment
                //getInventory();
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
                    inventoryList,
                    recyclerView,
                    getActivity(),
                    Const.INVENTORY_MATERIAL){

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
        if (inventoryList.isEmpty()) {
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

    public void getInventory() {
        startUpdatingAnimation();

        //TODO: ACABAR ESTA LLAMADA
        ProjectClient client = ((ProjectMenuActivity)this.getActivity()).retrofit.create(ProjectClient.class);
        Call<List<Material>> call = client.getInvetoryList();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<Material>>() {
            @Override
            public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());
                    inventoryList = response.body();
                    Collections.sort(inventoryList, new Comparator<Material>() {
                        @Override
                        public int compare(Material materialA, Material materialB) {
                            int boolean_compare = Boolean.compare(materialB.isUrgent(), materialA.isUrgent());
                            if (boolean_compare == 0)
                                return materialA.getName().compareToIgnoreCase(materialB.getName());
                            else return boolean_compare;
                        }
                    });
                    setContentView();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                }

                stopUpdatingAnimation();
            }

            @Override
            public void onFailure(Call<List<Material>> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

                stopUpdatingAnimation();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void notifyDataSetChangedOnAdapter(){
        adapter.notifyDataSetChanged();
    }
}
