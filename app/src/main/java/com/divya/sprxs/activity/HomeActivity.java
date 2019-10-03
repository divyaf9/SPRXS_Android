package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.divya.sprxs.model.CollaboratorsRequest;
import com.divya.sprxs.utils.BottomNavigationBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private HomeFragment homeFragment;
    private MyIdeasFragment myIdeasFragment;
    private CreateIdeasFragment createIdeasFragment;
    private ChatFragment chatFragment;
    private InboxFragment inboxFragment;
    private ImageView helpImageView;
    private TextView drawerNameTextView, drawerMailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        this.setTitle("Home");

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
        helpImageView = findViewById(R.id.helpImageView);
        helpImageView.setOnClickListener(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_home);
        View headerView = navigationView.getHeaderView(0);
        drawerNameTextView = headerView.findViewById(R.id.drawerNameTextView);
        drawerMailTextView = headerView.findViewById(R.id.drawerMailTextView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String firstname = prefs.getString("firstname", null);
        String surname = prefs.getString("surname", null);
        drawerNameTextView.setText(firstname + " " + surname);
        drawerMailTextView.setText(email);
        navigationView.setNavigationItemSelectedListener(this);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

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
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            openMyProfile();
        } else if (id == R.id.nav_home) {
            setFragment(homeFragment);
        } else if (id == R.id.nav_my_ideas) {
            setFragment(myIdeasFragment);
        } else if (id == R.id.nav_create_ideas) {
            setFragment(createIdeasFragment);
        } else if (id == R.id.nav_inbox) {
            setFragment(inboxFragment);
        } else if (id == R.id.nav_chat) {
            setFragment(chatFragment);
        } else if (id == R.id.nav_marketplace) {
            openMarketPlace();
        } else if (id == R.id.nav_search) {
            openSearch();
        } else if (id == R.id.nav_collaborator_requests) {
            openCollaboratorRequests();
        } else if (id == R.id.nav_my_collaborations) {
            openMyCollaborations();
        } else if (id == R.id.nav_contact_us) {
            openContact();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openMyProfile() {
        Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
        startActivity(intent);
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void openMarketPlace() {
        Intent intent = new Intent(HomeActivity.this, MarketPlaceActivity.class);
        startActivity(intent);
        finish();
    }

    private void openSearch() {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);
        finish();
    }

    private void openCollaboratorRequests() {
        Intent intent = new Intent(HomeActivity.this, CollaboratorRequestsActivity.class);
        startActivity(intent);
        finish();
    }

    private void openMyCollaborations() {
        Intent intent = new Intent(HomeActivity.this,MyCollaborationsActivity.class);
        startActivity(intent);
    }

    private void openContact() {
        Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.helpImageView:
//                openHelp();
//        }
    }

    private void openHelp() {
        Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}