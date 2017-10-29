package com.decobarri.decobarri;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.decobarri.decobarri.ActivityResources.GlobalMaterialListAdapter;
import com.decobarri.decobarri.ActivityResources.GlobalMaterialListItem;
import com.decobarri.decobarri.ActivityResources.AsyncCustomTask;
import com.decobarri.decobarri.R.drawable;
import com.decobarri.decobarri.R.id;
import com.decobarri.decobarri.R.layout;
import com.decobarri.decobarri.R.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainMenu_GlobalMaterials extends Fragment {

    private Adapter adapter;
    private LayoutManager lmanager;
    private RecyclerView recyclerView;
    private ArrayList<GlobalMaterialListItem> listContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Activamos el menú superior para el reload
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.tab_fragment_global_materials, container, false);

        // En principio no hacemos nada

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Recojemos y guardamos la view del fragment actual
        this.recyclerView = (RecyclerView) getView().findViewById(R.id.global_materials_listView);

        // Rellenamos la lista con nada y asignamos el adaptador, pero esto no ahce nada en realidad...
        lmanager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lmanager);
        this.listContent = new ArrayList<>();
        adapter = new GlobalMaterialListAdapter(listContent, recyclerView);
        recyclerView.setAdapter(adapter);

        // Recargamos la lista en background y actualizamos la vista
        reloadAsyncTask();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(menu.top_menu, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.action_refresh:

                // Recargamos la lista en background y actualizamos la vista
                System.out.println("Selected: refresh");
                reloadAsyncTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Recargamos nuestro ArrayList con el contenido actualizado con llamadas a servidor
    public void fillGlobalMaterialsList() {
        /* examples */
        /* examples */
        /* examples */
        this.listContent = new ArrayList<>();
        this.listContent.clear();
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_sillas),
                "Sillas",
                "Sillas sobrantes",
                true,
                5,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_botellas),
                "Botellas",
                "Botellas sobrantes",
                false,
                5,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_cables),
                "Cables",
                "Cables sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_cajas),
                "Cajas",
                "Cajas Grandes",
                false,
                20,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_herramientas),
                "Herramientas",
                "Herramientas sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_neumaticos),
                "Neumaticos",
                "Neumaticos sobrantes",
                true,
                4,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_pinturas),
                "Pinturas",
                "Pinturas roja, azul, verde y más...",
                true,
                0,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_piscina),
                "Piscina",
                "Piscina hinchable pequeña",
                true,
                1,
                "C/Exemple nº123"));
        this.listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_porexpan),
                "Porexpan",
                "Cuanto más grande mejor",
                false,
                0,
                "C/Exemple nº123"));
        /* /examples */
        /* /examples */
        /* /examples */

        Collections.sort(listContent, new Comparator<GlobalMaterialListItem>() {
            @Override
            public int compare(GlobalMaterialListItem materialA, GlobalMaterialListItem materialB) {
                int boolean_compare = Boolean.compare(materialB.is_urgent(), materialA.is_urgent());
                if (boolean_compare == 0)
                    return materialA.get_name().compareToIgnoreCase(materialB.get_name());
                else return boolean_compare;
            }
        });
    }

    // Con estos métodos, crearemos una Tarea asíncrona que llamará al método de recargar información
    // y luego nos recargará la lista de la view
    @SuppressLint("StaticFieldLeak")
    private void reloadAsyncTask(){
        (new AsyncCustomTask(getContext(), "Reload Global Materials List"){
            @Override
            public void doInBackgroundFunction() {
                //item.setEnabled(false);
                fillGlobalMaterialsList();
                System.out.println("Filled Global Materials");
            }
            @Override
            public void onPostExecuteFunction() {
                lmanager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(lmanager);

                adapter = new GlobalMaterialListAdapter(listContent, recyclerView);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //item.setEnabled(true);
                System.out.println("Refreshed adapter");
            }
        }).execute();
    }
}
