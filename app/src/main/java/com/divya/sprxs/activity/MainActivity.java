package com.divya.sprxs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.SlideAdapter;

public class MainActivity extends AppCompatActivity {


    private ViewPager mainViewPager;
    private LinearLayout landingPageLinearLayout;
    private SlideAdapter slideAdapter;
    private ImageView[] dots;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewPager = findViewById(R.id.mainViewPager);
        landingPageLinearLayout = findViewById(R.id.landingPageLinearLayout);

        slideAdapter = new SlideAdapter(this);
        mainViewPager.setAdapter(slideAdapter);
        next = findViewById(R.id.next);

        SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
        Boolean loginCheck = sharedPreferences.getBoolean("FirstLogin", false);
        final SharedPreferences preferences = getSharedPreferences("MySignup", MODE_PRIVATE);
        Boolean signupCheck = preferences.getBoolean("FirstSignup", false);

        if ((loginCheck.equals(true)) || ((signupCheck.equals(true)))) {
            Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        } else {
            slideAdapter = new SlideAdapter(this);
            mainViewPager.setAdapter(slideAdapter);
            addSlidesIndicator();
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);


            }
        });
    }


    public void addSlidesIndicator() {

        dots = new ImageView[5];

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            landingPageLinearLayout.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dots.length; i++) {

                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));



                if(position == 4) {

                  next.setVisibility(View.VISIBLE);

                }else if (position == 0){

                    next.setVisibility(View.INVISIBLE);
                }else{

                    next.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}



