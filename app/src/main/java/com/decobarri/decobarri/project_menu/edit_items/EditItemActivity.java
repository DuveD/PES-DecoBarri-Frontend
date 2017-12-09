package com.decobarri.decobarri.project_menu.edit_items;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;
import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class
EditItemActivity extends AppCompatActivity {

    private String title;
    private Boolean edit;
    private int id;

    private ImageView itemImageView;
    private TextView saveChanges;
    private EditText nameEditText;
    private EditText descEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        initVars();
        setUpNavBar();
        setUpButtons();
    }

    private void initVars() {

        title = "Item";
        edit = getIntent().getBooleanExtra(Const.EDIT_ITEM, false);

        saveChanges = (TextView) findViewById(R.id.saveChanges);
        itemImageView = (ImageView) findViewById(R.id.item_imageView);

        nameEditText = (EditText) findViewById(R.id.input_name);
        descEditText = (EditText) findViewById(R.id.input_description);

        EasyImage.configuration(this)
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false);

        setSource();
    }

    private void setSource() {
        if (edit) {
            title = "Edit " + title;
            saveChanges.setText("Save Changes");
            id = getIntent().getIntExtra(Const.ID, -1);
            fillInfo();
        } else {
            title = "New " + title;
            saveChanges.setText("Add Item");
        }
    }

    private void fillInfo() {
    }

    private void setUpNavBar(){
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
        ((ImageButton) findViewById(R.id.toolbar_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (changes()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
            builder.setMessage("Changes not saved. Exit?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        else super.onBackPressed();
    }

    private boolean changes() {
        // check if something changes
        return false;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private void setUpButtons() {

        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithDocuments(EditItemActivity.this, "Choose Item Image", 0);
            }
        });

        findViewById(R.id.saveChanges).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameEditText.getText().toString();
                String description = descEditText.getText().toString();
                String projectId = getIntent().getStringExtra(Const.PROJECT_ID);
                Item item = new Item();
                item.setName(name);
                item.setDescription(description);

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(getResources().getString(R.string.db_URL))
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
                Retrofit retrofit = builder.build();
                ProjectClient client = retrofit.create(ProjectClient.class);

                Call<String> call = client.AddItem(projectId, item);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            onBackPressed();
                        }
                        System.out.println("Error code:" + response.code());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });
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
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(EditItemActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> imagesFiles) {
        if (imagesFiles.size() > 1) System.out.println("There're more than one image!");
        Picasso.with(this)
                .load(imagesFiles.get(0))
                .resize(itemImageView.getWidth(), itemImageView.getHeight())
                .centerCrop()
                .into(itemImageView);
    }
}
