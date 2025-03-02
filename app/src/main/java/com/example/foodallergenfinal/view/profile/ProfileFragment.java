package com.example.foodallergenfinal.view.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.provider.Settings;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AuthRepository authRepository;

    private static final int multiplePermissionCode = 101;
    private static final int cameraRequestCode = 201;
    private static final int galleryRequestCode = 202;

    private static final String[] multiplePermissionList = Build.VERSION.SDK_INT >= 33 ?
            new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            } :
            new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };


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
            checkMultiplePermission();
        });

    }

    private void checkMultiplePermission() {
        ArrayList<String> needListOfPermission = new ArrayList<>();
        for (String permission : multiplePermissionList) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                needListOfPermission.add(permission);
            }
        }
        if (!needListOfPermission.isEmpty()) {
            requestPermissions(needListOfPermission.toArray(new String[0]), multiplePermissionCode);
        } else {
            showProfilePicOptionsDialog();
        }
    }

    private void showProfilePicOptionsDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Set Profile Picture")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else if (which == 1) {
                        openGallery();
                    }
                })
                .setCancelable(true)
                .show();
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_LONG).show();
                }
            });

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, cameraRequestCode);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, galleryRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == multiplePermissionCode) {
            if (grantResults.length > 0) {
                boolean isGrant = true;
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isGrant = false;
                        break;
                    }
                }
                if (isGrant) {
                    showProfilePicOptionsDialog();
                } else {
                    boolean someDenied = false;
                    for (String permission : permissions) {
                        assert this.getActivity() != null;
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), permission)) {
                            if (ActivityCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
                                someDenied = true;
                            }
                        }
                    }

                    if (someDenied) {
                        appSettingOpen();
                    } else {
                        warningPermissionDialog((dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                checkMultiplePermission();
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == cameraRequestCode) {
                Bitmap thumbNail = (Bitmap) data.getExtras().get("data");
                binding.profileIV.setImageBitmap(thumbNail);
                assert thumbNail != null;
                saveImageToInternalStorage(thumbNail);
            } else if (requestCode == galleryRequestCode) {
                Uri selectedImage = data.getData();
                binding.profileIV.setImageURI(selectedImage);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImage);
                    saveImageToInternalStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void appSettingOpen() {
        new AlertDialog.Builder(requireContext())
                .setMessage("It looks like you turned off the permission required for this application. It can be enabled under Application Settings")
                .setPositiveButton("GO TO SETTINGS", (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void warningPermissionDialog(DialogInterface.OnClickListener listener) {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage("All permissions are required for this app")
                .setCancelable(false)
                .setPositiveButton("Ok", listener)
                .create()
                .show();
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