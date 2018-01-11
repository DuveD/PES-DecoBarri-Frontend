package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Image;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;

import java.util.ArrayList;

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