package com.decobarri.decobarri.db_resources;

import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.activity_resources.Notes.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotesClient {
    @GET("/project/getNotes/{id}")
    Call<List<Note>> getNotes(@Path("id") String id);

    @POST("/project/addNote/{id}")
    Call<String> addNote(@Path("id") String id, @Body Note note);

    @PUT("/project/deleteNote/{id}")
    Call<String> deleteNote(@Path("id") String id, @Body Note note);

    @POST("/project/editNote/{id}")
    Call<String> editNote(@Path("id") String id, @Body Note note);
}
