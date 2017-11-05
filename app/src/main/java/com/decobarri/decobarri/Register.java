package com.decobarri.decobarri;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.main_menu.MainMenuActivity;
import com.decobarri.decobarri.db_resources.DB_library;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    TextView error;
    EditText username, password, name, email;
    DB_library httpDBlibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        error = (TextView) findViewById(R.id.textView4);
        username = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);
        name = (EditText) findViewById(R.id.editText3);
        email = (EditText) findViewById(R.id.editText4);


    }

    public void registro(View v) {
        final String user = username.getText().toString();
        final String pass = password.getText().toString();
        String n = name.getText().toString();
        String e = email.getText().toString();
        if (user.isEmpty() || pass.isEmpty()) {
            error.setText("Un dels camps és incorrecte.");
            error.setVisibility(View.VISIBLE);
        } else {

            User u = new User(user, n, pass, e);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(this.getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
            UserClient client = retrofit.create(UserClient.class);
            Call<User> call = client.CreateAccount(u);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        ContentValues values = new ContentValues();
                        values.put("username", user);
                        values.put("password", pass);
                        Intent i = new Intent(Register.this, MainMenuActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    else {
                        error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    error.setText("Error");
                    error.setVisibility(View.VISIBLE);
                }
            });

            /*httpDBlibrary = new DB_library(this);
            String param = "_id=" + user + "&password=" + pass + "&name=" + n + "&email=" + e;
            String result = httpDBlibrary.db_call(this.getResources().getString(R.string.ADD_USER), param, "POST");
            if (Objects.equals(result, "409")) {
                error.setVisibility(View.VISIBLE);
            } else if (Objects.equals(result, "500")) {
                error.setText("Internal server error");
                error.setVisibility(View.VISIBLE);
            }
            if (Objects.equals(result, "404")) {
                error.setText("Not found error");
                error.setVisibility(View.VISIBLE);
            } else {
                ContentValues values = new ContentValues();
                values.put("username", user);
                values.put("password", pass);
                Intent i = new Intent(this, MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }*/

        }

    }
}
