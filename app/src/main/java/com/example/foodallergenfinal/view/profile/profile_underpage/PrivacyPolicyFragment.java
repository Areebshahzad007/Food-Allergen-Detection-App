package com.example.foodallergenfinal.view.profile.profile_underpage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.databinding.FragmentPrivacyPolicyBinding;

public class PrivacyPolicyFragment extends Fragment {
    private FragmentPrivacyPolicyBinding binding;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false);


        binding.backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        return binding.getRoot();
    }
}