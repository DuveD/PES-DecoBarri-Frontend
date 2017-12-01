package com.decobarri.decobarri.main_menu;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.activity_resources.Projects.AllProjectsAdapter;
//import com.decobarri.decobarri.activity_resources.Projects.Project;
import com.decobarri.decobarri.db_resources.MaterialsInterface;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectsSearchFragment extends Fragment {
    List items = new ArrayList();
    private RecyclerView rec;
    private AllProjectsAdapter adapter;
    private RecyclerView.LayoutManager lmanager;

    private SearchView searchView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rec = (RecyclerView) getView().findViewById(R.id.projectsearch_recycler);

        fillList();
        /*items.add(new Project((BitmapFactory.decodeResource(getResources(),
                R.drawable.example_vallespir)), "Decoracio carrer Vallespi",
                "Aquest any tornarem a participar a" +
                        " les festes de sants."));
        items.add(new Project((BitmapFactory.decodeResource(getResources(),
                R.drawable.example_christmas_school)),
                "Projecte de decoració de Nadal de l'escola Les Corts", "Decoració " +
                "ambientada en temàtica de nadal per als nens fins a 3r de primaria."));
        items.add(new Project(BitmapFactory.decodeResource(getResources(),
                R.drawable.example_festes_esplugues),
                "Festes d' Esplugues", "Col·labora a fer millor les festes" +
                " del nostre barri i participa en la organització de les seves activitats."));
        items.add(new Project(BitmapFactory.decodeResource(getResources(),
                R.drawable.example_street_gracia),
                "Decoració del carrer Rossend Arús", "Decorarem el nostre carrer" +
                " amb l'objectiu de tornar a quedar com el millor carrer de les festes de gràcia, tal com vam aconseguir l'any passat."));
        items.add(new Project(BitmapFactory.decodeResource(getResources(),
                R.drawable.example_christmas_centre_cultural),
                "Decoració temàtica de Nadal del centre cultural Les Corts.", "" +
                " Ajuda a decorar el nostre centre i participa en els events que tenim preparats per aquest nadal."));*/
        setView();
    }


    private void fillList() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder
                .client(httpClient.build())
                .build();
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
