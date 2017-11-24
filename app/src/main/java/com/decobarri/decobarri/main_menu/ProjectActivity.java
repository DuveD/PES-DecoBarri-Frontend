package com.decobarri.decobarri.main_menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by arnauorriols on 10/11/17.
 */

public class ProjectActivity extends AppCompatActivity {

    private TextView projNameText;
    private TextView descriptionText;
    ProjectClient client;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_project_info);

        System.out.println("prova");

        projNameText = (TextView) findViewById(R.id.textProjName);
        descriptionText = (TextView) findViewById(R.id.descriptionText);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://project-pes.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
        Retrofit retrofit = builder.build();
        client = retrofit.create(ProjectClient.class);

        loadProjectInfo();
    }

    private void loadProjectInfo() {
        Call<Project> call = client.FindProjectById("5a0f1bf1b42109137235a5e3");

        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.isSuccessful()) {
                    project = response.body();
                    System.out.println("Success!! : " + project);
                    projNameText.setText(project.getName());
                    descriptionText.setText(project.getDescription());
                } else {
                    System.out.println("Error: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                System.out.println("Error call : " + call.request().toString());
                System.out.println("Error throwable: " + t.getMessage());
            }
        });
    }
}