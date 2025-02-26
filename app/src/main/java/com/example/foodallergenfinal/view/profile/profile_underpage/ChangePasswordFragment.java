package com.example.foodallergenfinal.view.profile.profile_underpage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


        return binding.getRoot();
    }
}