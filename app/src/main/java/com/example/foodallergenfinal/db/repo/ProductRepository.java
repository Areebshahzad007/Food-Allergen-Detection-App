package com.example.foodallergenfinal.db.repo;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.foodallergenfinal.db.Product;
import com.example.foodallergenfinal.db.ProductDao;
import com.example.foodallergenfinal.db.ProductDatabase;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {
    private final ProductDao productDao;
    private final LiveData<List<Product>> allProducts;
    private final ExecutorService executorService;

    public ProductRepository(Application application) {
        ProductDatabase database = ProductDatabase.getInstance(application);
        productDao = database.productDao();
        allProducts = productDao.getAllProducts();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insert(Product product) {
        executorService.execute(() -> productDao.insert(product));
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void delete(Product product) {
        executorService.execute(() -> productDao.deleteProduct(product));
    }

}
