package com.decobarri.decobarri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class Login extends AppCompatActivity {

    TextView error;
    EditText username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);
        error = (TextView) findViewById(R.id.textView3);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);


    }

    public void iniciar_sessio(View v) {

        String user = username.getText().toString();
        String pass = password.getText().toString();
        //project-pes.herokuapp.com/user/login
        //200:OK
    }

    public void registre (View v) {
        startActivity(new Intent(this, Register.class));
    }
}
