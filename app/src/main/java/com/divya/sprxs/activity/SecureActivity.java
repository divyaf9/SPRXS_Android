package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.divya.sprxs.R;

public class SecureActivity extends AppCompatActivity implements View.OnClickListener {
    private Button skipSecureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);

        skipSecureButton = findViewById(R.id.skipSecureButton);
        skipSecureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.skipSecureButton:
             moveToLogin();
             break;
     }
    }

    private void moveToLogin() {
        Intent intent = new Intent(SecureActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
