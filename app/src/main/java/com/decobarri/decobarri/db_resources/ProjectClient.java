package com.decobarri.decobarri.db_resources;

import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.activity_resources.Materials.Material;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProjectClient {

    @GET("/project/findByID/{id}")
    Call<Project> FindProjectById(@Path("id") String id);

    @POST("/project/findProjectsByLocation ")
    Call<List<ProjectLocation>> FindProjectsByLocation (@Body Project findProjectsByLocation);

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

    @POST("/project/addMember/{id}")
    Call<String> AddMember(@Path("id") String id, @Body User user);

    @GET("/project/getMembers/{id}")
    Call<List<User>> GetMembers(@Path("id") String id);

    @POST("/project/addItem/{id}")
    Call<String> AddItem(@Path("id") String id, @Body Item item);

    @PUT("project/editItem/{id}")
    Call<String> EditItem(@Path("id") String _id, @Body Item item);

    @PUT("/project/deleteItem/{id}")
    Call<String> DeleteItem(@Path("id") String _id, @Body Item item);

    // Invetory List Materials

    @GET("/project/getInventory/{id}")
    Call<List<Material>> getInvetoryList(@Path("id") String id);

    @POST("/project/addInventoryMaterial/{id}")
    Call<String> addInvetoryMaterial(@Path("id") String id, @Body Material material);

    @PUT("/project/editInventoryMaterial/{id}")
    Call<String> editInvetoryMaterial(@Path("id") String id, @Body Material material);

    @POST("/project/deleteInventoryMaterial/{id}")
    Call<String> deleteInvetoryMaterial(@Path("id") String id, @Body Material material);

    // Need List Materials

    @GET("/project/getNeedList/{id}")
    Call<List<Material>> getNeedList(@Path("id") String id);

    @POST("/project/addNeedListMaterial/{id}")
    Call<String> addNeedListMaterial(@Path("id") String id, @Body Material material);

    @PUT("/project/editNeedListMaterial/{id}")
    Call<String> editNeedListMaterial(@Path("id") String id, @Body Material material);

    @POST("/project/deleteNeedListMaterial/{id}")
    Call<String> deleteNeedListMaterial(@Path("id") String id, @Body Material material);


    @GET("/project/getImage/{id}")
    Call<ResponseBody> getImage(@Path("id") String _id);

    @POST("/project/addRequest/{id}")
    Call<String> addRequest(@Path("id") String _id, @Body User user);

    @POST("/project/deleteRequest/{id}")
    Call<String> deleteRequest(@Path("id") String _id, @Body User user);

    @GET("/project/getRequests/{id}")
    Call<List<Project>> getRequests(@Path("id") String _id);
}
