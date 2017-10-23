package com.decobarri.decobarri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.Objects;

import org.bson.Document;


public class Login extends AppCompatActivity {

    TextView error;
    EditText username, password;
    MongoClientURI uri;
    MongoClient mongoClient;
    MongoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);
        error = (TextView) findViewById(R.id.textView3);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        uri = new MongoClientURI( "mongodb://projectePES:PES12L@ds121565.mlab.com:21565/decorbarris" );
        mongoClient = new MongoClient(uri);
        db = mongoClient.getDatabase(uri.getDatabase());
    }

    public void iniciar_sessio(View v) {

        String user = username.getText().toString();
        String pass = password.getText().toString();



        try {
            MongoCollection<Document> coll = db.getCollection("users");

            BasicDBObject whereq = new BasicDBObject();
            whereq.put("username", user);
            MongoCursor<Document> c = coll.find(whereq).iterator();
            if( c.hasNext() && Objects.equals(c.next().get("password").toString(), pass)){
                TextView exito = (TextView) findViewById(R.id.textView5);
                exito.setVisibility(View.VISIBLE);
            }
            else{
                error.setVisibility(View.VISIBLE);
                username.getText().clear();
                password.getText().clear();
            }
        }catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public void registre (View v) {
        startActivity(new Intent(this, Register.class));
    }
}
