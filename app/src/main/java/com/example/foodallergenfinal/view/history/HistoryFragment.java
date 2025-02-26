package com.example.foodallergenfinal.view.history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        binding.scanYourProductButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_historyFragment_to_barcodeScannerFragment);
        });

        return binding.getRoot();
    }
}