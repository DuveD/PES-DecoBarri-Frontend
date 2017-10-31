package com.decobarri.decobarri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.decobarri.decobarri.db_resources.DB_library;
import com.decobarri.decobarri.main_menu.MainMenuActivity;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class Login extends AppCompatActivity {

    TextView error;
    EditText username, password;

    String user;
    String pass;
    DB_library httpDBlibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-----LOGIN anterior------//
        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        user = pref.getString("username", "");
        pass = pref.getString("password", "");
        if (!Objects.equals(user, "") && !Objects.equals(pass, "")){
            httpDBlibrary = new DB_library(this);
            String param = "_id=" + user + "&password=" + pass;
            String result = httpDBlibrary.db_call(this.getResources().getString(R.string.LOGIN), param, "POST");
            if(!Objects.equals(result, "404")&&!Objects.equals(result, "500")&&!Objects.equals(result, "401")){
                Intent i = new Intent(this, MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }

        setTitle("Login");
        setContentView(R.layout.activity_login);
        error = (TextView) findViewById(R.id.textView3);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        error.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                error.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void iniciar_sessio(View v) throws UnsupportedEncodingException {

        user = username.getText().toString();
        pass = password.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            error.setVisibility(View.VISIBLE);
        } else {
            httpDBlibrary = new DB_library(this);
            String param = "_id=" + user + "&password=" + pass;
            String result = httpDBlibrary.db_call(this.getResources().getString(R.string.LOGIN), param, "POST");
            System.out.println(result);
            if (Objects.equals(result, "404")) {
                error.setVisibility(View.VISIBLE);
            } if (Objects.equals(result, "401")) {
                error.setText("Wrong password");
                error.setVisibility(View.VISIBLE);
            }else {
                SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username", user);
                editor.putString("password", pass);
                editor.commit();
                editor.apply();
                Intent i = new Intent(this, MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
    }

    public void registre(View v) {
        startActivity(new Intent(this, Register.class));
    }
}
