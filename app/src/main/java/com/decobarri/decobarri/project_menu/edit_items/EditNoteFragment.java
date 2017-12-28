package com.decobarri.decobarri.project_menu.edit_items;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Materials.Material;
import com.decobarri.decobarri.activity_resources.Notes.Note;
import com.decobarri.decobarri.db_resources.MaterialsInterface;
import com.decobarri.decobarri.project_menu.NotesFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditNoteFragment extends Fragment {

    private Note note;
    private View view;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private LinearLayout backgroundLayout;
    private LinearLayout buttonsBackgroundLayout;


    private String title;
    private String description;
    private String date;
    private String author;
    private Boolean modifiable;
    private int color;

    private Retrofit retrofit;

    private static final String TAG = EditNoteFragment.class.getSimpleName();

    public static EditNoteFragment newInstance(Note note) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", (Serializable) note);

        EditNoteFragment fragment = new EditNoteFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public static EditNoteFragment newInstance() {
        EditNoteFragment fragment = new EditNoteFragment();
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
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            note = (Note) bundle.getSerializable("note");
            if (note == null) Log.e(TAG, "Bundle read error, Note is null!");
        } else {
            note = null;
            Log.i(TAG, "Bundle is empty");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(TAG);
        backgroundLayout = (LinearLayout) view.findViewById(R.id.note_layout);
        buttonsBackgroundLayout = (LinearLayout) view.findViewById(R.id.buttons_layout);
        titleEditText = (EditText) view.findViewById(R.id.note_title);
        descriptionEditText = (EditText) view.findViewById(R.id.note_description);
        setUpNavBar();
        setUpButtons();
        fillInfo();
        return view;
    }

    private void setUpNavBar(){
        ((ImageButton) view.findViewById(R.id.toolbar_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((ImageButton) view.findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void setUpButtons() {

        view.findViewById(R.id.note_fab_white).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABwhite));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABwhite));
            }
        });
        view.findViewById(R.id.note_fab_red).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABred));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABred));
            }
        });
        view.findViewById(R.id.note_fab_orange).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABorange));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABorange));
            }
        });
        view.findViewById(R.id.note_fab_yellow).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FAByellow));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FAByellow));
            }
        });
        view.findViewById(R.id.note_fab_green).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABgreen));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABgreen));
            }
        });
        view.findViewById(R.id.note_fab_blue).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABblue));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABblue));
            }
        });
    }

    private void fillInfo(){
        if (note != null) {
            title = note.getTitle();
            description = note.getDescription();
            color = note.getColor();
            titleEditText.setContentDescription(title);
            descriptionEditText.setContentDescription(description);
            backgroundLayout.setBackgroundColor(color);
            buttonsBackgroundLayout.setBackgroundColor(color);
        } else {
            title = titleEditText.getText().toString();
            description = descriptionEditText.getText().toString();
            color = ((ColorDrawable)backgroundLayout.getBackground()).getColor();
        }
    }

    public void saveNote() {

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
        final String newTitle = titleEditText.getText().toString();
        final String newDescription = descriptionEditText.getText().toString();
        if (!newTitle.equals(title)){
            Log.i(TAG, "Title edited.");
            return true;
        } else if (!newDescription.equals(description)){
            Log.i(TAG, "Description edited");
            return true;
        } else if ((((ColorDrawable) backgroundLayout.getBackground()).getColor() != color) &&
                (!newTitle.isEmpty() || !newDescription.isEmpty())){
            Log.i(TAG, "Color edited");
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
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(NotesFragment.class.getSimpleName());
        super.onDestroyView();
    }
}
