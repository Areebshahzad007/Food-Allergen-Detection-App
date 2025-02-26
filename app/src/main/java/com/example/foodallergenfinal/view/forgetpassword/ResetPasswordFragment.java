package com.example.foodallergenfinal.view.forgetpassword;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.auth.AuthRepository;
import com.example.foodallergenfinal.databinding.FragmentResetPasswordBinding;

public class ResetPasswordFragment extends Fragment {
    private FragmentResetPasswordBinding binding;
    private AuthRepository authRepository;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);

        authRepository = new AuthRepository();

        setupListeners();

        return binding.getRoot();
    }

    private void setupListeners() {
        binding.backBtn.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.sendRestPasswordEmailBtn.setOnClickListener(v -> {
            resetPassword();
        });
    }

    private void resetPassword() {
        String email = binding.emailET.getText().toString().trim();
        if (email.isEmpty()) {
            binding.emailET.setError("Email is required");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        authRepository.forgotPassword(email).thenAccept(success -> {
            binding.progressBar.setVisibility(View.GONE);
            if (success) {
                Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_resetPasswordFragment_to_checkMailFragment);
            } else {
                Toast.makeText(getContext(), "Failed to send reset email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}