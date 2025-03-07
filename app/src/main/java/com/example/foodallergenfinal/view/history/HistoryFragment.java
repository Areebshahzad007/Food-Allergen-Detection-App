package com.example.foodallergenfinal.view.history;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.foodallergenfinal.db.Product;
import com.example.foodallergenfinal.db.repo.DbProductViewModel;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private DbProductViewModel dbProductViewModel;

    private HistoryProductAdapter adapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        dbProductViewModel = new ViewModelProvider(this).get(DbProductViewModel.class);
        recyclerViewSet();

        binding.scanYourProductButton.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_historyFragment_to_barcodeScannerFragment);
        });

        binding.btnMore.setOnClickListener(v->{
            binding.btnClearHistory.setVisibility(View.VISIBLE);
        });

        binding.btnClearHistory.setOnClickListener(v->{
            dbProductViewModel.allDelete();
            recyclerViewSet();
            binding.btnClearHistory.setVisibility(View.GONE);
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

                // Enable swipe-to-delete functionality
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false; // We are not handling move
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Get position of swiped item
                        int position = viewHolder.getAdapterPosition();
                        Product productToDelete = products.get(position);

                        // Delete from database and adapter
                        dbProductViewModel.delete(productToDelete);
                        adapter.removeItem(productToDelete);

                        Toast.makeText(requireContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                    }
                }).attachToRecyclerView(binding.recyclerView);

            } else {
                binding.recyclerView.setVisibility(View.GONE);
                binding.noProductTV.setVisibility(View.VISIBLE);
                binding.scanYourProductButton.setVisibility(View.VISIBLE);
            }
        });
    }


}