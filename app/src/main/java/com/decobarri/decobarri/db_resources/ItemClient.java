package com.decobarri.decobarri.db_resources;

import com.decobarri.decobarri.activity_resources.Items.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Asus on 05/12/2017.
 */

public interface ItemClient {

    @GET("/item/findAll")
    Call<List<Item>> FindAll();

    @GET("/item/findItemByID/{id}")
    Call<Item> FindItemByID(@Path("id") String _id);

    @GET("/item/findItemByName/{name}")
    Call<Item> FindItemByName(@Path("name") String name);

}
