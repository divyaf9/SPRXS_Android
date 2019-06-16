package com.divya.sprxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.divya.sprxs.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton,registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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