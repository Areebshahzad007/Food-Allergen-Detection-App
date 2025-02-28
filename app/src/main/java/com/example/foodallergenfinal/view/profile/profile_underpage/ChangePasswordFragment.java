package com.example.foodallergenfinal.view.profile.profile_underpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.auth.AuthRepository;
import com.example.foodallergenfinal.databinding.FragmentChangePasswordBinding;
import com.example.foodallergenfinal.view.MainActivity;

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
            String currentPassword = binding.currentPasswordET.getText().toString().trim();
            String newPassword = binding.newPasswordET.getText().toString().trim();
            String repeatPassword = binding.repeatPasswordET.getText().toString().trim();

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.updatePasswordBtn.setEnabled(false);

            if (currentPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                resetUI();
                return;
            }

            if (!newPassword.equals(repeatPassword)) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                resetUI();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(requireContext(), "New password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                resetUI();
                return;
            }

            // Proceed with password update
            authRepository.updatePassword(currentPassword, newPassword).thenAccept(success -> {
                requireActivity().runOnUiThread(() -> {
                    resetUI();

                    Log.d("TAG", "Password update success: " + success); // Debugging Log

                    if (success) {
                        Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();

                        authRepository.logout();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        }, 500);
                    } else {
                        Toast.makeText(requireContext(), "Password update failed. Check your current password and try again.", Toast.LENGTH_LONG).show();
                    }
                });
            });


        });
    }

    private void resetUI() {
        binding.progressBar.setVisibility(View.GONE);
        binding.updatePasswordBtn.setEnabled(true);
    }
}