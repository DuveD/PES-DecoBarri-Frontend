package com.decobarri.decobarri.ActivityResources;

import android.graphics.Bitmap;

/**
 * Created by Marc G on 26/10/2017.
 */

public class ProjectItem {
    private Bitmap image;
    private String projectName;
    private String projectDescription;

    public ProjectItem(Bitmap image, String projectName, String projectDescription) {
        this.image = image;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

    public Bitmap get_Imagen() {
        return image;
    }

    public String get_name() {
        return projectName;
    }

    public String get_description() {
        return projectDescription;
    }
}