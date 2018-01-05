package com.decobarri.decobarri.db_resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arnauorriols on 7/11/17.
 */

public class Project {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("theme")
    @Expose
    private String theme;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("username")
    @Expose
    private String admin;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("tags")
    @Expose
    private List<String> tags = null;

    @SerializedName("members")
    @Expose
    private List<String> members = null;

    public Project(){}
//añadir username
    public Project(String id, String name, String theme, String description, String city, String address, String admin, String lat, String lng) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.description = description;
        this.city = city;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }
}