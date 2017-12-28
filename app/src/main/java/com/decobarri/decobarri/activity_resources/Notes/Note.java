package com.decobarri.decobarri.activity_resources.Notes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {

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

    @SerializedName("color")
    @Expose
    private int color;

    public Note(String id, String title, String date, String description, String author, Boolean modifiable, int color) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.author = author;
        this.modifiable = modifiable;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public Boolean getModifiable() {
        return modifiable;
    }

    public int getColor() {
        return color;
    }
}
