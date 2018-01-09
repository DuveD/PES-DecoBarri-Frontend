package com.decobarri.decobarri.activity_resources.Notes;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable{

    private String _id;
    private String title;
    private String description;
    private String author;
    private Boolean modifiable;
    private String date;
    private String color;

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(author);
        parcel.writeString((modifiable ? "true" : "false"));
        parcel.writeString(date);
        parcel.writeString(color);
    }

    public Note (){
        this._id = "";
        this.title = "";
        this.description = "";
        this.author = "";
        this.modifiable = true;
        this.date = "";
        this.color = "";
    }

    public Note (String _id, String title, String date, String description, String author, Boolean modifiable, String color) {
        this._id = _id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.modifiable = modifiable;
        this.date = date;
        this.color = color;
    }

    protected Note (Parcel in) {
        _id = in.readString();
        title = in.readString();
        description = in.readString();
        author = in.readString();
        modifiable = in.readString().equals("true");
        date = in.readString();
        color = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return title;
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

    public String getDate() {
        return date;
    }

    public String getColor() {
        return color;
    }
}
