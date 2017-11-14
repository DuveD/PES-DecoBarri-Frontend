package com.decobarri.decobarri.db_resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 11/11/2017.
 */

public class UserEdit {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;

    public UserEdit (String name, String email){
        this.name=name;
        this.email=email;
    }

    public void SetName (String name){
        this.name=name;
    }

    public String GetName (){
        return name;
    }

    public void SetEmail (String email){
        this.email=email;
    }

    public String GetEmail(){
        return email;
    }
}
