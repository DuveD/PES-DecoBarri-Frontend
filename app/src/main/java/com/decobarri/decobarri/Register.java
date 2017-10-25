package com.decobarri.decobarri;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.bson.Document;

public class Register extends AppCompatActivity {

    TextView error;
    EditText username, password;
    MongoClientURI uri;
    MongoClient mongoClient;
    MongoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        try {
            MongoCollection<Document> coll = db.getCollection("users");

            BasicDBObject whereq = new BasicDBObject();
            whereq.put("username", user);
            MongoCursor<Document> c = coll.find(whereq).iterator();
            if( !c.hasNext() ){
                Document doc = new Document();
                doc.put("username", user);
                doc.put("password", pass);
                coll.insertOne(doc);
                mongoClient.close();
            }
            else{
                error.setVisibility(View.VISIBLE);
            }
        }catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
