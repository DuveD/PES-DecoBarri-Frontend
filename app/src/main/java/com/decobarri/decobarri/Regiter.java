package com.decobarri.decobarri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Regiter extends AppCompatActivity {

    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter);
        error = (TextView) findViewById(R.id.textView4);
    }

    public void registro (View v) {
        error.setVisibility(View.VISIBLE);
    }
}
