package com.decobarri.decobarri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import org.bson.Document;

import java.net.UnknownHostException;

public class Regiter extends AppCompatActivity {

    TextView error;
    EditText username, password;
    MongoClientURI uri;
    MongoClient mongoClient;
    MongoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter);
        error = (TextView) findViewById(R.id.textView4);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        uri = new MongoClientURI( "mongodb://projectePES:PES12L@ds121565.mlab.com:21565/decorbarris" );
        mongoClient = new MongoClient(uri);
        db = mongoClient.getDatabase(uri.getDatabase());
    }

    public void registro (View v) {
        String user = username.getText().toString();
        String pass = password.getText().toString();


    }
}
