package com.decobarri.decobarri.db_resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by Asus on 05/11/2017.
 */

public class User {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("projects")
    @Expose
    private List<String> projects = null;

    public User(String id, String name, String password, String email) {
        this.username = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(String id, String password) {
        this.username = id;
        this.password = password;
    }


    public String getId() {
        return username;
    }

    public void setId(String id) {
        this.username = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }
}
