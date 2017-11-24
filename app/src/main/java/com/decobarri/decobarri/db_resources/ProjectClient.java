package com.decobarri.decobarri.db_resources;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by arnauorriols on 7/11/17.
 */

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


}
