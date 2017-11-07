package com.decobarri.decobarri.drawe_menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountSettingsActivity extends AppCompatActivity {

    User user;
    TextView id, name, email, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        toolbar.setTitle("My Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id = (TextView) findViewById(R.id.textView1);
        name = (TextView) findViewById(R.id.textView3);
        email = (TextView) findViewById(R.id.textView5);
        city = (TextView) findViewById(R.id.textView7);

        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        final String username = pref.getString("username", "");
        String password = pref.getString("password", "");

        if(Objects.equals(username, "") && Objects.equals(password, "")){
            Toast.makeText(this, "Not logged", Toast.LENGTH_LONG).show();
        }
        else {
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
                    id.setText(user.getId());
                    name.setText(user.getName());
                    email.setText(user.getEmail());
                    city.setText("BCN");
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(AccountSettingsActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
