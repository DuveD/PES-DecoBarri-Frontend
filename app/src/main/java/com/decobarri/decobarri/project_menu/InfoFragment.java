package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class InfoFragment extends Fragment {

    TextView projName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_info, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Project Info");
        return view;
    }

    @Override
    public void onStart() {
        bottomSheetButtonCliked(true);
        projName = (TextView) getActivity().findViewById(R.id.textProjName);
        myTask task = new myTask();
        task.execute(4);

        super.onStart();
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
            Retrofit retrofit =builder.build();
            ProjectClient client = retrofit.create(ProjectClient.class);
            Call<Project> call = client.FindProjectById("5a0f1e7a9837bb158a4f0a6b");
            call.enqueue(new Callback<Project>() {
                @Override
                public void onResponse(Call<Project> call, Response<Project> response) {
                    if (response.isSuccessful()) {
                        System.out.println("Success : " + response.body());
                    } else {
                        System.out.println("Error: " + response.body());
                    }
                }

                @Override
                public void onFailure(Call<Project> call, Throwable t) {
                    System.out.println("Error call: " + call.request().toString());
                    System.out.println("Error throwable: " + t.getMessage());
                }
            });

            return null;
        }
    }

    @Override
    public void onStop() {
        bottomSheetButtonCliked(false);
        super.onStop();
    }

    void bottomSheetButtonCliked(Boolean clicked){
        if (clicked){
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setAlpha(0.4f);
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_background_selected_color));
        } else {
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setAlpha(1f);
            ((LinearLayout) getActivity().findViewById(R.id.bottom_sheet_info)).setBackground(getResources().getDrawable(R.drawable.bottom_sheet_button_background));
        }
    }
}