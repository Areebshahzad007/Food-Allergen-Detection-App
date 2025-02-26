package com.example.foodallergenfinal.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodallergenfinal.HistoryFragment;
import com.example.foodallergenfinal.ProfileFragment;
import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.view.scan.ScanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment HistoryFragment = new HistoryFragment();
        Fragment ScanFragment = new ScanFragment();
        Fragment ProfileFragment = new ProfileFragment();

        //setCurrentFragment(HistoryFragment);

        // Set up the bottom navigation item click listener
        /*bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_history:
                    setCurrentFragment(HistoryFragment);
                    break;
                case R.id.nav_scan:
                    setCurrentFragment(ScanFragment);
                    break;
                case R.id.nav_profile:
                    setCurrentFragment(ProfileFragment);
                    break;
            }
            return true;
        });*/
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}