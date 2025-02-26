package com.example.foodallergenfinal.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import java.util.List;

@Keep
public class CategoryProductResponse {

    @SerializedName("count")
    private int count;

    @SerializedName("page")
    private int page;

    @SerializedName("page_count")
    private int pageCount;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("skip")
    private int skip;

    @SerializedName("products")
    private List<Product> products;

    // Getters and Setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Inner class for Product details
    @Keep
    public static class Product {

        @SerializedName("_id")
        private String id;

        @SerializedName("_keywords")
        private List<String> keywords;

        @SerializedName("allergens_tags")
        private List<String> allergensTags;

        @SerializedName("categories_tags")
        private List<String> categoriesTags;

        @SerializedName("image_url")
        private String imageUrl;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("ingredients_tags")
        private List<String> ingredientsTags;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public List<String> getAllergensTags() {
            return allergensTags;
        }

        public void setAllergensTags(List<String> allergensTags) {
            this.allergensTags = allergensTags;
        }

        public List<String> getCategoriesTags() {
            return categoriesTags;
        }

        public void setCategoriesTags(List<String> categoriesTags) {
            this.categoriesTags = categoriesTags;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public List<String> getIngredientsTags() {
            return ingredientsTags;
        }

        public void setIngredientsTags(List<String> ingredientsTags) {
            this.ingredientsTags = ingredientsTags;
        }
    }
}