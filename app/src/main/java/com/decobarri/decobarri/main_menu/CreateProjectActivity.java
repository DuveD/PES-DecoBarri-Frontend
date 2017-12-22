package com.decobarri.decobarri.main_menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Marc G on 13/11/2017.
 */

public class CreateProjectActivity extends AppCompatActivity {

    private EditText input_name, input_description, input_theme;
    private TextInputLayout inputLayoutName, inputLayoutDescription, inputLayoutTheme;
    private Button button_create;
    private ImageView projectImage;
    private String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_description);
        inputLayoutTheme = (TextInputLayout) findViewById(R.id.input_layout_theme);
        input_name = (EditText) findViewById(R.id.input_project_name);
        input_description = (EditText) findViewById(R.id.input_description);
        input_theme = (EditText) findViewById(R.id.input_theme);
        button_create = (Button) findViewById(R.id.create_button);

        SharedPreferences pref = this.getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");

        projectImage = (ImageView) findViewById(R.id.imageProject);
        projectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithDocuments(CreateProjectActivity.this, "Choose Item Image", 0);
            }
        });

        EasyImage.configuration(this)
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false);

        input_name.addTextChangedListener(new MyTextWatcher(input_name));
        input_description.addTextChangedListener(new MyTextWatcher(input_description));
        input_theme.addTextChangedListener(new MyTextWatcher(input_theme));

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submitForm();
                if (validateName() & validateDescription()) {
                    //***********************************************
                    //Falta añadir imagen, username y ubicacion
                    //***********************************************
                    Project projectCreated = new Project("", input_name.getText().toString(), input_theme.getText().toString(),
                            input_description.getText().toString(), "Barcelona", "FIB");
                    creaProjecte(projectCreated);
                }
            }
        });
    }

    private void creaProjecte(Project projectCreated) {
        //**********************************************************************

        //Descomentar para crear proyecto en servidor

        /*OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder
                .client(httpClient.build())
                .build();
        ProjectClient client =  retrofit.create(ProjectClient.class);
        Call<String> call = client.AddProject(projectCreated);
        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                if (response.isSuccessful()) {
                   //
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //
            }
        });*/
    }

    private void setImage(){
        projectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithDocuments(CreateProjectActivity.this, "Choose an image for your project", 0);
            }
        });
    }

    private boolean validateName() {
        if (input_name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("Escribe un nombre para tu proyecto");
            requestFocus(input_name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDescription() {
        String description = input_description.getText().toString().trim();
        if (description.isEmpty()){
            inputLayoutDescription.setError("Escribe una descripcion para tu proyecto");
            requestFocus(input_description);
            return false;
        } else {
            inputLayoutDescription.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_project_name:
                    validateName();
                    break;
                case R.id.input_description:
                    validateDescription();
                    break;
                case R.id.input_theme:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images
                onPhotosReturned(imagesFiles);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(CreateProjectActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> imagesFiles) {
        if (imagesFiles.size() > 1) System.out.println("There're more than one image!");
        Picasso.with(this)
                .load(imagesFiles.get(0))
                .resize(projectImage.getWidth(), projectImage.getHeight())
                .centerCrop()
                .into(projectImage);
    }

}