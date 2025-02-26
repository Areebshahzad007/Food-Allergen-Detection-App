package com.example.foodallergenfinal.service;

import com.example.foodallergenfinal.model.CategoryProductResponse;
import com.example.foodallergenfinal.model.ResponseProduct;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenFoodFactsApiService {
    @GET("product/{barcode}.json")
    Call<ResponseProduct> getProductDetails(@Path("barcode")String barcode);

    @GET("search")
    Call<CategoryProductResponse> searchProducts(
            @Query("categories_tags_en") String category,
            @Query("allergens_tags") String allergens,
            @Query("page_size") int pageSize
    );
}

