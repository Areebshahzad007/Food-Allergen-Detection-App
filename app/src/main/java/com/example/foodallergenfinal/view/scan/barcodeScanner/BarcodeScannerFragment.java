package com.example.foodallergenfinal.view.scan.barcodeScanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodallergenfinal.adapter.AlternativeProductAdapter;
import com.example.foodallergenfinal.databinding.FragmentBarcodeScannerBinding;
import com.example.foodallergenfinal.model.CategoryProductResponse;
import com.example.foodallergenfinal.utils.PrefsManager;
import com.example.foodallergenfinal.view.scan.ProductViewModel;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarcodeScannerFragment extends Fragment {
    private FragmentBarcodeScannerBinding binding;
    private ExecutorService cameraExecutor;

    private ProductViewModel viewModel;

    private List<String> userAllergic;

    private AlternativeProductAdapter adapter;

    public BarcodeScannerFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false);

        requestCameraPermission();
        cameraExecutor = Executors.newSingleThreadExecutor();

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Initialize userAllergic list
        userAllergic = new ArrayList<>();

        // Retrieve saved allergic items
        Set<String> savedItems = PrefsManager.getSavedAllergies(requireContext());

        // Add all saved allergic items to userAllergic list
        if (savedItems != null) {
            for (String item : savedItems) {
                userAllergic.add(item.toLowerCase());
                Log.d("TAG", "Added allergen: " + item);
            }
        }
        Log.d("TAG", "Final userAllergic list: " + userAllergic);

        // listeners
        setupListeners();
        observerListeners();

        return binding.getRoot();
    }

    private void setupListeners() {
        binding.cancelIV.setOnClickListener(v -> {
            binding.ltAllergensDetected.setVisibility(View.GONE);
        });

        binding.cancelIV2.setOnClickListener(v -> {
            binding.ltNoAllergens.setVisibility(View.GONE);
        });
    }

    private void observerListeners() {
        // Observe the LiveData for product details
        viewModel.getProduct().observe(getViewLifecycleOwner(), responseProduct -> {

            if (responseProduct != null) {
                List<String> allergislist = responseProduct.getProduct().getAllergensTags();
                List<String> allergensTags = new ArrayList<>();

                String category = responseProduct.getProduct().getCategoriesTags().get(0);
                //category = category.substring(3);

                Log.d("TAG", "category: "+ category);

                if (allergislist != null && !allergislist.isEmpty()) {
                    StringBuilder allergensText = new StringBuilder();

                    for (String allergen : allergislist) {
                        String cleanAllergen = allergen.replace("en:", ""); // Remove "en:"
                        allergensText.append(cleanAllergen).append(", "); // Append allergen with a separator
                        allergensTags.add(cleanAllergen);
                    }

                    // Remove the last comma and space
                    if (allergensText.length() > 2) {
                        allergensText.setLength(allergensText.length() - 2);
                    }

                    // Check for any matches
                    boolean isMatchFound = false;

                    for (String str1 : allergensTags) {
                        for (String str2 : userAllergic) {
                            if (str1.equals(str2)) {
                                String allergensString = allergensText.toString();
                                binding.containsTV.setText("Contains: " + allergensString);
                                binding.ltAllergensDetected.setVisibility(View.VISIBLE);

                                viewModel.fetchCategoryProducts(
                                        category.substring(3),
                                        "-en:"+str2,
                                        10
                                );

                                isMatchFound = true;
                            }
                        }
                    }

                    //No matches found.
                    if (!isMatchFound) {
                        binding.ltNoAllergens.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.ltNoAllergens.setVisibility(View.VISIBLE);
                }

            } else {
                // Handle the error case
                //Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show();
            }

        });

        // Observer for category products
        viewModel.getCategoryProducts().observe(getViewLifecycleOwner(), categoryProductResponse -> {
            if (categoryProductResponse != null) {
                List<CategoryProductResponse.Product> products = categoryProductResponse.getProducts();
                // recycler view
                adapter = new AlternativeProductAdapter(products);
                binding.recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(requireContext(), "Alternative Product not found", Toast.LENGTH_SHORT).show();
            }
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
        ProcessCameraProvider.getInstance(requireContext()).addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(requireContext()).get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, this::processBarcode);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis);
            } catch (Exception exc) {
                Log.e("CameraX", "Use case binding failed", exc);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void processBarcode(ImageProxy imageProxy) {
        if (imageProxy.getImage() == null) {
            imageProxy.close();
            return;
        }

        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
        BarcodeScanning.getClient().process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String code = barcode.getRawValue();
                        if (code != null) {
                            // Inside a Fragment (FragmentA or FragmentB)
                            getActivity().runOnUiThread(() -> {
                                binding.scannedDataTextView.setText("Scanned Code: " + code);
                                viewModel.fetchProductDetails("6111242100992");
                                //6111242100992
                            });

                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("BarcodeScanner", "Error scanning barcode", e))
                .addOnCompleteListener(task -> imageProxy.close());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cameraExecutor.shutdown();
    }
}