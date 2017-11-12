package com.decobarri.decobarri.main_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
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
import com.decobarri.decobarri.R.drawable;
import com.decobarri.decobarri.R.id;
import com.decobarri.decobarri.R.layout;
import com.decobarri.decobarri.activity_resources.Material;
import com.decobarri.decobarri.activity_resources.MaterialAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GlobalMaterialsFragment extends Fragment {

    private Adapter adapter;
    private LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private ArrayList<Material> contentList;
    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activamos el menú superior para el reload
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_global_materials, container, false);

        // En principio no hacemos nada

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Recojemos y guardamos la view del fragment actual
        recyclerView = (RecyclerView) getView().findViewById(R.id.global_materials_recycler);
        emptyView = (LinearLayout) getView().findViewById(R.id.empty_global_materials_layout);

        // Rellenamos la lista con nada y asignamos el adaptador, pero esto no ahce nada en realidad...
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        contentList = new ArrayList<>();
        adapter = new MaterialAdapter(contentList, recyclerView);
        recyclerView.setAdapter(adapter);

        // Recargamos la lista en background y actualizamos la vista
        reloadAsyncTask();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload_menu, optionsMenu);
        menu = optionsMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.action_refresh:

                // Do animation start
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout iv = (RelativeLayout)inflater.inflate(R.layout.ic_refresh, null);
                Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                item.setActionView(iv);

                // Recargamos la lista en background y actualizamos la vista
                System.out.println("Selected: refresh");
                reloadAsyncTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void resetUpdatingAnimation() {
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

    // Recargamos nuestro ArrayList con el contenido actualizado con llamadas a servidor
    public void fillContentList() {
        /* examples */
        /* examples */
        /* examples */
        contentList = new ArrayList<>();
        contentList.clear();
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_sillas),
                "Sillas",
                "Sillas sobrantes",
                true,
                5,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_botellas),
                "Botellas",
                "Botellas sobrantes",
                false,
                5,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_cables),
                "Cables",
                "Cables sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_cajas),
                "Cajas",
                "Cajas Grandes",
                false,
                20,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_herramientas),
                "Herramientas",
                "Herramientas sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_neumaticos),
                "Neumaticos",
                "Neumaticos sobrantes",
                true,
                4,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_pinturas),
                "Pinturas",
                "Pinturas roja, azul, verde y más...",
                true,
                0,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_piscina),
                "Piscina",
                "Piscina hinchable pequeña",
                true,
                1,
                "C/Exemple nº123"));
        contentList.add(new Material(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_porexpan),
                "Porexpan",
                "Cuanto más grande mejor",
                false,
                0,
                "C/Exemple nº123"));
        /* /examples */
        /* /examples */
        /* /examples */

        Collections.sort(contentList, new Comparator<Material>() {
            @Override
            public int compare(Material materialA, Material materialB) {
                int boolean_compare = Boolean.compare(materialB.isUrgent(), materialA.isUrgent());
                if (boolean_compare == 0)
                    return materialA.getName().compareToIgnoreCase(materialB.getName());
                else return boolean_compare;
            }
        });
    }

    private void setContentView() {
        if (contentList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MaterialAdapter(contentList, recyclerView);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // Con estos métodos, crearemos una Tarea asíncrona que llamará al método de recargar información
    // y luego nos recargará la lista de la view
    @SuppressLint("StaticFieldLeak")
    private void reloadAsyncTask(){
        (new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                fillContentList();
                System.out.println("Filled Global Materials");
                return null;
            }
            @Override
            public void onPostExecute( Void nope ) {
                setContentView();
                System.out.println("Refreshed adapter");

                // Reset animation
                resetUpdatingAnimation();
            }
        }).execute();
    }
}
