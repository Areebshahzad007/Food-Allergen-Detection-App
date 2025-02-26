package com.example.foodallergenfinal.view.scan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.databinding.FragmentScanBinding;

public class ScanFragment extends Fragment {
    private FragmentScanBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater, container, false);

        binding.barCodeScannerIV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_scanFragment_to_barcodeScannerFragment);
        });

        binding.ocrIV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_scanFragment_to_OCRScannerFragment);
        });

        return binding.getRoot();
    }
}