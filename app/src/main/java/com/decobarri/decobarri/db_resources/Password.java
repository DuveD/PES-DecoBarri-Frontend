package com.decobarri.decobarri.db_resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus on 11/11/2017.
 */

public class Password {

    @SerializedName("old_password")
    @Expose
    private String old_password;
    @SerializedName("new_password")
    @Expose
    private String new_password;

    public Password (String old_password, String new_password){
        this.old_password=old_password;
        this.new_password=new_password;
    }

    public void SetOldPassword (String old_password){
        this.old_password=old_password;
    }

    public String GetOldPassword (){
        return old_password;
    }

    public void SetNewPassword (String new_password){
        this.new_password=new_password;
    }

    public String GetNewPassword(){
        return new_password;
    }
}
