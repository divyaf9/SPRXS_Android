package com.divya.sprxs.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.divya.sprxs.R;
import com.divya.sprxs.fragment.CollaboratorFragment;
import com.divya.sprxs.fragment.IdeaDetailsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class IdeaDetailsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavBarIdeaDetails;
    private IdeaDetailsFragment ideaDetailsFragment;
    private CollaboratorFragment collaboratorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_details);

        bottomNavBarIdeaDetails = findViewById(R.id.bottomNavBarIdeaDetails);
        ideaDetailsFragment = new IdeaDetailsFragment();
        collaboratorFragment = new CollaboratorFragment();

        setFragment(ideaDetailsFragment);

        bottomNavBarIdeaDetails.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.Idea_icon:
                        setFragment(ideaDetailsFragment);
                        return true;
                    case R.id.Collaborator_icon:
                        setFragment(collaboratorFragment);
                        return true;
                }
                return false;
            }
        });

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ideaDetailsFrameLayout, fragment);
        fragmentTransaction.commit();
    }

}

