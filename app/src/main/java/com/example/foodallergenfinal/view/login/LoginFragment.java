package com.example.foodallergenfinal.view.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.auth.AuthRepository;
import com.example.foodallergenfinal.databinding.FragmentLoginBinding;
import com.example.foodallergenfinal.utils.PrefsManager;
import com.example.foodallergenfinal.view.HomeActivity;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AuthRepository authRepository;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Initialize the AuthRepository
        authRepository = new AuthRepository();

        // Check if the user is already logged in
        if (authRepository.isLoggedIn()) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            assert getActivity() != null;
            getActivity().finish();
        }

        setupListeners();

        return binding.getRoot();
    }

    private void setupListeners() {
        binding.signInBtn.setOnClickListener(v -> {
            loginUser();
        });

        binding.ltBtnSignup.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_signUpFragment);
        });

        binding.forgotPasswordTV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_resetPasswordFragment);
        });
    }

    private void loginUser() {
        String email = binding.emailET.getText().toString().trim();
        String password = binding.passwordET.getText().toString().trim();

        if (email.isEmpty()) {
            binding.emailET.setError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            binding.passwordET.setError("Password is required");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.signInBtn.setEnabled(false);

        authRepository.signIn(email, password).thenAccept(success -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.signInBtn.setEnabled(true);

            if (success) {
                PrefsManager.setString(requireContext(), "email", email);
                Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                assert getActivity() != null;
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Login Failed. Please verify your email before logging in.", Toast.LENGTH_LONG).show();
            }
        });
    }

}