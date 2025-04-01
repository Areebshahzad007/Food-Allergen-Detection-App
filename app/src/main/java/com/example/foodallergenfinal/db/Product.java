package com.example.foodallergenfinal.db;
// start....
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
//
    private List<String> allergensTags;
    //private String categoriesTags;
    private String imageUrl;
    private String ingredientsText;
    private String productName;

    public Product(List<String> allergensTags, String imageUrl, String ingredientsText, String productName) {
        this.allergensTags = allergensTags;
        //this.categoriesTags = categoriesTags;
        this.imageUrl = imageUrl;
        this.ingredientsText = ingredientsText;
        this.productName = productName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<String> getAllergensTags() { return allergensTags; }
    public void setAllergensTags(List<String> allergensTags) { this.allergensTags = allergensTags; }

    //public String getCategoriesTags() { return categoriesTags; }
    //public void setCategoriesTags(String categoriesTags) { this.categoriesTags = categoriesTags; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getIngredientsText() { return ingredientsText; }
    public void setIngredientsText(String ingredientsText) { this.ingredientsText = ingredientsText; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}

