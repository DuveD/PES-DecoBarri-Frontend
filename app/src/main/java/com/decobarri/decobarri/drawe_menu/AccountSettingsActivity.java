package com.decobarri.decobarri.drawe_menu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Password;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.db_resources.UserEdit;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountSettingsActivity extends AppCompatActivity implements ProfileFragmentInteraction{

    User user; //user logged
    String username, password;
    String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        toolbar.setTitle("My Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
        password = pref.getString("password", "");

        //-----------Pruebas-------------

        username = "aaa";
        password = "aaa";

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
                user = response.body();
                name = user.getName();
                email = user.getEmail();
                System.out.println("Success: " + name + ", " + email);

                Bundle args = new Bundle();
                args.putString("id", username);
                args.putString("password", password);
                args.putString("name", name);
                args.putString("email", email);

                Fragment f = new UserProfileFragment();
                f.setArguments(args);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Framelayout, f);
                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AccountSettingsActivity.this, "Error", Toast.LENGTH_LONG).show();
                System.out.println("Error call : " + call.request().toString());
                System.out.println("Error throwable: " + t.getMessage());
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public Integer ProfileInteraction (Integer mode, User u, String old_password){
        Fragment f = null;
        Integer code = 0;
        boolean change = true;
        switch (mode){
            case 1:
                //Edit: change fragment
                f = new EditProfileFragment();
                break;
            case 2:
                change=false;
                //save changes
                code = edit_profile(u, old_password);
                //change fragment
                cargar_datos();
                f = new UserProfileFragment();
                if(code == 401){
                    Toast.makeText(this,"Bad password", Toast.LENGTH_SHORT);
                }
                break;
            case 3:
                //change fragment
                f = new UserProfileFragment();
                break;
        }

        if(change) {
            Bundle b = new Bundle();
            b.putString("id", username);
            b.putString("password", password);
            b.putString("name", name);
            b.putString("email", email);
            f.setArguments(b);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.Framelayout, f);
            fragmentTransaction.commit();
        }
        return code;
    }

    @SuppressLint("ShowToast")
    public Integer edit_profile(User new_user, String old_password) {

        final Integer[] code = {0};

        String old_pass = old_password;
        String new_pass = new_user.getPassword();

        if (Objects.equals(new_pass, "")){ //No cambia la pass
            code[0]=edit_user(new_user);
        }
        else {
            if(Objects.equals(old_pass, password)){ //Cambia el user y la pass
                code[0]=edit_user(new_user);
                if(code[0]==200){
                    code[0]=edit_password(username, old_pass, new_pass);
                }
            }
            else { //Quiere cambiar la pass pero ha introducido una old_password diferente
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG);
            }
        }

        return code[0];
    }

    private Integer edit_user(User new_user) {
        final int[] code = new int[1];
        String new_name = new_user.getName();
        String new_email = new_user.getEmail();

        if (new_name.isEmpty()) new_name = name;
        if (new_email.isEmpty()) new_email = email;

        UserEdit u = new UserEdit(new_name, new_email);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getApplicationContext().getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<String> call = client.EditUser(username, u);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                code[0] = response.code();
                System.out.println("Error code: " + response.code());
                System.out.println("Error msg: " + response.message());
                System.out.println("Error body: " + response.body());
                System.out.println("Error errorbody: " + response.errorBody());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        System.out.println("code: " + code[0]);
        return code[0];
    }

    private Integer edit_password(String username, String old_pass, String new_pass) {
        final int[] code = new int[1];

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getApplicationContext().getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);

        Password p = new Password(old_pass, new_pass);
        Call<String> call = client.EditPassword(username, p);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                code[0] = response.code();
                System.out.println("Error code: " + response.code());
                System.out.println("Error msg: " + response.message());
                System.out.println("Error body: " + response.body());
                System.out.println("Error errorbody: " + response.errorBody());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

        return code[0];
    }

}
