package com.decobarri.decobarri;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.global_material_list_resources.GlobalMaterial_listAdapter;
import com.decobarri.decobarri.global_material_list_resources.GlobalMaterial_listItem;

import java.util.ArrayList;

public class MainMenu_GlobalMaterials extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_global_materials, container, false);

        /*
         * Global Materials List Information fill
         */
        ArrayList<GlobalMaterial_listItem> global_material_listContent = new ArrayList<GlobalMaterial_listItem>();

        /* examples */
        /* examples */
        /* examples */
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_botellas, "Botellas", "C/Exemple nº123", false));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_cables, "Cables", "C/Exemple nº123", true));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_cajas, "Cajas", "C/Exemple nº123", false));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_neumaticos, "Neumaticos", "C/Exemple nº123", true));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_porexpan, "Porexpan", "C/Exemple nº123", false));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_pinturas, "Pinturas", "C/Exemple nº123", true));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_herramientas, "Herramientas", "C/Exemple nº123", false));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_sillas, "Sillas", "C/Exemple nº123", true));
        global_material_listContent.add(new GlobalMaterial_listItem(R.drawable.example_resources_piscina_hinchable, "Piscina hinchable", "C/Exemple nº123", false));
        /* /examples */
        /* /examples */
        /* /examples */

        final ListView global_material_listView = (ListView) view.findViewById(R.id.global_materials_listView);
        global_material_listView.setEmptyView(view.findViewById(R.id.global_materials_emptyElement));

        /*
         * Global Materials List View fill
         */
        final GlobalMaterial_listAdapter GlobalMaterialAdapter = new GlobalMaterial_listAdapter(this.getActivity(), R.layout.item_list_material_view, global_material_listContent) {
            @Override
            public void onItem(Object item, View view) {
                if (item != null) {

                    /* SET GLOBAL MATERIAL IMAGE */
                    ImageView globalMaterial_image = (ImageView) view.findViewById(R.id.global_material_imageView);
                    if (globalMaterial_image != null)
                        globalMaterial_image.setImageResource(((GlobalMaterial_listItem) item).get_idImagen());

                    /* SET GLOBAL MATERIAL NAME */
                    TextView globalMaterial_name = (TextView) view.findViewById(R.id.global_material_name);
                    if (globalMaterial_name != null)
                        globalMaterial_name.setText(((GlobalMaterial_listItem) item).get_nameMaterial());

                    /* SET GLOBAL MATERIAL DIRECTION */
                    TextView globalMaterial_direction = (TextView) view.findViewById(R.id.global_material_direction);
                    if (globalMaterial_direction != null)
                        globalMaterial_direction.setText(((GlobalMaterial_listItem) item).get_nameDirection());

                    /* SET GLOBAL MATERIAL URGENT ICON TO VISIBLE IF IT IS NECESSARY */
                    if (((GlobalMaterial_listItem) item).is_urgent())
                        ((ImageView) view.findViewById(R.id.global_material_warningImage)).setVisibility(View.VISIBLE);

                }
            }
        };

        /*
         * Global Materials List item click listener
         */
        global_material_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalMaterial_listItem selected = (GlobalMaterial_listItem) parent.getItemAtPosition(position);

                CharSequence text = "Selected: " + selected.get_nameMaterial();
                Toast toast = Toast.makeText(view.getContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        global_material_listView.setAdapter(GlobalMaterialAdapter);

        return view;
    }
}
