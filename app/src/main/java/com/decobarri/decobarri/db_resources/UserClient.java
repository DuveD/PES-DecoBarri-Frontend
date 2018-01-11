package com.decobarri.decobarri.db_resources;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Asus on 05/11/2017.
 */

public interface UserClient {

    @POST("/user/login")
    Call<String> LoginUser(@Body User user);

    @POST("/user/add")
    Call<String> CreateAccount(@Body User user);

//******************************************************************
    @GET("user/showMyProjects/{username}")
    Call<List<String>> GetProjects(@Path("username") String username);
//******************************************************************

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

    @GET("/user/getContacts/{username}")
    Call<List<User>> GetContacts(@Path("username") String username);

    @Multipart
    @POST("/user/uploadImage/{username}")
    Call<String> Image(@Path("username") String username, @Part MultipartBody.Part image);

    @GET("/user/getImage/{username}")
    Call<ResponseBody> downloadImage(@Path("username") String username);

    @PUT("/user/addProject/{username}")
    Call<String> addProject(@Path("username") String username, @Body UserProject project_id);
}
