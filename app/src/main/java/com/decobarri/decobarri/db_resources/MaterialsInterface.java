package com.decobarri.decobarri.db_resources;

import com.decobarri.decobarri.activity_resources.Item;
import com.decobarri.decobarri.activity_resources.Material;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MaterialsInterface {

    @GET("/material/findAll")
    Call<List<Material>> contentList();

    /*
    @GET("/users/{user}/repos")
    Call<List<Material>> inventoryList(
            @Path("user") String user
    );

    @GET("/users/{user}/repos")
    Call<List<Material>> needList(
            @Path("user") String user
    );

    @GET("/users/{user}/repos")
    Call<List<Item>> itemList(
            @Path("user") String user
    );
    */
}
