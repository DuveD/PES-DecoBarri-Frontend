package com.decobarri.decobarri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.drawe_menu.AccountSettingsActivity;
import com.decobarri.decobarri.drawe_menu.ChatActivity;
import com.decobarri.decobarri.drawe_menu.ContactListActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    public ActionBarDrawerToggle toggle;
    public ImageView profileImage;
    public TextView usernameTV, emailTV;
    public String username;
    public Retrofit retrofit;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configureCall();
        super.onCreate(savedInstanceState);
    }

    private void configureCall(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder
                .client(httpClient.build())
                .build();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
    }

    public void setUpNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        navigationView = (NavigationView) findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences pref = getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
        View header = navigationView.getHeaderView(0);
        profileImage = (ImageView) header.findViewById(R.id.imageView);
        usernameTV = (TextView) header.findViewById(R.id.username_drawer);
        emailTV = (TextView) header.findViewById(R.id.email_drawer);
        setUpUser();
        setUpImage();

        //create default navigation drawer toggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open_description, R.string.navigation_drawer_close_description){
            @Override
            public void onDrawerStateChanged(int newState) {
                onBackPressedExtraAction();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (itemSelected != null) {
                    switch (itemSelected) {
                        case R.id.my_account:
                            startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class));
                            break;
                        case R.id.chat:
                            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                            break;
                        case R.id.contact_list:
                            startActivity(new Intent(getApplicationContext(), ContactListActivity.class));
                            break;
                        case R.id.logout:
                            SharedPreferences settings = getSharedPreferences("LOGGED_USER", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("username", "");
                            editor.putString("password", "");
                            editor.apply();
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            break;
                        default:
                            break;
                    }
                    itemSelected = null;
                }
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpImage() {
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseBody> call = client.downloadImage(username);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Drawer load: " + response.code());
                System.out.println("Drawer load: " + response.message());
                System.out.println("Drawer load: " + response.body());
                if(response.isSuccessful()) {
                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                    System.out.println("Drawer load: " + bm);
                    if (bm!=null)profileImage.setImageBitmap(
                            Bitmap.createScaledBitmap(bm, profileImage.getWidth(), profileImage.getHeight(), false));
                    else profileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_account_image));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setUpUser() {
        UserClient client = retrofit.create(UserClient.class);
        Call<User> call = client.FindByID(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                usernameTV.setText(username);
                emailTV.setText(response.body().getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (onBackPressedExtraAction()) {
        } else {
            super.onBackPressed();
        }
    }

    public Boolean onBackPressedExtraAction() {
        return false;
    }

    private Integer itemSelected;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        itemSelected = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

}
