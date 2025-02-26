package com.example.foodallergenfinal.view.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodallergenfinal.databinding.FragmentLoginBinding;
import com.example.foodallergenfinal.view.HomeActivity;

public class LoginFragment extends Fragment {
    // ViewBinding reference
    private FragmentLoginBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Set up click listener



        binding.signInBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        });


        // Return the root view of the fragment
        return binding.getRoot();
    }

}