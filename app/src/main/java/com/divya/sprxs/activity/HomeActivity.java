package com.divya.sprxs.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.divya.sprxs.R;
import com.divya.sprxs.fragment.ChatFragment;
import com.divya.sprxs.fragment.CreateIdeasFragment;
import com.divya.sprxs.fragment.HomeFragment;
import com.divya.sprxs.fragment.InboxFragment;
import com.divya.sprxs.fragment.MyIdeasFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private MyIdeasFragment myIdeasFragment;
    private CreateIdeasFragment createIdeasFragment;
    private ChatFragment chatFragment;
    private InboxFragment inboxFragment;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

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
        inboxFragment = new InboxFragment();
        setFragment(homeFragment);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
                        setFragment(inboxFragment);
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setFragment(homeFragment);
        } else if (id == R.id.nav_my_ideas) {
            setFragment(myIdeasFragment);
        } else if (id == R.id.nav_create_ideas) {
            setFragment(createIdeasFragment);
        } else if (id == R.id.nav_inbox) {
            setFragment(inboxFragment);
        } else if (id == R.id.nav_chat) {
            setFragment(chatFragment);
        } else if (id == R.id.nav_logout) {
            openLogin();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void openLogin() {
        SharedPreferences preferences = this.getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}