package com.decobarri.decobarri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);
    }

    //Comprova que l'usuari existeix
    //Si existeix obre l'activity principal
    //Si no mostra error
    public void iniciar_sessio(View v) {

        v.setVisibility(View.VISIBLE);
    }
}
