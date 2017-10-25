package com.decobarri.decobarri.ActivityResources;

/**
 * Created by Marc G on 26/10/2017.
 */

public class projectItem {
    private int idImagen;
    private String projectName;
    private String projectDescription;

    public projectItem(int image, String projectName, String projectDescription) {
        this.idImagen = image;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
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