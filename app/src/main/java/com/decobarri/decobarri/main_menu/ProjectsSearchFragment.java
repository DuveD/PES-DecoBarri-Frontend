package com.decobarri.decobarri.main_menu;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Projects.AllProjectsAdapter;
//import com.decobarri.decobarri.activity_resources.Projects.Project;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProjectsSearchFragment extends Fragment {
    List items = new ArrayList();
    private RecyclerView rec;
    private AllProjectsAdapter adapter;
    private RecyclerView.LayoutManager lmanager;
    private SearchView searchView;
    private Retrofit retrofit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initVars();
    }

    private void initVars() {
        items = new ArrayList<>();
        retrofit = ((MainMenuActivity)this.getActivity()).retrofit;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rec = (RecyclerView) getView().findViewById(R.id.projectsearch_recycler);
        fillList();
        setView();
    }


    private void fillList() {
        ProjectClient client =  retrofit.create(ProjectClient.class);
        Call<List<Project>> call = client.FindAllProjects();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                // The network call was a success and we got a response
                if (response.isSuccessful()) {
                    items = response.body();
                    setView();
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                //
            }
        });
    }

    private void setView() {
        lmanager = new LinearLayoutManager(getActivity());
        rec.setLayoutManager(lmanager);
        adapter = new AllProjectsAdapter(items, getActivity(), rec);
        rec.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_projects_search, container, false);

        return view;
    }

    public void onCreateTest(View view) {
        String call = "";
        String result = "You are on fragment_my_projects"/*httpDBlibrary.db_call( call )*/;

        Toast toast = Toast.makeText(view.getContext(), result, Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, optionsMenu);

        MenuItem myActionMenuItem = optionsMenu.findItem(R.id.options_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Project> filteredModelList = filter(items, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }


        });
        super.onCreateOptionsMenu(optionsMenu, inflater);
    }


    /*@Override
    public boolean onQueryTextChange(String newText) {
        final List<Project> filteredModelList = filter(items, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }*/

    private List<Project> filter(List<Project>projects, String query) {
        query = query.toLowerCase();final List<Project> filteredModelList = new ArrayList<>();
        for (Project p : projects) {
            final String text = p.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(p);
            }
        }
        return filteredModelList;
    }


}
