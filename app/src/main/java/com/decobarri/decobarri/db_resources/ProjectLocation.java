package com.decobarri.decobarri.db_resources;

/**
 * Created by Asus on 11/01/2018.
 */

public class ProjectLocation {

    private String position;
    private Project project;

    public ProjectLocation (String position, Project project) {
        this.position = position;
        this.project = project;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
