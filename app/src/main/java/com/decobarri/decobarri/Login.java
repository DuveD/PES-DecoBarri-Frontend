package com.decobarri.decobarri;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.main_menu.MainMenuActivity;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    TextView error;
    EditText username, password;

    String user;
    String pass;
    ProgressDialog progressDialog;
    User u;
    Button login;

    @Override
    protected void onStart() {
        final SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        user = pref.getString("username", "");
        pass = pref.getString("password", "");
        if (!Objects.equals(user, "") && !Objects.equals(pass, "")){
            MyTask task = new MyTask();
            try {
                task.execute(4).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");

        setContentView(R.layout.activity_login);
        error = (TextView) findViewById(R.id.textView3);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setVisibility(View.INVISIBLE);
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setVisibility(View.INVISIBLE);
            }
        });

        login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar_sessio(view);
            }
        });
    }

    public void iniciar_sessio(View v)  {

        user = username.getText().toString();
        pass = password.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            error.setVisibility(View.VISIBLE);
        } else {
            MyTask task = new MyTask();
            task.execute(4);
        }
    }

    private class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Login.this, R.style.MyTheme);
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            ScrollView screen = (ScrollView) findViewById(R.id.screen);
            //screen.setBackgroundColor(Login.this.getResources().getColor(R.color.));

            screen.setAnimation(new AlphaAnimation(1f, 0f));


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //if (progressDialog.isShowing()) progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            u = new User(user, pass);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(getApplicationContext().getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
            Retrofit retrofit = builder.build();
            UserClient client = retrofit.create(UserClient.class);
            Call<String> call = client.LoginUser(u);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        System.out.println("Success : " + response.body());
                        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", user);
                        editor.putString("password", pass);
                        editor.apply();
                        Intent i = new Intent(Login.this, MainMenuActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(i);
                    } else {
                        System.out.println("Error: " + response.body());
                        error.setText("Incorrect user or password");
                        error.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                    findViewById(R.id.screen).setBackground(Drawable.createFromPath("@color/transparent"));
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                    findViewById(R.id.screen).setBackground(Drawable.createFromPath("@color/transparent"));
                    error.setText("Error");
                    error.setVisibility(View.VISIBLE);
                    System.out.println("Error call : " + call.request().toString());
                    System.out.println("Error throwable: " + t.getMessage());
                }
            });
            return null;
        }
    }

    public void registre(View v) {
        startActivity(new Intent(this, Register.class));
    }
}
