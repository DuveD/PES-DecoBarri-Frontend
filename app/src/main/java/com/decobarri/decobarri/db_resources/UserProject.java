package com.decobarri.decobarri.db_resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 24/11/2017.
 */

public class UserProject {

    @SerializedName("project_id")
    @Expose
    private String project_id;

    public UserProject (String project_id){
        this.project_id=project_id;
    }
}
