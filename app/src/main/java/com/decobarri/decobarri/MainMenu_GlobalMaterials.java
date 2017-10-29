package com.decobarri.decobarri;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

    private GlobalMaterialListAdapter adapter;
    private ListView listView;
    private ArrayList<GlobalMaterialListItem> listContent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        reloadAsyncTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.tab_fragment_global_materials, container, false);
        if (adapter != null) adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(id.global_materials_listView);
        listView.setEmptyView(getView().findViewById(id.global_materials_emptyElement));

        adapter = new GlobalMaterialListAdapter(getActivity(), R.layout.item_list_material_view, listContent) {
            @Override
            public void onItem(Object item, View view) {
                if (item != null) {

                    /* SET GLOBAL MATERIAL IMAGE */
                    ImageView globalMaterial_image = (ImageView) view.findViewById(R.id.global_material_imageView);
                    if (globalMaterial_image != null)
                        globalMaterial_image.setImageBitmap(((GlobalMaterialListItem) item).get_image());

                    /* SET GLOBAL MATERIAL NAME */
                    TextView globalMaterial_name = (TextView) view.findViewById(R.id.global_material_name);
                    if (globalMaterial_name != null)
                        globalMaterial_name.setText(((GlobalMaterialListItem) item).get_name());

                    /* SET GLOBAL MATERIAL ADRESS */
                    TextView globalMaterial_direction = (TextView) view.findViewById(R.id.global_material_address);
                    if (globalMaterial_direction != null)
                        globalMaterial_direction.setText(((GlobalMaterialListItem) item).get_adress());

                    /* SET GLOBAL MATERIAL QUANTITY */
                    TextView globalMaterial_quantity = (TextView) view.findViewById(R.id.global_material_quantity);
                    if (globalMaterial_quantity != null)
                        if (((GlobalMaterialListItem) item).get_quantity() <= 0)
                            globalMaterial_quantity.setText("-");
                        else
                            globalMaterial_quantity.setText(Integer.toString(((GlobalMaterialListItem) item).get_quantity()));

                    /* SET GLOBAL MATERIAL URGENT ICON TO VISIBLE IF IT IS NECESSARY */
                    ImageView globalMaterial_urgent = (ImageView) view.findViewById(R.id.global_material_warningImage);
                    if (globalMaterial_urgent != null)
                        if (((GlobalMaterialListItem) item).is_urgent())
                            globalMaterial_urgent.setVisibility(View.VISIBLE);

                }
            }
            public int getViewTypeCount() {
                return 1;
            }
        };

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalMaterialListItem selected = (GlobalMaterialListItem) parent.getItemAtPosition(position);

                CharSequence text = "Item description: " + selected.get_description() + "\t"
                                  + "Item urgent: " + selected.is_urgent();
                Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(menu.top_menu, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.action_refresh:

                CharSequence text = "Selected: Refresh";
                Toast toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
                toast.show();
                reloadAsyncTask();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fillGlobalMaterialsList() {
        /* examples */
        /* examples */
        /* examples */
        listContent = new ArrayList<>();
        listContent.clear();
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_sillas),
                "Sillas",
                "Sillas sobrantes",
                true,
                5,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_botellas),
                "Botellas",
                "Botellas sobrantes",
                false,
                5,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_cables),
                "Cables",
                "Cables sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_cajas),
                "Cajas",
                "Cajas Grandes",
                false,
                20,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_herramientas),
                "Herramientas",
                "Herramientas sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_neumaticos),
                "Neumaticos",
                "Neumaticos sobrantes",
                true,
                4,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_pinturas),
                "Pinturas",
                "Pinturas roja, azul, verde y más...",
                true,
                0,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
                BitmapFactory.decodeResource(getResources(), drawable.example_resources_piscina),
                "Piscina",
                "Piscina hinchable pequeña",
                true,
                1,
                "C/Exemple nº123"));
        listContent.add(new GlobalMaterialListItem(
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

    private void reloadAsyncTask(){
        (new AsyncCustomTask(getContext()){
            @Override
            public void doInBackgroundFunction() {
                //item.setEnabled(false);
                fillGlobalMaterialsList();
            }
            @Override
            public void onPostExecuteFunction() {
                adapter.notifyDataSetChanged();
                //item.setEnabled(true);
            }
        }).execute();
    }
}
