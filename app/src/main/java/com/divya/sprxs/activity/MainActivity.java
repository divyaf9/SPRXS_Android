package com.divya.sprxs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.divya.sprxs.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton,registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
        Boolean loginCheck = sharedPreferences.getBoolean("FirstLogin", false);
        if (loginCheck) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

        loginButton= findViewById(R.id.loginButton);
        registerButton= findViewById(R.id.registerButton);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.loginButton:
                goToLoginActivity();
                break;

            case R.id.registerButton:
                goToRegisterActivity();
                break;
        }
    }

    public void goToLoginActivity() {

        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        super.onBackPressed();

    }

    public void goToRegisterActivity(){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }


}