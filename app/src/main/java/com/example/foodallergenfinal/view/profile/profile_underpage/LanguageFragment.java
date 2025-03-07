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

        binding.ltEngUK.setOnClickListener(v -> selectLanguage("en", "GB", binding.ltEngUK));
        binding.ltEngUSA.setOnClickListener(v -> selectLanguage("en", "US", binding.ltEngUSA));
        binding.ltGer.setOnClickListener(v -> selectLanguage("de", "DE", binding.ltGer));
        binding.ltFra.setOnClickListener(v -> selectLanguage("fr", "FR", binding.ltFra));
        binding.ltSpa.setOnClickListener(v -> selectLanguage("es", "ES", binding.ltSpa));

        // Restore selected language background
        restoreSelectedLanguageBackground();

        return binding.getRoot();
    }

    private void selectLanguage(String languageCode, String countryCode, View selectedView) {
        Locale locale = new Locale(languageCode, countryCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.setLocale(locale);
        resources.updateConfiguration(config, dm);

        // Save the selected language
        PrefsManager.saveSelectedLanguage(requireContext(), languageCode, countryCode);

        // Update UI backgrounds
        updateLanguageBackgrounds(selectedView);

        // Restart activity to apply changes
        requireActivity().recreate();
    }

    private void updateLanguageBackgrounds(View selectedView) {
        // Reset all to unselected background
        binding.ltEngUK.setBackgroundResource(R.drawable.language_bg2);
        binding.ltEngUSA.setBackgroundResource(R.drawable.language_bg2);
        binding.ltGer.setBackgroundResource(R.drawable.language_bg2);
        binding.ltFra.setBackgroundResource(R.drawable.language_bg2);
        binding.ltSpa.setBackgroundResource(R.drawable.language_bg2);

        // Set selected to active background
        selectedView.setBackgroundResource(R.drawable.language_bg);
    }

    private void restoreSelectedLanguageBackground() {
        String selectedLanguage = PrefsManager.getSelectedLanguage(requireContext());
        if (selectedLanguage == null) return;

        String[] languageParts = selectedLanguage.split("-");
        if (languageParts.length != 2) return;

        String languageCode = languageParts[0];
        String countryCode = languageParts[1];

        if (languageCode.equals("en") && countryCode.equals("GB")) {
            updateLanguageBackgrounds(binding.ltEngUK);
        } else if (languageCode.equals("en") && countryCode.equals("US")) {
            updateLanguageBackgrounds(binding.ltEngUSA);
        } else if (languageCode.equals("de") && countryCode.equals("DE")) {
            updateLanguageBackgrounds(binding.ltGer);
        } else if (languageCode.equals("fr") && countryCode.equals("FR")) {
            updateLanguageBackgrounds(binding.ltFra);
        } else if (languageCode.equals("es") && countryCode.equals("ES")) {
            updateLanguageBackgrounds(binding.ltSpa);
        }
    }
}
