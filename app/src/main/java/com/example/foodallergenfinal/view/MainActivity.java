package com.example.foodallergenfinal.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.utils.PrefsManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        applySavedLanguage(this);

        // Apply dark mode before UI loads
        boolean isDarkMode = PrefsManager.getBoolean(this, "is_dark_mode");
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void applySavedLanguage(Context context) {
        String savedLanguage = PrefsManager.getSelectedLanguage(context);
        if (savedLanguage != null && !savedLanguage.isEmpty()) {
            String[] languageParts = savedLanguage.split("-");
            Locale locale = new Locale(languageParts[0], languageParts[1]);
            Locale.setDefault(locale);

            Resources resources = context.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.setLocale(locale);
            resources.updateConfiguration(config, dm);
        }
    }
}