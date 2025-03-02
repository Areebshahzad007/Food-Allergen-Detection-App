package com.example.foodallergenfinal.view.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.utils.PrefsManager;
import com.example.foodallergenfinal.view.MainActivity;
import com.example.foodallergenfinal.auth.AuthRepository;
import com.example.foodallergenfinal.databinding.FragmentProfileBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AuthRepository authRepository;

    private int cameraRequestCode = 201;
    private String email;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        authRepository = new AuthRepository();

        /*boolean isDarkMode = PrefsManager.getBoolean(requireContext(), "is_dark_mode");
        binding.modeSwitchBtn.setChecked(isDarkMode);*/

        email = PrefsManager.getString(requireContext(), "email");
        String firstName = PrefsManager.getString(requireContext(), "first_name"+email);
        String lastName = PrefsManager.getString(requireContext(), "last_name"+email);
        String fullName = firstName + " " + lastName;
        binding.profileName.setText(fullName);
        binding.emailTV.setText(email);

        loadSavedImage(); // Load image when fragment is opened

        setupListeners();

        return binding.getRoot();
    }

    private void setupListeners() {

        binding.ltPassword.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_changePasswordFragment);
        });

        binding.ltLanguage.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_languageFragment);
        });

        binding.ltAllergies.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_addAllergiesFragment);
        });

        binding.ltAboutApp.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_aboutAppFragment);
        });

        binding.ltPrivacyPolicy.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_profileFragment_to_privacyPolicyFragment);
        });

        binding.logOutBtn.setOnClickListener(v -> {
            authRepository.logout();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            assert getActivity() != null;
            getActivity().finish();
        });

        binding.editIconIV.setOnClickListener(v -> {
            requestCameraPermission();
        });

    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startCamera();
                } else {
                    Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_LONG).show();
                }
            });

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, cameraRequestCode);
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        File directory = requireContext().getFilesDir();
        File imageFile = new File(directory, "profile_image.jpg");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();

            // Save image path in SharedPreferences using PrefsManager
            PrefsManager.saveProfileImagePath(requireContext(), email, imageFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load and Display Saved Image
    private void loadSavedImage() {
        String imagePath = PrefsManager.getProfileImagePath(requireContext(),email);

        if (imagePath != null) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                binding.profileIV.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == cameraRequestCode) {
                Bitmap pic = (Bitmap) data.getExtras().get("data");
                binding.profileIV.setImageBitmap(pic);
                saveImageToInternalStorage(pic); // Save image locally
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve saved dark mode preference
        boolean isDarkMode = PrefsManager.getBoolean(requireContext(), "is_dark_mode");
        binding.modeSwitchBtn.setChecked(isDarkMode);

        binding.modeSwitchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                PrefsManager.setBoolean(requireContext(), "is_dark_mode", true);
            } else {
                // Disable Dark Mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                PrefsManager.setBoolean(requireContext(), "is_dark_mode", false);
            }
        });
    }


}