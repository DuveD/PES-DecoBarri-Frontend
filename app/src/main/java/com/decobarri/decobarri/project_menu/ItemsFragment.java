package com.decobarri.decobarri.project_menu;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.activity_resources.Items.ItemAdapter;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.project_menu.edit_items.EditItemFragment;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemsFragment extends Fragment {

    private Adapter adapter;
    private LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private Menu menu;
    private List<Item> itemList;
    private String projectId;

    private Retrofit retrofit;

    private static final String TAG = ItemsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.fabPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.DrawerLayout, EditItemFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        initVars();
        super.onCreate(savedInstanceState);
    }

    private void initVars() {
        projectId = ((ProjectMenuActivity)this.getActivity()).projectID;
        retrofit = ((ProjectMenuActivity)this.getActivity()).retrofit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_items, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Items");
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);

        itemList = new ArrayList<>();
        fillItemList();
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
        inflater.inflate(R.menu.reload_menu_gray, optionsMenu);
        menu = optionsMenu;
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
            adapter = new ItemAdapter(itemList,recyclerView, getActivity()){

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
        if (itemList.isEmpty()) {
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
            }
            @Override
            protected Void doInBackground(Void... voids) {
                fillItemList();
                return null;
            }
            @Override
            public void onPostExecute( Void nope ) {
                setContentView();
                stopUpdatingAnimation();
                System.out.println("Done");
            }
        }).execute();
    }

    public void fillItemList() {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
        Retrofit retrofit = builder.build();
        ProjectClient client = retrofit.create(ProjectClient.class);

            Call<List<Item>> call = client.GetItems(projectId);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    itemList = response.body();

                    Collections.sort(itemList, new Comparator<Item>() {
                        @Override
                        public int compare(Item itemA, Item itemB) {
                            return itemA.getName().compareToIgnoreCase(itemB.getName());
                        }
                    });

                    adapter.notifyDataSetChanged();
                }
                else {
                    System.out.println("Error code: " + response.code());
                    System.out.println("Error msg: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });

    }
}
