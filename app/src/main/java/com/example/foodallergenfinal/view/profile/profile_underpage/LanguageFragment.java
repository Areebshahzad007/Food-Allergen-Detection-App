package com.example.foodallergenfinal.view.profile.profile_underpage;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.databinding.FragmentLanguageBinding;
import com.example.foodallergenfinal.utils.PrefsManager;

import java.util.Locale;

public class LanguageFragment extends Fragment {
    private FragmentLanguageBinding binding;

    public LanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLanguageBinding.inflate(inflater, container, false);

        binding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.ltEngUK.setOnClickListener(v -> changeLanguage("en", "GB"));
        binding.ltEngUSA.setOnClickListener(v -> changeLanguage("en", "US"));
        binding.ltGer.setOnClickListener(v -> changeLanguage("de", "DE"));
        binding.ltFra.setOnClickListener(v -> changeLanguage("fr", "FR"));
        binding.ltSpa.setOnClickListener(v -> changeLanguage("es", "ES"));

        return binding.getRoot();
    }

    private void changeLanguage(String languageCode, String countryCode) {
        Locale locale = new Locale(languageCode, countryCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.setLocale(locale);
        resources.updateConfiguration(config, dm);

        // Save the selected language
        PrefsManager.saveSelectedLanguage(requireContext(), languageCode, countryCode);

        // Restart activity to apply changes
        requireActivity().recreate();
    }
}