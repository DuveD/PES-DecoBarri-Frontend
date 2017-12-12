package com.decobarri.decobarri.project_menu.edit_items;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.activity_resources.Const;

import pl.aprilapps.easyphotopicker.EasyImage;

public class EditNoteActivity extends AppCompatActivity {

    private String title;
    private Boolean edit;
    private int id;

    private TextView saveChanges;
    private LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        initVars();
        setUpNavBar();
        setUpButtons();
    }

    private void initVars() {

        //title = "Item";
        edit = getIntent().getBooleanExtra(Const.EDIT, false);

        saveChanges = (TextView) findViewById(R.id.saveChanges);
        background = (LinearLayout) findViewById(R.id.note_layout);

        setSource();
    }

    private void setSource() {
        if (edit) {
            title = "Edit " + title;
            saveChanges.setText("Save Note");
            id = getIntent().getIntExtra(Const.ID, -1);
            fillInfo();
        } else {
            title = "New " + title;
            saveChanges.setText("Add Note");
        }
    }

    private void fillInfo() {
    }

    private void setUpNavBar(){
        ((TextView) findViewById(R.id.toolbar_title)).setText(title);
        ((ImageButton) findViewById(R.id.toolbar_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (changes()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
            builder.setMessage("Changes not saved. Exit?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        else super.onBackPressed();
    }

    private boolean changes() {
        // check if something changes
        return false;
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private void setUpButtons() {

        findViewById(R.id.saveChanges).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.note_fab_white).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setBackgroundColor(getResources().getColor(R.color.FABwhite));
            }
        });
        findViewById(R.id.note_fab_red).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setBackgroundColor(getResources().getColor(R.color.FABred));
            }
        });
        findViewById(R.id.note_fab_orange).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setBackgroundColor(getResources().getColor(R.color.FABorange));
            }
        });
        findViewById(R.id.note_fab_yellow).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setBackgroundColor(getResources().getColor(R.color.FAByellow));
            }
        });
        findViewById(R.id.note_fab_green).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setBackgroundColor(getResources().getColor(R.color.FABgreen));
            }
        });
        findViewById(R.id.note_fab_blue).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                background.setBackgroundColor(getResources().getColor(R.color.FABblue));
            }
        });
    }
}
