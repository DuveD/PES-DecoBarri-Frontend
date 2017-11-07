package com.decobarri.decobarri;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.db_resources.DB_library;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.main_menu.MainMenuActivity;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

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

            u = new User(user, pass);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(this.getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();
            UserClient client = retrofit.create(UserClient.class);
            Call<String> call = client.LoginUser(u);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()) {
                        Intent i = new Intent(Login.this, MainMenuActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
        }

        /*
        if (!Objects.equals(user, "") && !Objects.equals(pass, "")){
            httpDBlibrary = new DB_library(this);
            String param = "_id=" + user + "&password=" + pass;
            String result = httpDBlibrary.db_call(this.getResources().getString(R.string.LOGIN), param, "POST");
            if(!Objects.equals(result, "404")&&!Objects.equals(result, "500")&&!Objects.equals(result, "401")){
                Intent i = new Intent(this, MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }*/
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

        error.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            u = new User(user, pass);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(getApplicationContext().getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            UserClient client = retrofit.create(UserClient.class);
            Call<String> call = client.LoginUser(u);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    System.out.println(response.body());
                    if (response.isSuccessful()) {
                        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", user);
                        editor.putString("password", pass);
                        editor.apply();
                        Intent i = new Intent(Login.this, MainMenuActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(i);
                    } else {
                        error.setText("Incorrect user or password");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    error.setText("Error");
                    System.out.println(t.toString());
                }
            });
            return null;
        }
    }

    public void registre(View v) {
        startActivity(new Intent(this, Register.class));
    }
}
