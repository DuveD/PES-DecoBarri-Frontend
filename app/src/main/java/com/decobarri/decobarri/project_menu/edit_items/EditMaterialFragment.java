package com.decobarri.decobarri.project_menu.edit_items;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.project_menu.InventoryFragment;
import com.decobarri.decobarri.project_menu.ItemsFragment;
import com.decobarri.decobarri.project_menu.NeedListFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditMaterialFragment extends Fragment {

    public String parentFragment;

    private Material oldMaterial;
    private View view;
    private TextView titleTextView;
    private ImageView materialImageView;
    private TextView nameTextView;
    private TextView quantityTextView;
    private TextView adressTextView;
    private TextView descriptionTextView;
    private CheckBox urgentCheckBox;
    private TextView saveTextView;
    private ImageButton closeButton;

    private String projectID;

    private String oldImage;
    private String oldName;
    private int oldQuantity;
    private String oldDescription;
    private Boolean oldUrgent;

    private Retrofit retrofit;

    private static final String TAG = EditMaterialFragment.class.getSimpleName();

    public static EditMaterialFragment newInstance(Material oldMaterial, String parentFragment ) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("oldMaterial", oldMaterial);
        bundle.putString("parentFragment", parentFragment);

        EditMaterialFragment fragment = new EditMaterialFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public static EditMaterialFragment newInstance( String parentFragment ) {
        Bundle bundle = new Bundle();
        bundle.putString("parentFragment", parentFragment);
        EditMaterialFragment fragment = new EditMaterialFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_edit_material);
        initVars();
    }

    private void initVars() {
        readBundle(getArguments());
        retrofit = ((ProjectMenuActivity)this.getActivity()).retrofit;
        projectID = ((ProjectMenuActivity)this.getActivity()).projectID;
        EasyImage.configuration(getActivity())
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false);
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            parentFragment = bundle.getString("parentFragment");
            if (parentFragment == null) Log.e(TAG, "Bundle read error, parentFragment is null!");

            oldMaterial = bundle.getParcelable("oldMaterial");
            if (oldMaterial == null) Log.i(TAG, "Bundle read info, Material is null.");
        } else {
            oldMaterial = null;
            Log.e(TAG, "Bundle read error, is empty.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_material, container, false);
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);
        titleTextView = (TextView) view.findViewById(R.id.title);

        materialImageView = (ImageView) view.findViewById(R.id.material_imageView);
        nameTextView = (TextView) view.findViewById(R.id.input_name);
        quantityTextView = (TextView) view.findViewById(R.id.input_quantity);
        adressTextView = (TextView) view.findViewById(R.id.adress);
        descriptionTextView = (TextView) view.findViewById(R.id.input_description);
        urgentCheckBox = (CheckBox) view.findViewById(R.id.input_urgent);
        saveTextView = (TextView) view.findViewById(R.id.save_button);
        closeButton = (ImageButton) view.findViewById(R.id.toolbar_button);

        setUpNavBar();
        setUpButtons();
        fillInfo();
        return view;
    }

    private void setUpNavBar(){
        if (oldMaterial != null) titleTextView.setText("Edit Material");
        else titleTextView.setText("New Material");
        ((ImageButton) view.findViewById(R.id.toolbar_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpButtons() {
        materialImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithDocuments(EditMaterialFragment.this, getResources().getString(R.string.choose_material_image), 0);
            }
        });
    }

    private void fillInfo() {
        String title = null;

        if (parentFragment.equals(InventoryFragment.class.getSimpleName())) {
            title = "Invetory Material";
        } else if (parentFragment.equals(NeedListFragment.class.getSimpleName())) {
            title = "Need List Material";
        }

        if (oldMaterial != null) {
            title = "Edit " + title;
            //TODO: IMAGE
            //oldImage = oldMaterial.getImage();
            oldName = oldMaterial.getName();
            oldQuantity = oldMaterial.getQuantity();
            oldDescription = oldMaterial.getDescription();
            oldUrgent = oldMaterial.isUrgent();

            //materialImageView.setImage(oldImage);
            nameTextView.setText(oldName);
            quantityTextView.setText(oldQuantity);
            descriptionTextView.setText(oldDescription);
            urgentCheckBox.setChecked(oldUrgent);

            saveTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (parentFragment.equals(InventoryFragment.class.getSimpleName())) {
                        editInvetoryMaterial( getMaterialForm() );
                    } else if (parentFragment.equals(NeedListFragment.class.getSimpleName())) {
                        editNeedListMaterial( getMaterialForm() );
                    }
                }
            });
        } else {
            title = "New " + title;
            //TODO: IMAGE
            //oldImage = oldMaterial.getImage();
            oldName = nameTextView.getText().toString();
            oldQuantity = 0;
            oldDescription = descriptionTextView.getText().toString();;
            urgentCheckBox.setChecked(false);

            saveTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!canCreate()){
                        Toast.makeText(getActivity(), R.string.missing_material_parameter, Toast.LENGTH_SHORT).show();
                    } else {
                        if (parentFragment.equals(InventoryFragment.class.getSimpleName())) {
                            addInvetoryMaterial( getMaterialForm() );
                        } else if (parentFragment.equals(NeedListFragment.class.getSimpleName())) {
                            addNeedListMaterial( getMaterialForm() );
                        }
                    }
                }
            });
        }
        titleTextView.setText(title);
    }

    private Material getMaterialForm() {
        return new Material(
                null,
                null,
                nameTextView.getText().toString(),
                descriptionTextView.getText().toString(),
                urgentCheckBox.isChecked(),
                Integer.parseInt(quantityTextView.getText().toString()),
                null);
    }

    public void addNeedListMaterial(Material material) {
        //TODO: ACABAR ESTA LLAMADA
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<String> call = client.addNeedListMaterial();
        disableButtons();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());

                    // if item is saved correctly
                    ((ProjectMenuActivity)getActivity()).back();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Toast.makeText(getActivity(), R.string.needList_material_add_failed, Toast.LENGTH_SHORT).show();
                    activeButtons();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

                activeButtons();
            }
        });
    }

    public void addInvetoryMaterial(Material material) {
        //TODO: ACABAR ESTA LLAMADA
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<String> call = client.addInvetoryMaterial();
        disableButtons();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());

                    // if item is saved correctly
                    ((ProjectMenuActivity)getActivity()).back();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Toast.makeText(getActivity(), R.string.inventory_material_add_failed, Toast.LENGTH_SHORT).show();
                    activeButtons();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

                activeButtons();
            }
        });
    }

    public void editNeedListMaterial(Material material) {
        //TODO: ACABAR ESTA LLAMADA
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<String> call = client.editNeedListMaterial();
        disableButtons();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());

                    // if item is saved correctly
                    ((ProjectMenuActivity)getActivity()).back();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Toast.makeText(getActivity(), R.string.needList_material_save_failed, Toast.LENGTH_SHORT).show();
                    activeButtons();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

                activeButtons();
            }
        });
    }

    public void editInvetoryMaterial(Material material) {
        //TODO: ACABAR ESTA LLAMADA
        ProjectClient client = retrofit.create(ProjectClient.class);
        Call<String> call = client.editInvetoryMaterial();
        disableButtons();

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());

                    // if item is saved correctly
                    ((ProjectMenuActivity)getActivity()).back();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Toast.makeText(getActivity(), R.string.inventory_material_save_failed, Toast.LENGTH_SHORT).show();
                    activeButtons();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

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

    public void onBackPressed() {
        if (materialChange()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage(R.string.changes_not_saved)
                    .setPositiveButton(R.string.Yes, dialogClickListener)
                    .setNegativeButton(R.string.No, dialogClickListener).show();
        }
        else ((ProjectMenuActivity)getActivity()).back();;
    }

    private boolean materialChange() {
        // check if something changes
        return false;
    }

    private boolean canCreate() {
        //TODO: IMAGE
        final String newName = nameTextView.getText().toString();
        final String newDescription = descriptionTextView.getText().toString();

        if (newName.isEmpty() || newDescription.isEmpty()){
            return false;
        }
        return true;
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
                .resize(materialImageView.getWidth(), materialImageView.getHeight())
                .centerCrop()
                .into(materialImageView);
    }

    private void disableButtons(){
        saveTextView.setEnabled(false);
        closeButton.setEnabled(false);
    }

    private void activeButtons(){
        saveTextView.setEnabled(true);
        closeButton.setEnabled(true);
    }
}
