package com.decobarri.decobarri.project_menu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.DelayAutoCompleteTextView;
import com.decobarri.decobarri.activity_resources.GeoAutocompleteAdapter;
import com.decobarri.decobarri.activity_resources.GeoSearchResult;
import com.decobarri.decobarri.activity_resources.Items.Item;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.drawe_menu.EditProfileFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.decobarri.decobarri.R;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private MapView mMapView;
    private GoogleMap googleMap;
    private String projectId;
    private MarkerOptions marker;
    private  Marker myMarker;
    private boolean firstMarker;
    private Geocoder geocoder;
    private ImageButton searchButton;

    private Integer THRESHOLD = 2;
    private DelayAutoCompleteTextView geo_autocomplete;


    private String projectID;

    private static final String TAG = MapFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    private void initVars() {
        projectID = ((ProjectMenuActivity)this.getActivity()).projectID;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_map, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Map");
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);

        firstMarker = true;

        projectId = ((ProjectMenuActivity)this.getActivity()).projectID;

        searchButton = (ImageButton) view.findViewById(R.id.SearchLocationButton);
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
        mMapView = (MapView) view.findViewById(R.id.map);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        initMap();


        //Search addresses

        geo_autocomplete = (DelayAutoCompleteTextView) view.findViewById(R.id.geo_autocomplete);
        geo_autocomplete.setThreshold(THRESHOLD);
        geo_autocomplete.setAdapter(new GeoAutocompleteAdapter(getActivity())); // 'this' is Activity instance

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

        return view;
    }

    private void initMap () {

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
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

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(getActivity().getResources().getString(R.string.db_URL))
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
                Retrofit retrofit = builder.build();
                ProjectClient client = retrofit.create(ProjectClient.class);

                Call<Project> call = client.FindProjectById(projectId);
                call.enqueue(new Callback<Project>() {
                    @Override
                    public void onResponse(Call<Project> call, Response<Project> response) {
                        LatLng coord = new LatLng(Integer.parseInt(response.body().getLat()), Integer.parseInt(response.body().getLng()));
                        googleMap.addMarker(new MarkerOptions().position(coord));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(coord).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }

                    @Override
                    public void onFailure(Call<Project> call, Throwable t) {

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
