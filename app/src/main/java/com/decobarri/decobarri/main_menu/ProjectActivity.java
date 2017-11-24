package com.decobarri.decobarri.main_menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.decobarri.decobarri.BaseActivity;
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

public class ProjectActivity extends BaseActivity {

    private TextView projNameText;
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_project_info);

        projNameText = (TextView) findViewById(R.id.textProjName);
        descriptionText = (TextView) findViewById(R.id.descriptionText);

        myTask task = new myTask();
        task.execute(4);
    }

    private class myTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://project-pes.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
            Retrofit retrofit = builder.build();
            ProjectClient client = retrofit.create(ProjectClient.class);
            Call<Project> call = client.FindProjectById("5a0f1bf1b42109137235a5e3");

            call.enqueue(new Callback<Project>() {
                @Override
                public void onResponse(Call<Project> call, Response<Project> response) {
                    if (response.isSuccessful()) {
                        Project p = response.body();
                        System.out.println("Success : " + p.getName());
                        projNameText.setText(p.getName());
                        descriptionText.setText(p.getDescription());
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

            return null;
        }
    }
}