package com.decobarri.decobarri.project_menu.edit_items;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.project_menu.ItemsFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private String projectId;
    private ImageButton toolbarImageButton;

    private String oldImage;
    private String oldName;
    private String oldDescription;

    private Retrofit retrofit;

    private static final String TAG = EditItemFragment.class.getSimpleName();

    public static EditItemFragment newInstance(Item item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);

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

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getActivity().getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
        retrofit = builder.build();

        //retrofit = ((ProjectMenuActivity)this.getActivity()).retrofit;
        EasyImage.configuration(getActivity())
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false);
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            item = bundle.getParcelable("item");
            Log.e(TAG, "Item ready");
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
        projectId = ((ProjectMenuActivity)this.getActivity()).projectID;
        toolbarImageButton = ((ImageButton) view.findViewById(R.id.toolbar_button));

        if (item!=null){
            String image = item.getImage();
            itemImageView.setImageBitmap(decodeFromBase64(image));
            nameEditText.setText(item.getName());
            descriptionEditText.setText(item.getDescription());
        }

        setUpNavBar();
        setUpButtons();
        fillInfo();
        return view;
    }

    private void setUpNavBar(){
        if (item != null) titleTextView.setText("Edit Item");
        else titleTextView.setText("New Item");
        toolbarImageButton.setOnClickListener(new View.OnClickListener() {
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
                EasyImage.openChooserWithDocuments(EditItemFragment.this, getResources().getString(R.string.choose_note_image), 0);
            }
        });

        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameEditText.getText().toString().equals("") && descriptionEditText.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "El ítem debe tener nombre y descripción.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Item newItem = new Item();
                    newItem.setName(nameEditText.getText().toString());
                    newItem.setDescription(descriptionEditText.getText().toString());

                    BitmapDrawable bitmapDrawable = ((BitmapDrawable)itemImageView.getDrawable());
                    Drawable drawable = getResources().getDrawable(R.drawable.item);
                    Bitmap bitmap = ((bitmapDrawable == null) ? ((BitmapDrawable) drawable).getBitmap() : bitmapDrawable.getBitmap());
                    newItem.setImage(encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 50));

                    if (item != null) {
                        newItem.setId(item.getID());
                        saveItem(newItem);
                    }
                    else createItem(newItem);
                }
            }
        });
    }

    private void saveItem(Item newItem) {

        System.out.println("ID: " + item.getID());
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<String> call = client.EditItem(projectId, newItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());
                if (response.isSuccessful()){
                    System.out.println("Response: " + response.body());
                    ((ProjectMenuActivity)getActivity()).back();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error: " + t.toString());
            }
        });
    }

    private void createItem(Item newItem) {
        disableButtons();
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<String> call = client.AddItem(projectId, newItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());
                if (response.isSuccessful()){
                    System.out.println("Response: " + response.body());
                    ((ProjectMenuActivity)getActivity()).back();
                }
                activeButtons();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error: " + t.toString());
                activeButtons();
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
            oldName = item.getName();
            oldDescription = item.getDescription();
            //TODO: Materials composition
            saveTextView.setText("Save Changes");
        } else {
            oldName = nameEditText.getText().toString();
            oldDescription = descriptionEditText.getText().toString();
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
        else ((ProjectMenuActivity)this.getActivity()).back();
    }

    private boolean changes() {
        final String newName = nameEditText.getText().toString();
        final String newDescription = descriptionEditText.getText().toString();

        if (!newName.equals(oldName)){
            Log.i(TAG, "Name edited.");
            return true;
        } else if (!newDescription.equals(oldDescription)){
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
                    ((ProjectMenuActivity)getActivity()).back();
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

    private void disableButtons(){
        saveTextView.setEnabled(false);
        toolbarImageButton.setEnabled(false);
    }

    private void activeButtons(){
        saveTextView.setEnabled(true);
        toolbarImageButton.setEnabled(true);
    }

    private void onPhotosReturned(List<File> imagesFiles) {
        if (imagesFiles.size() > 1) System.out.println("There're more than one image!");
        Picasso.with(getActivity())
                .load(imagesFiles.get(0))
                .resize(itemImageView.getWidth(), itemImageView.getHeight())
                .centerCrop()
                .into(itemImageView);
    }

    public Bitmap decodeFromBase64(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        Bitmap resized = Bitmap.createScaledBitmap(image, image.getWidth()/2, image.getHeight()/2, true);
        resized.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.
                toByteArray(), Base64.DEFAULT);
    }
}
