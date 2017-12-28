package com.decobarri.decobarri.activity_resources.Notes;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.project_menu.ProjectMenuActivity;
import com.decobarri.decobarri.project_menu.edit_items.EditNoteFragment;

import java.util.List;

public class NotesAdapter
        extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>
        implements View.OnLongClickListener {

    private List<Note> noteList;
    private RecyclerView recyclerView;
    private Context context;
    private static final String TAG = NotesAdapter.class.getSimpleName();

    public NotesAdapter(List<Note> noteList, RecyclerView recyclerView, Context context) {
        this.noteList = noteList;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView note;
        TextView title;
        TextView description;
        TextView date;
        LinearLayout background;

        NotesViewHolder(View view) {
            super(view);
            note = (CardView) view.findViewById(R.id.note_cardView);
            title = (TextView) view.findViewById(R.id.note_title);
            description = (TextView) view.findViewById(R.id.note_description);
            date = (TextView) view.findViewById(R.id.note_description);
            background = (LinearLayout) view.findViewById(R.id.note_layout);
        }
    }

    public Note deleteNote(int index) {
        return noteList.remove(index);
    }

    public Note getNote (int index) {
        return noteList.get(index);
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_note, parent, false);
        view.setOnLongClickListener(this);
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

        viewHolder.background.setBackgroundColor(noteList.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return (null != noteList ? noteList.size() : 0);
    }

    @Override
    public boolean onLongClick(View view) {
        final int itemPosition = recyclerView.getChildLayoutPosition(view);

        CharSequence[] items = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Select an action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item){
                    case 0:
                        onLongClickEdit(itemPosition);
                        break;
                    case 1:
                        onLongClickDelete(itemPosition);
                        break;
                }
            }
        });
        builder.show();
        return true;
    }

    private void onLongClickEdit( int itemPosition ) {
        Log.i(TAG, "Edit Note");
        ProjectMenuActivity activity = (ProjectMenuActivity)context;
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.add(R.id.DrawerLayout, EditNoteFragment.newInstance(getNote(itemPosition)));
        transaction.addToBackStack(null);
        transaction.commit();
        customNotifyDataSetChanged();
    }

    private void onLongClickDelete( int itemPosition ) {
        Log.i(TAG, "Delete Note");
        deleteNote(itemPosition);
        // TODO: DELETE MATERIAL CALL
        customNotifyDataSetChanged();
    }

    public void customNotifyDataSetChanged(){
        notifyDataSetChanged();
    }
}