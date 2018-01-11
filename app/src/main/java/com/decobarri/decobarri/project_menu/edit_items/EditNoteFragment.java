package com.decobarri.decobarri.project_menu.edit_items;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Notes.Note;
import com.decobarri.decobarri.db_resources.NotesClient;
import com.decobarri.decobarri.project_menu.NotesFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class EditNoteFragment extends Fragment {

    private View view;
    private String projectID;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private LinearLayout backgroundLayout;
    private LinearLayout buttonsBackgroundLayout;
    private ImageButton closeButton;
    private ImageButton saveButton;

    private Note oldNote;
    private String oldTitle;
    private String oldDescription;
    private String oldColor;

    private String actualColor;

    private Retrofit retrofit;

    private static final String TAG = EditNoteFragment.class.getSimpleName();

    public static EditNoteFragment newInstance(Note note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("oldNote", note);

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
        projectID = ((ProjectMenuActivity)this.getActivity()).projectID;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            oldNote = (Note) bundle.getParcelable("oldNote");
            if (oldNote == null) Log.e(TAG, "Bundle read error, Note is null!");
        } else {
            oldNote = null;
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
        saveButton = (ImageButton) view.findViewById(R.id.save_button);
        closeButton = (ImageButton) view.findViewById(R.id.toolbar_button);
        setUpNavBar();
        setUpButtons();
        fillInfo();
        return view;
    }

    private void setUpNavBar(){
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setUpButtons() {

        view.findViewById(R.id.note_fab_white).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABwhite));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABwhite));
                actualColor = "white";
            }
        });
        view.findViewById(R.id.note_fab_red).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABred));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABred));
                actualColor = "red";
            }
        });
        view.findViewById(R.id.note_fab_orange).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABorange));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABorange));
                actualColor = "orange";
            }
        });
        view.findViewById(R.id.note_fab_yellow).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FAByellow));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FAByellow));
                actualColor = "yellow";
            }
        });
        view.findViewById(R.id.note_fab_green).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABgreen));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABgreen));
                actualColor = "green";
            }
        });
        view.findViewById(R.id.note_fab_blue).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABblue));
                buttonsBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FABblue));
                actualColor = "blue";
            }
        });
    }

    private void fillInfo(){
        if (oldNote != null) {
            oldTitle = oldNote.getTitle();
            oldDescription = oldNote.getDescription();
            oldColor = oldNote.getColor();
            actualColor = oldNote.getColor();
            titleEditText.setText(oldTitle);
            descriptionEditText.setText(oldDescription);
            backgroundLayout.setBackgroundColor(getColor(oldColor));
            buttonsBackgroundLayout.setBackgroundColor(getColor(oldColor));

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveNote( getNoteForm() );
                }
            });
        } else {
            oldNote = new Note();
            oldTitle = titleEditText.getText().toString();
            oldDescription = descriptionEditText.getText().toString();
            oldColor = "white";
            actualColor = "white";

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(!canCreate()){
                    Toast.makeText(getActivity(), R.string.missing_note_parameter, Toast.LENGTH_SHORT).show();
                } else {
                    addNote( getNoteForm() );
                }
                }
            });
        }
    }

    private Note getNoteForm() {
        SharedPreferences pref = getActivity().getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        String username = pref.getString("username", "");

        Log.i(TAG, projectID
                + " " + oldNote.getId()
                + " " + titleEditText.getText().toString()
                + " " + oldNote.getDate()
                + " " + descriptionEditText.getText().toString()
                + " " + username
                + " " + oldNote.getModifiable()
                + " " + actualColor);


        return new Note (   oldNote.getId(),
                            titleEditText.getText().toString(),
                            oldNote.getDate(),
                            descriptionEditText.getText().toString(),
                            username,
                            oldNote.getModifiable(),
                            actualColor);
    }

    public void addNote(Note note) {
        disableButtons();

        NotesClient client = retrofit.create(NotesClient.class);
        Call<String> call = client.addNote(projectID, note);

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
                    Toast.makeText(getActivity(), R.string.note_save_failed, Toast.LENGTH_SHORT).show();
                    activeButtons();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // the network call was a failure
                Log.e(TAG, "Call failed: " + call.request());
                Log.e(TAG, "Error: " + t.getMessage());

                Toast.makeText(getActivity(), R.string.note_save_failed, Toast.LENGTH_SHORT).show();

                if (!isOnline())  Log.e(TAG, "No internet connection.");
                else Log.w(TAG, "Please check your internet connection and try again.");

                activeButtons();
            }
        });
    }

    public void saveNote(Note note) {
        disableButtons();

        NotesClient client = retrofit.create(NotesClient.class);
        Call<String> call = client.editNote(projectID, note);

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
                    Toast.makeText(getActivity(), R.string.note_save_failed, Toast.LENGTH_SHORT).show();
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
        if (noteChange()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage(R.string.changes_not_saved)
                    .setPositiveButton(R.string.Yes, dialogClickListener)
                    .setNegativeButton(R.string.No, dialogClickListener).show();
        }
        else ((ProjectMenuActivity)getActivity()).back();
    }

    private boolean noteChange() {
        final String newTitle = titleEditText.getText().toString();
        final String newDescription = descriptionEditText.getText().toString();
        if (!newTitle.isEmpty() && !newTitle.equals(oldTitle)){
            Log.i(TAG, "Title edited.");
            return true;
        } else if (!newDescription.isEmpty() && !newDescription.equals(oldDescription)){
            Log.i(TAG, "Description edited");
            return true;
        } else if (!actualColor.equals(oldColor) &&
                (!newTitle.isEmpty() || !newDescription.isEmpty())){
            Log.i(TAG, "Color edited");
            return true;
        }
        return false;
    }

    private boolean canCreate() {
        final String newTitle = titleEditText.getText().toString();
        final String newDescription = descriptionEditText.getText().toString();
        if (newTitle.isEmpty() || newDescription.isEmpty()){
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
        ((ProjectMenuActivity)this.getActivity()).setCurrentFragment(NotesFragment.class.getSimpleName());
        super.onDestroyView();
    }

    private void disableButtons(){
        saveButton.setEnabled(false);
        closeButton.setEnabled(false);
    }

    private void activeButtons(){
        saveButton.setEnabled(true);
        closeButton.setEnabled(true);
    }

    public int getColor (String color){
        int tmpColor = getResources().getColor(R.color.FABwhite);
        switch (color){
            default:
            case "white":
                tmpColor = getResources().getColor(R.color.FABwhite);
                break;
            case "red":
                tmpColor = getResources().getColor(R.color.FABred);
                break;
            case "orange":
                tmpColor = getResources().getColor(R.color.FABorange);
                break;
            case "yellow":
                tmpColor = getResources().getColor(R.color.FAByellow);
                break;
            case "green":
                tmpColor = getResources().getColor(R.color.FABgreen);
                break;
            case "blue":
                tmpColor = getResources().getColor(R.color.FABblue);
                break;

        }
        return tmpColor;
    }
}
