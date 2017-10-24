package com.decobarri.decobarri;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.decobarri.decobarri.db_resources.DB_library;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.nio.charset.StandardCharsets;

public class Register extends AppCompatActivity {

    TextView error;
    EditText username, password, name, email;
    DB_library httpDBlibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        error = (TextView) findViewById(R.id.textView4);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);


    }

    public void registro (View v) {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String n = name.getText().toString();
        String e = email.getText().toString();
        if (user.isEmpty()||pass.isEmpty()){
            error.setText("Un dels camps es incorrecte.");
            error.setVisibility(View.VISIBLE);
        }
        else {
            httpDBlibrary = new DB_library(this);

            String result = httpDBlibrary.db_call("/user/findByID/:_id=" + user);
            if (result.isEmpty()) {
                String urlParameters  = "name="+n+"&_id="+user+"&password="+pass+"&email="+e;
                byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                int    postDataLength = postData.length;
                httpDBlibrary.db_call("/user/add");

                ContentValues values = new ContentValues();
                values.put("username", user);
                values.put("password", pass);
                startActivity(new Intent(this, MainMenu.class));
            }
            else {
                error.setVisibility(View.VISIBLE);
            }
        }

    }
}
