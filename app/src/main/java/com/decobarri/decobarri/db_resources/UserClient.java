package com.decobarri.decobarri.db_resources;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
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
}
