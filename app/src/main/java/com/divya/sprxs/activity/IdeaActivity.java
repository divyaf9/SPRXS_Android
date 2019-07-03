package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.divya.sprxs.R;

public class IdeaActivity extends AppCompatActivity implements View.OnClickListener {
    private Button skipIdeaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);

        skipIdeaButton = findViewById(R.id.skipIdeaButton);
        skipIdeaButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.skipIdeaButton:
             moveToSecure();
             break;
     }
    }

    private void moveToSecure() {
        Intent intent = new Intent(IdeaActivity.this,SecureActivity.class);
        startActivity(intent);
    }
}
