package com.decobarri.decobarri.project_menu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Image;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.main_menu.MainMenuActivity;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoFragment extends Fragment {

    private TextView projNameText;
    private TextView projDescriptionText;
    private ImageView projImage;
    private Button deletebutton;

    private String projName;
    private String projDescription;
    private String projectID;
    private String username;

    private static final String TAG = InfoFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    private void initVars() {
        projectID = ((ProjectMenuActivity)this.getActivity()).projectID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_info, container, false);
        projNameText = (TextView) view.findViewById(R.id.textProjName);
        projDescriptionText = (TextView) view.findViewById(R.id.descriptionText);
        projImage = (ImageView) view.findViewById(R.id.project_image);
        deletebutton = (Button) view.findViewById(R.id.deletebutton);
        deletebutton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder b =  new  AlertDialog.Builder(getActivity())
                        .setTitle("Are you sure you want to delete this project forever?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        deleteProject(projectID);
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                b.show();
            }
                //Do stuff here
            });

        ((ProjectMenuActivity) this.getActivity()).setCurrentFragment(TAG);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<Project> call = client.FindProjectById(projectID);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.isSuccessful()) {
                    projNameText.setText(response.body().getName());
                    projDescriptionText.setText(response.body().getDescription());
                    loadImageInImageview(response.body().getTheme());
                } else {
                    System.out.println("Project info load code: " + response.code());
                    System.out.println("Project info load: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {

            }
        });
        return view;
    }


    private void deleteProject(String projectID){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder
                .client(httpClient.build())
                .build();
        ProjectClient client =  retrofit.create(ProjectClient.class);
        Call<String> call = client.DeleteProject(projectID);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                if (response.isSuccessful()) {
                    //
                    System.out.println("Success : " + response.body());
                    Toast toast = Toast.makeText(getActivity(), "Proyecto Borrado", (int) 2);
                    Intent i = new Intent(getActivity(), MainMenuActivity.class);
                    System.out.println("Creado en bd");
                    startActivity(i);
                    toast.show();
                }
                else {
                    System.out.println("Error code: " + response.code());

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast toast = Toast.makeText(getActivity(), "Proyecto Borrado", (int) 2);
                Intent i = new Intent(getActivity(), MainMenuActivity.class);
                System.out.println("Creado en bd");
                startActivity(i);
                toast.show();
                System.out.println("Call failed: " + call.request());
            }
        });
    }

    private void loadImageInImageview(String encodedImage){
        Bitmap bm = stringToBitMap(encodedImage);
        projImage.setImageBitmap(bm);
    }

    public Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onStart() {
        projNameText = (TextView) getActivity().findViewById(R.id.textProjName);
        projDescriptionText = (TextView) getActivity().findViewById(R.id.descriptionText);
        super.onStart();
    }
}