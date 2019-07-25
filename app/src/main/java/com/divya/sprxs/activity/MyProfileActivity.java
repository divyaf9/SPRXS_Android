package com.divya.sprxs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nameProfileTextView, mailProfileTextView, logoutTextView, privacyProfileTextView, competitionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        this.setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameProfileTextView = findViewById(R.id.nameProfileTextView);
        mailProfileTextView = findViewById(R.id.mailProfileTextView);
        logoutTextView = findViewById(R.id.logoutTextView);
        privacyProfileTextView = findViewById(R.id.privacyProfileTextView);
        competitionTextView = findViewById(R.id.competitionTextView);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String firstname = prefs.getString("firstname", null);
        String surname = prefs.getString("surname", null);

        nameProfileTextView.setText(firstname + " " + surname);
        mailProfileTextView.setText(email);

        logoutTextView.setOnClickListener(this);
        privacyProfileTextView.setOnClickListener(this);
        competitionTextView.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutTextView:
                openLogout();
            case R.id.privacyProfileTextView:
                openPrivacyPolicy();
            case R.id.competitionTextView:
                openCompetitionRules();
        }
    }


    private void openLogout() {
        SharedPreferences preferences = this.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        editor.apply();
        SharedPreferences sharedPreferences = this.getSharedPreferences("MySignup", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        edit.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openPrivacyPolicy() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sprxs.io/Privacy.html")));
    }

    private void openCompetitionRules() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sprxs.io/Competition.Rules.html")));

    }

}