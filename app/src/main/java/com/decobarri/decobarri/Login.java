package com.decobarri.decobarri;

import android.app.Activity;
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

    //Comprova que l'usuari existeix
    //Si existeix obre l'activity principal
    //Si no mostra error
    public void iniciar_sessio(View v) {

        error.setVisibility(View.VISIBLE);
        username.getText().clear();
        password.getText().clear();
    }

    public void registre (View v) {
        startActivity(new Intent(this, Regiter.class));
    }
}
