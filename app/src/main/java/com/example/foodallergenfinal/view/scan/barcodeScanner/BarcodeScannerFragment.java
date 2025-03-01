package com.example.foodallergenfinal.view.scan.barcodeScanner;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
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

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.adapter.AlternativeProductAdapter;
import com.example.foodallergenfinal.databinding.FragmentBarcodeScannerBinding;
import com.example.foodallergenfinal.db.Product;
import com.example.foodallergenfinal.db.repo.DbProductViewModel;
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

    private boolean isFlashOn = false;
    private Camera cameraXInstance;

    private ProductViewModel viewModel;

    private List<String> userAllergic;

    private AlternativeProductAdapter adapter;
    private String lastScannedCode = null;

    private DbProductViewModel dbProductViewModel;

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
        dbProductViewModel = new ViewModelProvider(this).get(DbProductViewModel.class);

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
        
        binding.flashLightIV.setOnClickListener(v -> toggleFlashlight());
    }

    private void toggleFlashlight() {
        if (cameraXInstance != null) {
            isFlashOn = !isFlashOn;
            cameraXInstance.getCameraControl().enableTorch(isFlashOn);
            //binding.flashLightIV.setImageResource(isFlashOn ? R.drawable.flash_on : R.drawable.flash_off);
        } else {
            Toast.makeText(requireContext(), "CameraX is not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    private void observerListeners() {
        // Observe the LiveData for product details
        viewModel.getProduct().observe(getViewLifecycleOwner(), responseProduct -> {

            if (responseProduct != null && responseProduct.getProduct() != null) {
                List<String> allergislist = responseProduct.getProduct().getAllergensTags();
                List<String> allergensTags = new ArrayList<>();


                // save product to room
                Product product = new Product(
                        allergensTags,
                        responseProduct.getProduct().getImageUrl(),
                        responseProduct.getProduct().getIngredientsText(),
                        responseProduct.getProduct().getProductName()
                );
                dbProductViewModel.insert(product);

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

                                List<String> categoriesTags = responseProduct.getProduct().getCategoriesTags();
                                // Prevent crash: Check if categoriesTags is null or empty
                                if (categoriesTags == null || categoriesTags.isEmpty()) {
                                    Toast.makeText(requireContext(), "Category not found for this product", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String category = categoriesTags.get(0);  // Safe to access now

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
                Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show();
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
                    new AlertDialog.Builder(requireContext())
                            .setMessage("Camera permission is required to scan barcodes.")
                            .setPositiveButton("OK", null)
                            .show();
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
                cameraXInstance = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis
                );
                /*cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalysis);*/
            } catch (Exception exc) {
                Log.e("CameraX", "Use case binding failed", exc);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    @SuppressLint("SetTextI18n")
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
                        if (code != null && !code.equals(lastScannedCode)) {
                            lastScannedCode = code;
                            // Inside a Fragment (FragmentA or FragmentB)
                            getActivity().runOnUiThread(() -> {
                                binding.scannedDataTextView.setText("Scanned Code: " + code);
                                viewModel.fetchProductDetails(code);
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