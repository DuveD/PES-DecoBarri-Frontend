package com.decobarri.decobarri.db_resources;

import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.activity_resources.Materials.Material;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProjectClient {

    @GET("/project/findByID/{id}")
    Call<Project> FindProjectById(@Path("id") String id);

    @GET("/project/findAll")
    Call<List<Project>> FindAllProjects();

    @GET("/project/findByName/{name}")
    Call<Project> FindProjectByName(@Path("name") String name);

    @POST("/project/add")
    Call<String> AddProject(@Body Project project);

    @DELETE ("/project/delete/{id}")
    Call<String> DeleteProject(@Path("id") String id);

    @PUT("/project/edit/{id}")
    Call<String> EditProject(@Path("id") String id, @Body Project project);

    @GET("/project/getItems/{id}")
    Call<List<Item>> GetItems(@Path("id") String id);

    @GET("/project/getMembers/{id}")
    Call<List<User>> GetMembers(@Path("id") String id);

    @POST("/project/addItem/{id}")
    Call<String> AddItem(@Path("id") String id, @Body Item item);

    @PUT("project/editItem/{id}")
    Call<String> EditItem(@Path("id") String _id, @Body Item item);

    //@DELETE("/project/deleteItem/{id}")
    //@HTTP(method = "DELETE", path = "/project/deleteItem/{id}", hasBody = true)
    @PUT("/project/deleteItem/{id}")
    Call<String> DeleteItem(@Path("id") String _id, @Body Item item);

    // Invetory List Materials

    @GET("")
    Call<List<Material>> getInvetoryList();

    @POST("")
    Call<String> addInvetoryMaterial();

    @PUT("")
    Call<String> editInvetoryMaterial();

    @PUT("")
    Call<String> deleteInvetoryMaterial();

    // Need List Materials

    @GET("")
    Call<List<Material>> getNeedList();

    @POST("")
    Call<String> addNeedListMaterial();

    @PUT("")
    Call<String> editNeedListMaterial();

    @PUT("")
    Call<String> deleteNeedListMaterial();
}
