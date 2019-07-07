package com.divya.sprxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;

public class SearchIdeaDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView ideaNameSearchDetailsText, ideaIdSearchDetailsText, ideaDescriptionSearchDetailsText, categorySearchDetailsText;
    private Button requestToCollaborateButton;
    private String ideaId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_idea_details);

        ideaNameSearchDetailsText = findViewById(R.id.ideaNameSearchDetailsText);
        ideaIdSearchDetailsText = findViewById(R.id.ideaIdSearchDetailsText);
        ideaDescriptionSearchDetailsText = findViewById(R.id.ideaDescriptionSearchDetailsText);
        categorySearchDetailsText = findViewById(R.id.categorySearchDetailsText);
        requestToCollaborateButton = findViewById(R.id.requestToCollaborateButton);
        requestToCollaborateButton.setOnClickListener(this);

        ideaId = getIntent().getStringExtra("mySearchList");
        final String ideaName = getIntent().getStringExtra("mySearchListIdea");
        final String ideaDesc = getIntent().getStringExtra("mySearchListIdeaDesc");
        final String category = getIntent().getStringExtra("mySearchListCategory");

        ideaIdSearchDetailsText.setText("#"+ideaId);
        ideaNameSearchDetailsText.setText(ideaName);
        ideaDescriptionSearchDetailsText.setText(ideaDesc);
        categorySearchDetailsText.setText(category);

//        ideaId = ideaIdSearchDetailsText.getText().toString();

        ideaDescriptionSearchDetailsText.setMovementMethod(new ScrollingMovementMethod());

        this.setTitle("Idea Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.requestToCollaborateButton:
                openRequestToCollaborate();
                break;
        }

    }

    private void openRequestToCollaborate() {
        Intent intent = new Intent(SearchIdeaDetails.this,RequestToCollaborate.class);
        intent.putExtra("myRequestList",ideaId);
        startActivity(intent);
    }
}
