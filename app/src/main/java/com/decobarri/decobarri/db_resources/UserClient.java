package com.decobarri.decobarri.db_resources;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Asus on 05/11/2017.
 */

public interface UserClient {

    @POST("/user/login")
    Call<String> LoginUser(@Body User user);

    @POST("/user/add")
    Call<String> CreateAccount(@Body User user);

    @GET("/user/findByID/{username}")
    Call<User> FindByID(@Path("username") String username);

    @PUT("/user/edit/{username}")
    Call<String> EditUser(@Path("username") String username, @Body User user);

    @PUT("/user/editPassword/{username}")
    Call<String> EditPassword(@Path("username") String username, @Body Password password);

    @PUT("/user/addContact/{username}")
    Call<String> AddContact(@Path("username") String username, @Body User contact);

    @PUT("/user/deleteContact/{username}")
    Call<String> DeleteContact(@Path("username") String username, @Body User contact);

    @PUT("/user/deleteProject/{username}")
    Call<String> DeleteProject(@Path("username") String username, @Body UserProject p);
}
