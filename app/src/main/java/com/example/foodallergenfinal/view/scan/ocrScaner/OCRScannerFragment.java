package com.example.foodallergenfinal.view.scan.ocrScaner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodallergenfinal.databinding.FragmentOCRScannerBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class OCRScannerFragment extends Fragment {
    private FragmentOCRScannerBinding binding;
    private ExecutorService cameraExecutor;
    private List<String> userAllergic;
    private ImageCapture imageCapture;

    public OCRScannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOCRScannerBinding.inflate(inflater, container, false);

        // user demo allergic
        userAllergic = new ArrayList<>();
        userAllergic.add("milk");
        userAllergic.add("nut");
        userAllergic.add("peanut");

        // Request camera permission
        requestCameraPermission();
        cameraExecutor = Executors.newSingleThreadExecutor();

        // Set button listener to capture image
        binding.captureButton.setOnClickListener(v -> captureImage());

        // Cancel button click listener
        binding.cancelIV.setOnClickListener(v -> {
            binding.ltAllergensDetected.setVisibility(View.GONE);
        });
        binding.cancelIV2.setOnClickListener(v -> {
            binding.ltNoAllergens.setVisibility(View.GONE);
        });

        return binding.getRoot();
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
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
            } catch (Exception exc) {
                Log.e("CameraX", "Use case binding failed", exc);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void captureImage() {
        if (imageCapture == null) {
            Log.e("OCR", "ImageCapture not initialized.");
            return;
        }

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy imageProxy) {
                super.onCaptureSuccess(imageProxy);
                processTextRecognition(imageProxy);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e("OCR", "Image capture failed: " + exception.getMessage());
            }
        });
    }

    private void processTextRecognition(@NonNull ImageProxy imageProxy) {
        @SuppressWarnings("UnsafeOptInUsageError")
        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(text -> {
                    String recognizedText = text.getText();
                    Log.d("OCR", "Recognized Text: " + recognizedText);

                    if (!recognizedText.isEmpty()) {
                        if (isAdded() && getActivity() != null) {
                            requireActivity().runOnUiThread(() -> {
                                checkAllergens(recognizedText);
                            });
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("OCR", "Text Recognition Failed", e))
                .addOnCompleteListener(task -> imageProxy.close());
    }

    private void checkAllergens(String recognizedText) {
        List<String> detectedAllergens = new ArrayList<>();

        for (String allergen : userAllergic) {
            if (recognizedText.toLowerCase().contains(allergen.toLowerCase())) {
                detectedAllergens.add(allergen);
            }
        }

        if (!detectedAllergens.isEmpty()) {
            binding.containsTV.setText("Contains: " + String.join(", ", detectedAllergens));
            binding.ltAllergensDetected.setVisibility(View.VISIBLE);
            binding.ltNoAllergens.setVisibility(View.GONE);
        } else {
            binding.ltAllergensDetected.setVisibility(View.GONE);
            binding.ltNoAllergens.setVisibility(View.VISIBLE);
        }
    }
}