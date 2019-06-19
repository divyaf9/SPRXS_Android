package com.divya.sprxs.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
;

import com.divya.sprxs.R;

public class HelpActivity extends AppCompatActivity {

//    private Toolbar toolbar_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        this.setTitle("Help and Support");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;

    }
}
