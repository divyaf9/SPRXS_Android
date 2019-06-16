package com.divya.sprxs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;


public class MenuFragment extends Fragment implements View.OnClickListener {

    Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater layoutInflater = LayoutInflater.from( getActivity() );
        View header = layoutInflater.inflate( R.layout.toolbar, null );
        TextView textView = header.findViewById(R.id.titleTextView);
        textView.setText("Inbox");
        ImageView imageView = header.findViewById(R.id.menu);
        actionBar.setCustomView(header);

        logoutButton = v.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
//
//        DrawerLayout drawer = v.findViewById(R.id.drawer_layout);
//        NavigationView navigationView = v.findViewById(R.id.nav_view);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
              openLogin();
                break;
        }
    }


    private void openLogin() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        return false;
//    }
}


