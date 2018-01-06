package com.decobarri.decobarri.main_menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStats;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.DelayAutoCompleteTextView;
import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.GeoAutocompleteAdapter;
import com.decobarri.decobarri.activity_resources.GeoSearchResult;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

public class CreateProjectActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText input_name, input_description, input_theme;
    private TextInputLayout inputLayoutName, inputLayoutDescription, inputLayoutTheme;
    private Button button_create;
    private ImageView projectImage;
    private String username;

    private MapView mMapView;
    private GoogleMap googleMap;
    private String projectId;
    private MarkerOptions marker;
    private Marker myMarker;
    private boolean firstMarker;
    private Geocoder geocoder;
    private ImageButton searchButton;

    private Integer THRESHOLD = 2;
    private DelayAutoCompleteTextView geo_autocomplete;

    private FusedLocationProviderClient mFusedLocationClient;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_description);
        inputLayoutTheme = (TextInputLayout) findViewById(R.id.input_layout_theme);
        input_name = (EditText) findViewById(R.id.input_project_name);
        input_description = (EditText) findViewById(R.id.input_description);
        input_theme = (EditText) findViewById(R.id.input_theme);
        button_create = (Button) findViewById(R.id.create_button);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SharedPreferences pref = this.getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");

        projectImage = (ImageView) findViewById(R.id.imageProject);
        setImage();

        EasyImage.configuration(this)
            .setCopyTakenPhotosToPublicGalleryAppFolder(false)
            .setCopyPickedImagesToPublicGalleryAppFolder(false)
            .setAllowMultiplePickInGallery(false);

        //Mapa
        firstMarker = true;
        searchButton = (ImageButton) findViewById(R.id.SearchLocationButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loc = geo_autocomplete.getText().toString();
                try {
                    searchLocation(geocoder.getFromLocationName(loc, 1).get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mMapView = (MapView) findViewById(R.id.map);
        geocoder = new Geocoder(this, Locale.getDefault());

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        initMap();
        searchAdress();


        input_name.addTextChangedListener(new MyTextWatcher(input_name));
        input_description.addTextChangedListener(new MyTextWatcher(input_description));
        input_theme.addTextChangedListener(new MyTextWatcher(input_theme));

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //submitForm()
            if (validateName() & validateDescription()) {
                //***********************************************
                //Falta añadir imagen, username y ubicacion (la variable username ya esta inicializada)
                //***********************************************
                LatLng latLng = myMarker.getPosition();
                Double lat = latLng.latitude;
                Double lng = latLng.longitude;
                Project projectCreated = new Project("", input_name.getText().toString(), input_theme.getText().toString(),
                        input_description.getText().toString(), "Barcelona", "FIB", username, lat.toString(), lng.toString());
                creaProjecte(projectCreated);
                //Intent i = new Intent(CreateProjectActivity.this, MainMenuActivity.class);
                //System.out.println("Creado en bd");
                //startActivity(i);

            }
            }
        });
    }

    private void creaProjecte(Project projectCreated) {
        //**********************************************************************

        //Descomentar para crear proyecto en servidor

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
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
                    System.out.println("Success : " + response.body());
                }
                else {
                    System.out.println("Error code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //
                Intent i = new Intent(CreateProjectActivity.this, MainMenuActivity.class);
                System.out.println("Creado en bd");
                startActivity(i);
                System.out.println("Call failed: " + call.request());
            }
        });
    }

    //Validacion de campos vacios

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

    //Imagenes

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

    private void setImage(){
        projectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithDocuments(CreateProjectActivity.this, "Choose an image for your project", 0);
            }
        });
    }

    //MAPA

    private void searchAdress(){
        //Search addresses

        geo_autocomplete = (DelayAutoCompleteTextView) findViewById(R.id.geo_autocomplete);
        geo_autocomplete.setThreshold(THRESHOLD);
        geo_autocomplete.setAdapter(new GeoAutocompleteAdapter(this)); // 'this' is Activity instance

        geo_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
                geo_autocomplete.setText(result.getAddress());
            }
        });

        geo_autocomplete.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initMap () {

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        List<Address> address;
                        try {
                            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            marker = new MarkerOptions().position(latLng)
                                    .title(address.get(0).getAddressLine(0))
                                    .snippet(String.format("%.6f", address.get(0).getLatitude()) + ", " + String.format("%.6f", address.get(0).getLongitude()) );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (firstMarker) {
                            myMarker = googleMap.addMarker(marker);
                            firstMarker = false;
                        }else {
                            myMarker.remove();
                            myMarker = googleMap.addMarker(marker);
                        }
                        myMarker.showInfoWindow();
                    }
                });

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());
                        if (firstMarker) {
                            myMarker = googleMap.addMarker(new MarkerOptions().position(coord));
                            firstMarker = false;
                        }
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(coord).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void searchLocation(Address a){

        LatLng coord = new LatLng(a.getLatitude(), a.getLongitude());

        marker = new MarkerOptions().position(coord)
                .title(a.getAddressLine(0))
                .snippet(String.format("%.6f", a.getLatitude()) + ", " + String.format("%.6f", a.getLongitude()) );


        if(!firstMarker) myMarker.remove();
        myMarker = googleMap.addMarker(new MarkerOptions().position(coord));
        firstMarker = false;
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(coord).zoom(20).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}