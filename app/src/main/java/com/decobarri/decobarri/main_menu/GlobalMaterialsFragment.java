package com.decobarri.decobarri.main_menu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
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

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.R.layout;
import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.activity_resources.Materials.MaterialAdapter;
import com.decobarri.decobarri.db_resources.MaterialsClient;
import com.decobarri.decobarri.db_resources.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GlobalMaterialsFragment extends Fragment {

    private Adapter adapter;
    private LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private Menu menu;

    private ArrayList globalMaterialList;
    private static Boolean updatingGlobalMaterialList;
    private static final String TAG = GlobalMaterialsFragment.class.getSimpleName();
    private Retrofit retrofit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initVars();
    }

    private void initVars() {
        globalMaterialList = new ArrayList<>();
        retrofit = ((MainMenuActivity)this.getActivity()).retrofit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_global_materials, container, false);
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
        recyclerView = (RecyclerView) getView().findViewById(R.id.global_materials_recycler);
        emptyView = (LinearLayout) getView().findViewById(R.id.empty_global_materials_layout);
        fillContentList();
        setContentView();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload_menu, optionsMenu);
        menu = optionsMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                fillContentList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setContentView() {
        setVisibleList();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MaterialAdapter(
                globalMaterialList,
                recyclerView,
                getActivity(),
                TAG){

            @Override
            public void customNotifyDataSetChanged(){
                setVisibleList();
                super.customNotifyDataSetChanged();
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setVisibleList() {
        if (globalMaterialList.isEmpty()) {
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
                RelativeLayout iv = (RelativeLayout) inflater.inflate(R.layout.ic_refresh, null);
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

    // Recargamos nuestro ArrayList con el contenido actualizado con llamadas a servidor
    public void fillContentList() {
        updatingGlobalMaterialList = true;
        startUpdatingAnimation();

        MaterialsClient client = retrofit.create(MaterialsClient.class);
        Call<List<MaterialsClient.wantListPairs>> call = client.contentList();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<MaterialsClient.wantListPairs>>() {
            @Override
            public void onResponse(Call<List<MaterialsClient.wantListPairs>> call, Response<List<MaterialsClient.wantListPairs>> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());
                    globalMaterialList = new ArrayList<>();
                    for (MaterialsClient.wantListPairs project: response.body()) {
                        List<Material> materialList = project.materials;
                        for (Material material : materialList) { material.setAddress(project.project.getName()); }
                        globalMaterialList.addAll(project.materials);
                    }
                    Collections.sort(globalMaterialList, new Comparator<Material>() {
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
                updatingGlobalMaterialList = false;
                stopUpdatingAnimation();
            }

            @Override
            public void onFailure(Call<List<MaterialsClient.wantListPairs>> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

                updatingGlobalMaterialList = false;
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
}
