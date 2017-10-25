package com.decobarri.decobarri.ActivityResources;

/**
 * Created by Marc G on 26/10/2017.
 */

public class projectItem {
    private int idImagen;
    private String projectName;
    private String projectDescription;

    public projectItem(int idImagen, String nameMaterial, String nameDirection, boolean urgent) {
        this.idImagen = idImagen;
        this.projectName = nameMaterial;
        this.projectDescription = nameDirection;
    }

    public int get_idImagen() {
        return idImagen;
    }

    public String get_name() {
        return projectName;
    }

    public String get_description() {
        return projectDescription;
    }
}