package com.decobarri.decobarri.db_resources;

import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.activity_resources.Notes.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotesInterface {

    @GET("/project/getNotes/{id}")
    Call<List<Note>> contentList(
            @Path("id") String id);
}
