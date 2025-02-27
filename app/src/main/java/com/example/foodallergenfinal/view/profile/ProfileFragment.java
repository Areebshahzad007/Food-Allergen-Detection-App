package com.example.foodallergenfinal.view.profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.utils.PrefsManager;
import com.example.foodallergenfinal.view.MainActivity;
import com.example.foodallergenfinal.auth.AuthRepository;
import com.example.foodallergenfinal.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AuthRepository authRepository;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        authRepository = new AuthRepository();

        String firstName = PrefsManager.getString(requireContext(), "first_name");
        String lastName = PrefsManager.getString(requireContext(), "last_name");
        String fullName = firstName + " " + lastName;
        binding.profileName.setText(fullName);
        binding.emailTV.setText(PrefsManager.getString(requireContext(), "email"));

        setupListeners();

        return binding.getRoot();
    }

    private void setupListeners() {
        binding.changePasswordTV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_changePasswordFragment);
        });

        binding.languageTV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_languageFragment);
        });

        binding.addAllergiesTV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_addAllergiesFragment);
        });

        binding.aboutAppTV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_aboutAppFragment);
        });

        binding.privacyPolicyTV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_privacyPolicyFragment);
        });

        binding.logOutBtn.setOnClickListener(v -> {
            authRepository.logout();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            assert getActivity() != null;
            getActivity().finish();
        });
    }
}