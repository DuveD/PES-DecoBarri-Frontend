package com.decobarri.decobarri.project_menu.edit_items;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;
import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.db_resources.MaterialsInterface;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.project_menu.ItemsFragment;
import com.decobarri.decobarri.project_menu.NotesFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditItemFragment extends Fragment {

    private Item item;
    private View view;
    private TextView titleTextView;
    private ImageView itemImageView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private SearchView materialsSearchView;
    private TextView saveTextView;

    private String name;
    private String description;

    private Retrofit retrofit;

    private static final String TAG = EditItemFragment.class.getSimpleName();

    public static EditItemFragment newInstance(Item item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", (Serializable) item);

        EditItemFragment fragment = new EditItemFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public static EditItemFragment newInstance() {
        EditItemFragment fragment = new EditItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    private void initVars() {
        readBundle(getArguments());
        retrofit = ((ProjectMenuActivity)this.getActivity()).retrofit;
        EasyImage.configuration(getActivity())
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false);
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            item = (Item) bundle.getSerializable("item");
            if (item == null) Log.e(TAG, "Bundle read error, Item is null!");
        } else {
            item = null;
            Log.i(TAG, "Bundle is empty");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_item, container, false);
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);
        titleTextView = (TextView) view.findViewById(R.id.title);
        itemImageView = (ImageView) view.findViewById(R.id.item_imageView);
        nameEditText = (EditText) view.findViewById(R.id.item_name);
        descriptionEditText = (EditText) view.findViewById(R.id.item_description);
        materialsSearchView = (SearchView) view.findViewById(R.id.searchView);
        saveTextView = (TextView) view.findViewById(R.id.save_button);

        setUpNavBar();
        setUpButtons();
        fillInfo();
        return view;
    }

    private void setUpNavBar(){
        if (item != null) titleTextView.setText("Edit Item");
        else titleTextView.setText("New Item");
        ((ImageButton) view.findViewById(R.id.toolbar_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpButtons() {
        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithDocuments(EditItemFragment.this, "Choose Item Image", 0);
            }
        });

        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    private void saveItem() {

        MaterialsInterface client = retrofit.create(MaterialsInterface.class);
        Call<List<Material>> call = client.contentList();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<Material>>() {
            @Override
            public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());

                    // if item is saved correctly
                    ((ProjectMenuActivity)getActivity()).superOnBackPressed();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Material>> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    private void fillInfo() {
        if (item != null) {
            name = item.getName();
            description = item.getDescription();
            //TODO: Materials composition
            saveTextView.setText(
                    "Save Changes");
        } else {
            name = nameEditText.getText().toString();
            description = descriptionEditText.getText().toString();
            saveTextView.setText("Add Item");
        }
    }

    public void onBackPressed() {
        if (changes()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage("Changes not saved. Exit?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        else ((ProjectMenuActivity)this.getActivity()).superOnBackPressed();
    }

    private boolean changes() {
        final String newName = nameEditText.getText().toString();
        final String newDescription = descriptionEditText.getText().toString();
        if (!newName.equals(name)){
            Log.i(TAG, "Name edited.");
            return true;
        } else if (!newDescription.equals(description)){
            Log.i(TAG, "Description edited");
            return true;
        }
        return false;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    ((ProjectMenuActivity)getActivity()).superOnBackPressed();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(ItemsFragment.class.getSimpleName());
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images
                onPhotosReturned(imagesFiles);
                ((ImageView) view.findViewById(R.id.background_image)).setVisibility(View.GONE);

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotosReturned(List<File> imagesFiles) {
        if (imagesFiles.size() > 1) System.out.println("There're more than one image!");
        Picasso.with(getActivity())
                .load(imagesFiles.get(0))
                .resize(itemImageView.getWidth(), itemImageView.getHeight())
                .centerCrop()
                .into(itemImageView);
    }
}
