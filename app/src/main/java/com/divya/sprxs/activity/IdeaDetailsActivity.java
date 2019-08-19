package com.divya.sprxs.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.divya.sprxs.R;
import com.divya.sprxs.fragment.AttachmentsFragment;
import com.divya.sprxs.fragment.CollaboratorsFragment;
import com.divya.sprxs.fragment.IdeaDetailsFragment;
import com.divya.sprxs.fragment.MilestonesFragment;
import com.divya.sprxs.utils.BottomNavigationBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class IdeaDetailsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavBarIdeaDetails;
    private IdeaDetailsFragment ideaDetailsFragment;
    private CollaboratorsFragment collaboratorsFragment;
    private MilestonesFragment milestonesFragment;
    private AttachmentsFragment attachmentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_details);

        bottomNavBarIdeaDetails = findViewById(R.id.bottomNavBarIdeaDetails);
        ideaDetailsFragment = new IdeaDetailsFragment();
        collaboratorsFragment = new CollaboratorsFragment();
        milestonesFragment = new MilestonesFragment();
        attachmentsFragment = new AttachmentsFragment();

        setFragment(ideaDetailsFragment);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavBarIdeaDetails.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavBarIdeaDetails.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.Idea_icon:
                        setFragment(ideaDetailsFragment);
                        return true;
                    case R.id.Collaborator_icon:
                        setFragment(collaboratorsFragment);
                        return true;
                    case R.id.Milestone_icon:
                        setFragment(milestonesFragment);
                        return true;
                    case R.id.Attachments_icon:
                        setFragment(attachmentsFragment);
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

