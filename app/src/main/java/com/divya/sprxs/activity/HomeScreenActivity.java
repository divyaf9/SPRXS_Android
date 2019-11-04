package com.divya.sprxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.GridAdapter;

public class HomeScreenActivity extends AppCompatActivity {


    public static int maxHeight;

    private GridView gridView;

    private ImageView inboxHomeScreen,searchIdeaHomeScreen;

    private String[] values = {

            "Create Idea",
            "My Ideas",
            "Chat",
            "Market Place",
            "Collaborator\n   Requests",
            "My Collaborations",
            "My Profile",
            "Contact Us"
    };

    private int[] images = {

            R.drawable.createidea,
            R.drawable.myideas,
            R.drawable.chat,
            R.drawable.market,
            R.drawable.requesttocollaborate,
            R.drawable.mycollaborations,
            R.drawable.myprofile,
            R.drawable.contactus
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        gridView = findViewById(R.id.grid);
        inboxHomeScreen = findViewById(R.id.inboxHomeScreen);
        searchIdeaHomeScreen = findViewById(R.id.searchIdeaHomeScreen);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        maxHeight = height;


        gridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }

        });

        GridAdapter gridAdapter = new GridAdapter(this, values, images);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        Intent intent = new Intent(HomeScreenActivity.this, CreateIdeasActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intentMyIdeas = new Intent(HomeScreenActivity.this, MyIdeasActivity.class);
                        startActivity(intentMyIdeas);
                        break;

                    case 2:
                        Intent intentChatDisplay = new Intent(HomeScreenActivity.this, ChatDisplayActivity.class);
                        startActivity(intentChatDisplay);
                        break;

                    case 3:
                        Intent intentMarketPlace = new Intent(HomeScreenActivity.this, MarketPlaceActivity.class);
                        startActivity(intentMarketPlace);
                        break;

                    case 4:
                        Intent intentCollaboratorRequest = new Intent(HomeScreenActivity.this, CollaboratorRequestsActivity.class);
                        startActivity(intentCollaboratorRequest);
                        break;

                    case 5:
                        Intent intentMyCollaborations = new Intent(HomeScreenActivity.this, MyCollaborationsActivity.class);
                        startActivity(intentMyCollaborations);
                        break;

                    case 6:
                        Intent intentMyProfile = new Intent(HomeScreenActivity.this, MyProfileActivity.class);
                        startActivity(intentMyProfile);
                        break;

                    case 7:
                        Intent intentContactUs = new Intent(HomeScreenActivity.this, ContactUsActivity.class);
                        startActivity(intentContactUs);
                        break;
                }


            }
        });

        inboxHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeScreenActivity.this, InboxActivity.class);
                startActivity(intent);
            }
        });

        searchIdeaHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeScreenActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


    }
}
