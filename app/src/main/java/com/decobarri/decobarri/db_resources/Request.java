package com.decobarri.decobarri.db_resources;

/**
 * Created by Asus on 05/01/2018.
 */

public class Request {

    private String username;
    private String name;
    private String project;

    public Request (String username, String name, String project) {
        this.username = username;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
