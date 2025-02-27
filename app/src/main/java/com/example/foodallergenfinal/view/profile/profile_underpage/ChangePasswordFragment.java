package com.example.foodallergenfinal.view.profile.profile_underpage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.auth.AuthRepository;
import com.example.foodallergenfinal.databinding.FragmentChangePasswordBinding;

public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    private AuthRepository authRepository;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        authRepository = new AuthRepository();

        setupListeners();

        return binding.getRoot();
    }

    private void setupListeners() {
        binding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.updatePasswordBtn.setOnClickListener(v -> {
            String currentPassword = binding.currentPasswordET.getText().toString();
            String newPassword = binding.newPasswordET.getText().toString();
            String repeatPassword = binding.repeatPasswordET.getText().toString();

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.updatePasswordBtn.setEnabled(false);

            if (currentPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(repeatPassword)) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                authRepository.updatePassword(currentPassword, newPassword).thenAccept(success -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.updatePasswordBtn.setEnabled(true);
                    if (success) {
                        Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                        //requireActivity().onBackPressed();
                    } else {
                        Toast.makeText(requireContext(), "Password update failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}