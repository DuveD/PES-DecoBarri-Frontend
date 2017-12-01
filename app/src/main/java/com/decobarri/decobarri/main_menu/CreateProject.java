package com.decobarri.decobarri.main_menu;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.decobarri.decobarri.R;

/**
 * Created by Marc G on 13/11/2017.
 */

public class CreateProject extends AppCompatActivity {

    private EditText input_name, input_description, input_theme;
    private TextInputLayout inputLayoutName, inputLayoutDescription, inputLayoutTheme;
    private Button button_create;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutDescription = (TextInputLayout) findViewById(R.id.input_layout_description);
        inputLayoutTheme = (TextInputLayout) findViewById(R.id.input_layout_theme);
        input_name = (EditText) findViewById(R.id.input_project_name);
        input_description = (EditText) findViewById(R.id.input_description);
        input_theme = (EditText) findViewById(R.id.input_theme);
        button_create = (Button) findViewById(R.id.create_button);

        input_name.addTextChangedListener(new MyTextWatcher(input_name));
        input_description.addTextChangedListener(new MyTextWatcher(input_description));
        input_theme.addTextChangedListener(new MyTextWatcher(input_theme));

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submitForm();
            }
        });
    }
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

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

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

}