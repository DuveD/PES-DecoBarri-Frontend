package com.decobarri.decobarri.project_menu;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;
import com.decobarri.decobarri.activity_resources.Notes.Note;
import com.decobarri.decobarri.activity_resources.Notes.NotesAdapter;
import com.decobarri.decobarri.db_resources.NotesInterface;
import com.decobarri.decobarri.project_menu.edit_items.EditMaterialActivity;
import com.decobarri.decobarri.project_menu.edit_items.EditNoteActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private Menu menu;

    private String project_id;

    private List<Note> notesList;
    private static Boolean updatingNotes;
    private static final String TAG = NotesFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.fabPlus).setOnClickListener(this);
        initVars();
    }

    private void initVars() {
        notesList = new ArrayList<>();
        project_id = ((ProjectMenuActivity)this.getActivity()).projectId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_notes, container, false);
        ((TextView) getActivity().findViewById(R.id.Toolbar_title)).setText("Notes");
        return view;
    }

    @Override
    public void onDestroyView() {
        stopUpdatingAnimation();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.notes_recycler);
        emptyView = (LinearLayout) getView().findViewById(R.id.empty_notes_layout);
        fillContentList();
        setContentView();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.reload_menu_gray, optionsMenu);
        menu = optionsMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                fillContentList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setContentView() {
        setVisibleList();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotesAdapter(
                notesList,
                recyclerView,
                getActivity()){

            @Override
            public void customNotifyDataSetChanged(){
                setVisibleList();
                super.customNotifyDataSetChanged();
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setVisibleList() {
        if (notesList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public void startUpdatingAnimation() {
        // Get our refresh item from the menu if it are initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem != null && menuItem.getActionView() == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout iv = (RelativeLayout) inflater.inflate(R.layout.ic_refresh_gray, null);
                Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_refresh);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                menuItem.setActionView(iv);
            }
        }
    }

    public void stopUpdatingAnimation() {
        // Get our refresh item from the menu if it are initialized
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_refresh);
            if (menuItem != null && menuItem.getActionView() != null) {
                menuItem.getActionView().clearAnimation();
                menuItem.setActionView(null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), EditNoteActivity.class);
        intent.putExtra(Const.ID, "ITEM_ID");
        this.startActivity(intent);
    }

    // Recargamos nuestro ArrayList con el contenido actualizado con llamadas a servidor
    public void fillContentList() {
        updatingNotes = true;
        startUpdatingAnimation();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder
                .client(httpClient.build())
                .build();
        NotesInterface client =  retrofit.create(NotesInterface.class);


        Call<List<Note>> call = client.contentList(project_id);

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                // The network call was a success and we got a response
                if (response.isSuccessful()) {
                    Log.i(TAG, "Success : " + response.body());
                    notesList = response.body();
                    Collections.sort(notesList, new Comparator<Note>() {
                        @Override
                        public int compare(Note noteA, Note noteB) {
                            int boolean_compare = noteA.getDate().compareToIgnoreCase(noteB.getDate());;
                            if (boolean_compare == 0)
                                return noteA.getTitle().compareToIgnoreCase(noteB.getTitle());
                            else return boolean_compare;
                        }
                    });
                    setContentView();
                }
                else {
                    Log.e(TAG, "Response failed: " + response.body());
                }
                updatingNotes = false;
                stopUpdatingAnimation();
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

                updatingNotes = false;
                stopUpdatingAnimation();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
