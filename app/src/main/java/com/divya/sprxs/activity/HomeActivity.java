package com.divya.sprxs.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.divya.sprxs.R;
import com.divya.sprxs.fragment.ChatFragment;
import com.divya.sprxs.fragment.CreateIdeasFragment;
import com.divya.sprxs.fragment.HomeFragment;
import com.divya.sprxs.fragment.MenuFragment;
import com.divya.sprxs.fragment.MyIdeasFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private MyIdeasFragment myIdeasFragment;
    private CreateIdeasFragment createIdeasFragment;
    private ChatFragment chatFragment;
    private MenuFragment menuFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Home");
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        frameLayout = findViewById(R.id.frameLayout);
        homeFragment = new HomeFragment();
        myIdeasFragment = new MyIdeasFragment();
        createIdeasFragment = new CreateIdeasFragment();
        chatFragment = new ChatFragment();
        menuFragment = new MenuFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {

                    case R.id.Home_icon:
                        setFragment(homeFragment);
                        return true;

                    case R.id.My_Ideas_icon:
                        setFragment(myIdeasFragment);
                        return true;

                    case R.id.Create_Ideas_icon:
                        setFragment(createIdeasFragment);
                        return true;

                    case R.id.Chat_icon:
                        setFragment(chatFragment);
                        return true;

                    case R.id.Inbox_icon:
                        setFragment(menuFragment);
                        return true;

                }
                return false;
            }
        });

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

    }
}