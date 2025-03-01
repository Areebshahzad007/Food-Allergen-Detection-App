package com.example.foodallergenfinal.view.history;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.adapter.HistoryProductAdapter;
import com.example.foodallergenfinal.databinding.FragmentHistoryBinding;
import com.example.foodallergenfinal.db.repo.DbProductViewModel;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private DbProductViewModel dbProductViewModel;

    private HistoryProductAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        dbProductViewModel = new ViewModelProvider(this).get(DbProductViewModel.class);
        recyclerViewSet();

        binding.scanYourProductButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_historyFragment_to_barcodeScannerFragment);
        });

        return binding.getRoot();
    }

    private void recyclerViewSet() {
        dbProductViewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null && !products.isEmpty()) {
                adapter = new HistoryProductAdapter(products, product -> {
                    // Delete product on long click
                    dbProductViewModel.delete(product);
                    adapter.removeItem(product);
                    Toast.makeText(requireContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                });
                binding.recyclerView.setAdapter(adapter);
                binding.noProductTV.setVisibility(View.GONE);
                binding.scanYourProductButton.setVisibility(View.GONE);
            } else {
                Toast.makeText(requireContext(), "No products found", Toast.LENGTH_SHORT).show();
            }
        });
    }


}