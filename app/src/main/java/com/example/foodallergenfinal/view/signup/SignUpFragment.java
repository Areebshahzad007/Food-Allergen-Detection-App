package com.example.foodallergenfinal.view.signup;

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
import com.example.foodallergenfinal.databinding.FragmentSignUpBinding;
import com.example.foodallergenfinal.utils.PrefsManager;
import com.example.foodallergenfinal.view.HomeActivity;

public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;
    private AuthRepository authRepository;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        authRepository = new AuthRepository();

        setupListeners();

        return binding.getRoot();
    }

    private void setupListeners() {
        binding.signInBtn.setOnClickListener(v -> signUpUser());
        binding.ltBtnLogin.setOnClickListener(v->{
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_signUpFragment_to_loginFragment);
        });
    }

    private void signUpUser() {
        String email = binding.emailET.getText().toString().trim();
        String password = binding.passwordET.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordET.getText().toString().trim();
        String firstName = binding.firstNameET.getText().toString().trim();
        String lastName = binding.lastNameET.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.signInBtn.setEnabled(false);

        authRepository.signUp(email, password).thenAccept(success -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.signInBtn.setEnabled(true);

            if (success) {
                PrefsManager.setString(requireContext(), "email", email);
                PrefsManager.setString(requireContext(), "first_name", firstName);
                PrefsManager.setString(requireContext(), "last_name", lastName);

                Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                assert getActivity() != null;
                getActivity().finish();
            } else {
                Toast.makeText(requireContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}