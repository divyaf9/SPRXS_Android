package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.divya.sprxs.R;

public class NoIdeasActivity extends AppCompatActivity {


    private LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_ideas);

        setTitle("My Ideas");


        lottieAnimationView = findViewById(R.id.noIdeaLottieAnimationView);


    }
}
