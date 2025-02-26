package com.example.foodallergenfinal.view.scan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodallergenfinal.MainActivity;
import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.auth.AuthRepository;
import com.example.foodallergenfinal.databinding.FragmentScanBinding;
import com.example.foodallergenfinal.view.HomeActivity;

public class ScanFragment extends Fragment {
    private FragmentScanBinding binding;
    private AuthRepository authRepository;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater, container, false);
        authRepository = new AuthRepository();

        binding.barCodeScannerIV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_scanFragment_to_barcodeScannerFragment);
        });

        binding.ocrIV.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_scanFragment_to_OCRScannerFragment);
        });


        // just for testing purpose
        binding.btnLogout.setOnClickListener(v -> {
            authRepository.logout();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return binding.getRoot();
    }
}