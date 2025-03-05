package com.example.foodallergenfinal.db.repo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodallergenfinal.db.Product;

import java.util.List;

public class DbProductViewModel extends AndroidViewModel {
    private final ProductRepository repository;
    private final LiveData<List<Product>> allProducts;

    public DbProductViewModel(Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
    }

    public void insert(Product product) {
        repository.insert(product);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public void allDelete() {
        repository.allDelete();  // Calling allDelete from repository
    }

}