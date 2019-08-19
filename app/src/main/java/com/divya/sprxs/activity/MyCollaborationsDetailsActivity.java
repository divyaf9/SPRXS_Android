package com.divya.sprxs.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.fragment.CollaboratorsFragment;
import com.divya.sprxs.fragment.MilestonesRequestFragment;
import com.divya.sprxs.fragment.MyCollaborationsDetailsFragment;
import com.divya.sprxs.utils.BottomNavigationBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyCollaborationsDetailsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationViewMyCollaborations;
    private MyCollaborationsDetailsFragment myCollaborationsDetailsFragment;
    private MilestonesRequestFragment milestonesRequestFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collaborations_details);

        bottomNavigationViewMyCollaborations = findViewById(R.id.bottomNavBarMyCollaborations);
        myCollaborationsDetailsFragment = new MyCollaborationsDetailsFragment();
        milestonesRequestFragment = new MilestonesRequestFragment();

        setFragment(myCollaborationsDetailsFragment);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationViewMyCollaborations.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationViewMyCollaborations.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.My_Collaborations_Details_icon:
                        setFragment(myCollaborationsDetailsFragment);
                        return true;
                    case R.id.Milestone_Requests_icon:
                        setFragment(milestonesRequestFragment);
                        return true;
                }
                return false;
            }
        });

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.myCollaborationsDetailsFrameLayout, fragment);
        fragmentTransaction.commit();
    }


}
