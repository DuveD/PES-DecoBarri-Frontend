package com.decobarri.decobarri.drawe_menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.BaseActivity;
import com.decobarri.decobarri.Login;
import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Image;
import com.decobarri.decobarri.db_resources.Password;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.main_menu.MainMenuActivity;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountSettingsActivity extends AppCompatActivity implements ProfileFragmentInteraction {

    User user; //user logged
    String username, password;
    String name, email;
    boolean success;
    ViewPager ViewPager;
    ProgressDialog progressDialog;
    User new_user;
    String old_password, new_password;
    Fragment f;
    String currentFragment, new_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        ViewPager = (ViewPager) findViewById(R.id.AccountViewPager);
        toolbar.setTitle("My Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
        password = pref.getString("password", "");

        if(Objects.equals(username, "") && Objects.equals(password, "")){
            Toast.makeText(this, "Not logged", Toast.LENGTH_LONG).show();
        }
        else {
            cargar_datos();
        }
    }

    private void cargar_datos(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<User> call = client.FindByID(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    user = response.body();
                    name = user.getName();
                    email = user.getEmail();

                    Bundle args = new Bundle();
                    args.putString("id", username);
                    args.putString("password", password);
                    args.putString("name", name);
                    args.putString("email", email);
                    args.putString("image", user.getImage());
                    args.putBoolean("success", success);

                    currentFragment = "userProfile";

                    f = new UserProfileFragment();
                    f.setArguments(args);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.Framelayout, f);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AccountSettingsActivity.this, "Error", Toast.LENGTH_LONG).show();
                System.out.println("Error call : " + call.request().toString());
                System.
                        out.println("Error throwable: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void ChangeFragment (Integer mode){
        f = null;
        switch (mode){
            case 1:
                f = new EditProfileFragment();
                currentFragment= "editProfile";
                break;
            case 2:
                f = new UserProfileFragment();
                currentFragment= "userProfile";
                break;
            case 3:
                f = new UserProfileFragment();
                currentFragment= "userProfile";
                break;
            case 4:
                f = new EditPasswordFragment();
                currentFragment= "editPassword";
                break;
        }

        Bundle b = new Bundle();
        b.putString("id", username);
        b.putString("password", password);
        b.putString("name", name);
        b.putString("email", email);
        b.putBoolean("success", success);
        f.setArguments(b);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Framelayout, f);
        fragmentTransaction.commit();
    }

    public boolean EditUser(User u) {
        success=true;
        String new_name = u.getName();
        String new_email = u.getEmail();

        if (new_name.isEmpty()) new_name = name;
        if (new_email.isEmpty()) new_email = email;

        new_user = new User();
        new_user.setName(new_name);
        new_user.setEmail(new_email);
        new_user.setImage(u.getImage());


        EditUserTask et = new EditUserTask();
        et.execute();
        try {
            et.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("success: " + success);

        return success;
    }

    class EditUserTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AccountSettingsActivity.this, R.style.MyTheme);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            MultipartBody.Part body = null;
            if (new_image!=null) {
                File file = new File(new_image);
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            }

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(getApplicationContext().getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
            Retrofit retrofit = builder.build();
            UserClient client = retrofit.create(UserClient.class);

            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), new_user.getName());
            final RequestBody mail = RequestBody.create(MediaType.parse("text/plain"), new_user.getEmail());

            Call<String> call = client.EditUser(username, new_user);

            final MultipartBody.Part finalBody = body;
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    System.out.println("Edit user: " + response.code());
                    System.out.println("Edit user: " + response.message());
                    if(response.isSuccessful()) {
                        success = true;
                        System.out.println("Edit user: " + response.errorBody());
                        progressDialog.dismiss();
                        cargar_datos();
                    }
                    else {
                        success = false;
                        FragmentManager fm = getSupportFragmentManager();
                        EditProfileFragment fragment = (EditProfileFragment) fm.findFragmentById(R.id.EditProfileFragment);
                        fragment.error();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    System.out.println("Edit user error: " + t.getMessage());
                    success=false;
                    progressDialog.dismiss();
                }
            });
            return null;
        }
    }

    public boolean getSuccess (){
        return success;
    }

    public boolean EditPassword(String username, String old_pass, String new_pass) {
        success=false;
        old_password=old_pass;
        new_password=new_pass;

        if (!old_password.equals("") && !new_password.equals("")) {
            EditPasswordTask ep = new EditPasswordTask();
            try {
                ep.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    class EditPasswordTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AccountSettingsActivity.this, R.style.MyTheme);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.getWindow().setGravity(Gravity.CENTER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(getApplicationContext().getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
            Retrofit retrofit = builder.build();
            UserClient client = retrofit.create(UserClient.class);

            Password p = new Password(old_password, new_password);
            Call<String> call = client.EditPassword(username, p);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        success = true;
                        progressDialog.dismiss();
                        cargar_datos();
                    }
                    else {
                        success = false;
                        FragmentManager fm = getSupportFragmentManager();
                        EditPasswordFragment fragment = (EditPasswordFragment) fm.findFragmentById(R.id.EditPasswordFragment);
                        fragment.error();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        switch(currentFragment){
            case "editProfile":
                ChangeFragment(3);
                break;
            case "editPassword":
                ChangeFragment(1);
                break;
            case "userProfile":
                Intent i = new Intent(AccountSettingsActivity.this, MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
                break;
            default:
                break;
        }
    }

}
