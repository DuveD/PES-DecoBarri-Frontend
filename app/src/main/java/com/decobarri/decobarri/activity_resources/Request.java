package com.decobarri.decobarri.activity_resources;

/**
 * Created by Asus on 05/01/2018.
 */

public class Request {

    private String username;
    private String project;

    Request (String username, String project) {
        this.username = username;
        this.project = project;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
