package com.example.foodallergenfinal.model;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.Keep;
import java.util.List;

@Keep
public class ResponseProduct {
    @SerializedName("code")
    private String code;

    @SerializedName("product")
    private Product product;

    @SerializedName("status")
    private Integer status;

    @SerializedName("status_verbose")
    private String statusVerbose;

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusVerbose() {
        return statusVerbose;
    }

    public void setStatusVerbose(String statusVerbose) {
        this.statusVerbose = statusVerbose;
    }

    @Keep
    public static class Product {
        @SerializedName("_keywords")
        private List<String> keywords;

        @SerializedName("allergens_from_user")
        private String allergensFromUser;

        @SerializedName("allergens_tags")
        private List<String> allergensTags;

        @SerializedName("brand_owner")
        private String brandOwner;

        @SerializedName("brands")
        private String brands;

        @SerializedName("categories_tags")
        private List<String> categoriesTags;

        @SerializedName("generic_name")
        private String genericName;

        @SerializedName("_id")
        private String id;

        @SerializedName("image_ingredients_url")
        private String imageIngredientsUrl;

        @SerializedName("image_nutrition_url")
        private String imageNutritionUrl;

        @SerializedName("image_url")
        private String imageUrl;

        @SerializedName("ingredients_tags")
        private List<String> ingredientsTags;

        @SerializedName("ingredients_text")
        private String ingredientsText;

        @SerializedName("nutrient_levels_tags")
        private List<String> nutrientLevelsTags;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("product_type")
        private String productType;

        @SerializedName("quantity")
        private String quantity;

        // Getters and Setters
        public String getAllergensFromUser() {
            return allergensFromUser;
        }

        public void setAllergensFromUser(String allergensFromUser) {
            this.allergensFromUser = allergensFromUser;
        }

        public List<String> getAllergensTags() {
            return allergensTags;
        }

        public void setAllergensTags(List<String> allergensTags) {
            this.allergensTags = allergensTags;
        }

        public String getBrandOwner() {
            return brandOwner;
        }

        public void setBrandOwner(String brandOwner) {
            this.brandOwner = brandOwner;
        }

        public String getBrands() {
            return brands;
        }

        public void setBrands(String brands) {
            this.brands = brands;
        }

        public List<String> getCategoriesTags() {
            return categoriesTags;
        }

        public void setCategoriesTags(List<String> categoriesTags) {
            this.categoriesTags = categoriesTags;
        }

        public String getGenericName() {
            return genericName;
        }

        public void setGenericName(String genericName) {
            this.genericName = genericName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageIngredientsUrl() {
            return imageIngredientsUrl;
        }

        public void setImageIngredientsUrl(String imageIngredientsUrl) {
            this.imageIngredientsUrl = imageIngredientsUrl;
        }

        public String getImageNutritionUrl() {
            return imageNutritionUrl;
        }

        public void setImageNutritionUrl(String imageNutritionUrl) {
            this.imageNutritionUrl = imageNutritionUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public List<String> getIngredientsTags() {
            return ingredientsTags;
        }

        public void setIngredientsTags(List<String> ingredientsTags) {
            this.ingredientsTags = ingredientsTags;
        }

        public String getIngredientsText() {
            return ingredientsText;
        }

        public void setIngredientsText(String ingredientsText) {
            this.ingredientsText = ingredientsText;
        }

        public List<String> getNutrientLevelsTags() {
            return nutrientLevelsTags;
        }

        public void setNutrientLevelsTags(List<String> nutrientLevelsTags) {
            this.nutrientLevelsTags = nutrientLevelsTags;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }
    }
}