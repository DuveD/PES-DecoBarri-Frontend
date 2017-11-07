package com.decobarri.decobarri.db_resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arnauorriols on 7/11/17.
 */

public class Nota {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("modifiable")
    @Expose
    private Boolean modifiable;

    public Nota(String id, String title, String date, String description, String author, Boolean modifiable) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.author = author;
        this.modifiable = modifiable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getModifiable() {
        return modifiable;
    }

    public void setModifiable(Boolean modifiable) {
        this.modifiable = modifiable;
    }
}
