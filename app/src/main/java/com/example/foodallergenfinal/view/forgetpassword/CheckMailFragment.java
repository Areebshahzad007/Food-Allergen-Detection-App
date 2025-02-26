package com.example.foodallergenfinal.view.forgetpassword;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.foodallergenfinal.databinding.FragmentCheckMailBinding;

public class CheckMailFragment extends Fragment {
    private FragmentCheckMailBinding binding;
    public CheckMailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckMailBinding.inflate(inflater, container, false);

        binding.tryAnotherEmailTV.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        return binding.getRoot();
    }
}