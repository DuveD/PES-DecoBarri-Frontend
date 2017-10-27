package com.decobarri.decobarri;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.ActivityResources.globalMaterialListAdapter;
import com.decobarri.decobarri.ActivityResources.globalMaterialListItem;
import com.decobarri.decobarri.db_resources.DB_library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainMenu_GlobalMaterials extends Fragment {
    private DB_library httpDBlibrary;

    private MenuItem refreshItem;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_global_materials, container, false);

        // BD_library init wiht activity context
        httpDBlibrary = new DB_library( this.getActivity() );

        String call = this.getResources().getString(R.string.FIND_ALL_MATERIALS);
        //String result = httpDBlibrary.db_call( call , "" , "GET" );

        /*
        StructureMaterial {
            name: string
            description: string
            urgent: boolean
            quantity: integer
            address: string
        }
        */

        //JSON.parse(result);

        /*
         * Global Materials List Information fill
         */
        ArrayList<globalMaterialListItem> global_material_listContent = new ArrayList<>();

        /* examples */
        /* examples */
        /* examples */

        /*
         * private Bitmap idImagen;
         * private String nameMaterial;
         * private String description;
         * private boolean urgent;
         * private int quantity;
         * private String adress;
         */
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_sillas)),
                "Sillas",
                "Sillas sobrantes",
                true,
                5,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_botellas)),
                "Botellas",
                "Botellas sobrantes",
                false,
                5,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_cables)),
                "Cables",
                "Cables sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_cajas)),
                "Cajas",
                "Cajas Grandes",
                false,
                20,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_herramientas)),
                "Herramientas",
                "Herramientas sobrantes",
                false,
                0,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_neumaticos)),
                "Neumaticos",
                "Neumaticos sobrantes",
                true,
                4,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_pinturas)),
                "Pinturas",
                "Pinturas roja, azul, verde y más...",
                true,
                0,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_piscina)),
                "Piscina",
                "Piscina hinchable pequeña",
                true,
                1,
                "C/Exemple nº123"));
        global_material_listContent.add(new globalMaterialListItem(
                ((MainMenu) getActivity()).compressImage(BitmapFactory.decodeResource(getResources(), R.drawable.example_resources_porexpan)),
                "Porexpan",
                "Cuanto más grande mejor",
                false,
                0,
                "C/Exemple nº123"));
        /* /examples */
        /* /examples */
        /* /examples */

        Collections.sort(global_material_listContent, new Comparator<globalMaterialListItem>() {
            @Override
            public int compare(globalMaterialListItem materialA, globalMaterialListItem materialB)
            {
                int boolean_compare = Boolean.compare( materialB.is_urgent(), materialA.is_urgent() );
                if (boolean_compare == 0) return materialA.get_name().compareToIgnoreCase(materialB.get_name());
                else return boolean_compare;
            }
        });

        final ListView global_material_listView = (ListView) view.findViewById(R.id.global_materials_listView);
        global_material_listView.setEmptyView(view.findViewById(R.id.global_materials_emptyElement));

        /*
         * Global Materials List View fill
         */
        final globalMaterialListAdapter GlobalMaterialAdapter = new globalMaterialListAdapter(this.getActivity(), R.layout.item_list_material_view, global_material_listContent) {
            @Override
            public void onItem(Object item, View view) {
                if (item != null) {

                    /* SET GLOBAL MATERIAL IMAGE */
                    ImageView globalMaterial_image = (ImageView) view.findViewById(R.id.global_material_imageView);
                    if (globalMaterial_image != null)
                        globalMaterial_image.setImageBitmap(((globalMaterialListItem) item).get_image());

                    /* SET GLOBAL MATERIAL NAME */
                    TextView globalMaterial_name = (TextView) view.findViewById(R.id.global_material_name);
                    if (globalMaterial_name != null)
                        globalMaterial_name.setText(((globalMaterialListItem) item).get_name());

                    /* SET GLOBAL MATERIAL ADRESS */
                    TextView globalMaterial_direction = (TextView) view.findViewById(R.id.global_material_address);
                    if (globalMaterial_direction != null)
                        globalMaterial_direction.setText(((globalMaterialListItem) item).get_adress());

                    /* SET GLOBAL MATERIAL QUANTITY */
                    TextView globalMaterial_quantity = (TextView) view.findViewById(R.id.global_material_quantity);
                    if (globalMaterial_quantity != null)
                        if (((globalMaterialListItem) item).get_quantity() <= 0)
                            globalMaterial_quantity.setText("-");
                        else
                            globalMaterial_quantity.setText(Integer.toString(((globalMaterialListItem) item).get_quantity()));

                    /* SET GLOBAL MATERIAL URGENT ICON TO VISIBLE IF IT IS NECESSARY */
                    ImageView globalMaterial_urgent = (ImageView) view.findViewById(R.id.global_material_warningImage);
                    if (globalMaterial_urgent != null)
                        if (((globalMaterialListItem) item).is_urgent()) globalMaterial_urgent.setVisibility(View.VISIBLE);

                }
            }
        };

        /*
         * Global Materials List item click listener
         */
        global_material_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalMaterialListItem selected = (globalMaterialListItem) parent.getItemAtPosition(position);

                CharSequence text = "Item description: " + selected.get_description();
                Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        global_material_listView.setAdapter(GlobalMaterialAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshItem = item;

                CharSequence textt = "Selected: Refresh";
                Toast toast = Toast.makeText(getContext(), textt, Toast.LENGTH_SHORT);
                toast.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizeGlobalMaterialsList(){

    }
}
