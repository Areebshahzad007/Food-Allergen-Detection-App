package com.example.foodallergenfinal.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.foodallergenfinal.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set up the bottom navigation item click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);

            int itemId = item.getItemId();
            if (itemId == R.id.nav_history) {
                navController.navigate(R.id.historyFragment);
                return true;
            } else if (itemId == R.id.nav_scan) {
                navController.navigate(R.id.scanFragment);
                return true;
            } else if (itemId == R.id.nav_profile) {
                navController.navigate(R.id.profileFragment);
                return true;
            }

            return false;
        });
    }

}