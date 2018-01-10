package com.decobarri.decobarri.main_menu;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Projects.AllProjectsAdapter;
//import com.decobarri.decobarri.activity_resources.Projects.Project;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.ProjectLocation;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProjectsSearchFragment extends Fragment {
    List items = new ArrayList();
    private RecyclerView rec;
    private AllProjectsAdapter adapter;
    private RecyclerView.LayoutManager lmanager;
    private SearchView searchView;
    private Retrofit retrofit;
    private Double lat, lng;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationListener locationListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initVars();
    }

    private void initVars() {
        items = new ArrayList<>();
        retrofit = ((MainMenuActivity) this.getActivity()).retrofit;
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("Location Changes", location.toString());
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
    }

    private void getLocation() {

        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final Looper looper = null;
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestSingleUpdate(locationManager.GPS_PROVIDER, locationListener, looper);
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        if (location !=null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
        /*mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            GoogleApiClient mGoogleApiClient;

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) getActivity())
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) getActivity())
                    .addApi(LocationServices.API).build();

            mGoogleApiClient.connect();
            Location location = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());
            if (coord !=null) {
                lat = coord.latitude;
                lng = coord.longitude;
            }*/
            /*
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());
                    if (coord !=null) {
                        lat = coord.latitude;
                        lng = coord.longitude;
                    }
                }
            });
        }*/

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rec = (RecyclerView) getView().findViewById(R.id.projectsearch_recycler);
        fillList();
        setView();
    }


    private void fillList() {
        getLocation();
        ProjectClient client =  retrofit.create(ProjectClient.class);

        if (lat!=null && lng!=null) {
            System.out.println("lat:" + lat);
            System.out.println("lng:" + lng);
            Project p = new Project();
            p.setLat(lat.toString());
            p.setLng(lng.toString());
            Call<List<ProjectLocation>> call = client.FindProjectsByLocation(p);
            call.enqueue(new Callback<List<ProjectLocation>>() {
                @Override
                public void onResponse(Call<List<ProjectLocation>> call, Response<List<ProjectLocation>> response) {
                    if (response.isSuccessful()) {
                        List<Project> newList = new ArrayList<>();
                        for(ProjectLocation pl : response.body()) {
                            newList.add(pl.getProject());
                        }
                        if(!newList.isEmpty()) items = newList;
                        setView();
                    }
                    System.out.println("Project load code:" + response.code());
                    System.out.println("Project load message:" + response.message());
                }

                @Override
                public void onFailure(Call<List<ProjectLocation>> call, Throwable t) {

                }
            });
        }
        else {
            System.out.println("no location");
            Call<List<Project>> call= client.FindAllProjects();
            call.enqueue(new Callback<List<Project>>() {
                @Override
                public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                    // The network call was a success and we got a response
                    if (response.isSuccessful()) {
                        items = response.body();
                        setView();
                    }
                    System.out.println("Project load code:" + response.code());
                    System.out.println("Project load message:" + response.message());
                }

                @Override
                public void onFailure(Call<List<Project>> call, Throwable t) {
                    //
                }
            });
        }

    }

    private void setView() {
        lmanager = new LinearLayoutManager(getActivity());
        rec.setLayoutManager(lmanager);
        adapter = new AllProjectsAdapter(items, getActivity(), rec);
        rec.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_projects_search, container, false);

        return view;
    }

    public void onCreateTest(View view) {
        String call = "";
        String result = "You are on fragment_my_projects"/*httpDBlibrary.db_call( call )*/;

        Toast toast = Toast.makeText(view.getContext(), result, Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, optionsMenu);

        MenuItem myActionMenuItem = optionsMenu.findItem(R.id.options_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Project> filteredModelList = filter(items, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }


        });
        super.onCreateOptionsMenu(optionsMenu, inflater);
    }


    /*@Override
    public boolean onQueryTextChange(String newText) {
        final List<Project> filteredModelList = filter(items, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }*/

    private List<Project> filter(List<Project>projects, String query) {
        query = query.toLowerCase();final List<Project> filteredModelList = new ArrayList<>();
        for (Project p : projects) {
            final String text = p.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(p);
            }
        }
        return filteredModelList;
    }


}
