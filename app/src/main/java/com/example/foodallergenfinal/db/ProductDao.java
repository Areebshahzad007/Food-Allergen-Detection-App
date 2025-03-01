package com.example.foodallergenfinal.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAllProducts();

    @Delete
    void deleteProduct(Product product);
}

