package com.decobarri.decobarri.material_list;

public class GlobalMaterial_listItem {
    private int idImagen;
    private String nameMaterial;
    private String nameDirection;
    private boolean urgent;

    public GlobalMaterial_listItem(int idImagen, String nameMaterial, String nameDirection, boolean urgent) {
        this.idImagen = idImagen;
        this.nameMaterial = nameMaterial;
        this.nameDirection = nameDirection;
        this.urgent = urgent;
    }

    public int get_idImagen() {
        return idImagen;
    }

    public String get_nameMaterial() {
        return nameMaterial;
    }

    public String get_nameDirection() {
        return nameDirection;
    }

    public boolean is_urgent(){
        return urgent;
    }
}
