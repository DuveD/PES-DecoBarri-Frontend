package com.decobarri.decobarri.activity_resources.Notes;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.NotesClient;
import com.decobarri.decobarri.project_menu.NotesFragment;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.decobarri.decobarri.project_menu.edit_items.EditNoteFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotesAdapter
        extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>
        implements View.OnClickListener {

    private List<Note> noteList;
    private RecyclerView recyclerView;
    private Context context;

    private Retrofit retrofit;
    private String projectID;

    private static final String TAG = NotesAdapter.class.getSimpleName();

    public NotesAdapter(List<Note> noteList, RecyclerView recyclerView, Context context) {
        this.noteList = noteList;
        this.recyclerView = recyclerView;
        this.context = context;
        retrofit = ((ProjectMenuActivity)context).retrofit;
        projectID = ((ProjectMenuActivity)context).projectID;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView note;
        TextView title;
        TextView description;
        TextView date;
        CardView background;

        NotesViewHolder(View view) {
            super(view);
            note = (CardView) view.findViewById(R.id.note_cardView);
            title = (TextView) view.findViewById(R.id.note_title);
            description = (TextView) view.findViewById(R.id.note_description);
            date = (TextView) view.findViewById(R.id.note_date);
            background = (CardView) view.findViewById(R.id.note_cardView);
        }
    }

    public Note deleteNoteOnList(int index) {
        return noteList.remove(index);
    }

    public Note getNoteOnList (int index) {
        return noteList.get(index);
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note, parent, false);
        view.setOnClickListener(this);
        NotesAdapter.NotesViewHolder noteHolder = new NotesAdapter.NotesViewHolder(view);
        return noteHolder;
    }

    @Override
    public void onBindViewHolder(final NotesViewHolder viewHolder, int position) {

        /* SET NOTE TITLE */
        viewHolder.title.setText(noteList.get(position).getTitle());

        /* SET NOTE DESCRIPTION */
        viewHolder.description.setText(noteList.get(position).getDescription());

        /* SET NOTE DATE */
        viewHolder.date.setText(noteList.get(position).getDate());

        /* SET NOTE BACKGROUND COLOR */
        viewHolder.background.setBackgroundColor(
                getColor(noteList.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return (null != noteList ? noteList.size() : 0);
    }

    @Override
    public void onClick(View view) {
        final int itemPosition = recyclerView.getChildLayoutPosition(view);

        CharSequence[] items = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Select an action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item){
                    case 0:
                        onClickEdit(itemPosition);
                        break;
                    case 1:
                        onClickDelete(itemPosition);
                        break;
                }
            }
        });
        builder.show();
    }

    private void onClickEdit( int itemPosition ) {
        Log.i(TAG, "Edit Note");
        ProjectMenuActivity activity = (ProjectMenuActivity)context;
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.add(R.id.editFragmentsLayout, EditNoteFragment.newInstance(getNoteOnList(itemPosition)));
        transaction.addToBackStack(null);
        transaction.commit();
        customNotifyDataSetChanged();
    }

    private void onClickDelete( int itemPosition ) {
        Log.i(TAG, "Delete Note");
        deleteNote(itemPosition);
        customNotifyDataSetChanged();
    }

    public void deleteNote(final int itemPosition) {

        NotesClient client = retrofit.create(NotesClient.class);

        // TODO: Acabar esta llamada
        Log.i(TAG, projectID
                + " " + getNoteOnList(itemPosition).getId()
                + " " + getNoteOnList(itemPosition).getTitle()
                + " " + getNoteOnList(itemPosition).getDescription()
                + " " + getNoteOnList(itemPosition).getAuthor()
                + " " + getNoteOnList(itemPosition).getModifiable()
                + " " + getNoteOnList(itemPosition).getDate()
                + " " + getNoteOnList(itemPosition).getColor());

        Call<String> call = client.deleteNote(projectID, getNoteOnList(itemPosition));

        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // The network call was a success and we got a response
                Log.i(TAG, "Call successful: " + call.request());
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    Log.i(TAG, "Success : " + response.body());
                    deleteNoteOnList(itemPosition);
                    customNotifyDataSetChanged();
                }
                else {
                    Log.i(TAG, "Response "+response.code() + ": " + response.message());
                    if (response.body() != null)
                        Log.i(TAG, response.body());

                    Toast.makeText(context, R.string.note_delete_failed, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void customNotifyDataSetChanged(){
        notifyDataSetChanged();
        NotesFragment fragment = (NotesFragment) ((Activity)context).getFragmentManager().findFragmentById(R.id.projectFragmentsLayout);
        fragment.setVisibleList();
    }

    public int getColor (String color){
        int tmpColor = context.getResources().getColor(R.color.FABwhite);
        switch (color){
            default:
            case "white":
                tmpColor = context.getResources().getColor(R.color.FABwhite);
                break;
            case "red":
                tmpColor = context.getResources().getColor(R.color.FABred);
                break;
            case "orange":
                tmpColor = context.getResources().getColor(R.color.FABorange);
                break;
            case "yellow":
                tmpColor = context.getResources().getColor(R.color.FAByellow);
                break;
            case "green":
                tmpColor = context.getResources().getColor(R.color.FABgreen);
                break;
            case "blue":
                tmpColor = context.getResources().getColor(R.color.FABblue);
                break;

        }
        return tmpColor;
    }
}