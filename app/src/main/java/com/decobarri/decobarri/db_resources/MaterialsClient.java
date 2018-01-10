package com.decobarri.decobarri.db_resources;

import com.decobarri.decobarri.activity_resources.Materials.Material;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MaterialsClient {

    @GET("/material/findAll")
    Call<List<Material>> contentList();
}
