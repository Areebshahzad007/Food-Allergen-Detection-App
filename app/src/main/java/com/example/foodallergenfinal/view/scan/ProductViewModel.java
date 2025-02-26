package com.example.foodallergenfinal.view.scan;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodallergenfinal.model.CategoryProductResponse;
import com.example.foodallergenfinal.model.ResponseProduct;
import com.example.foodallergenfinal.service.OpenFoodFactsApiService;
import com.example.foodallergenfinal.service.OpenFoodFactsClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<ResponseProduct> productLiveData = new MutableLiveData<>();

    public LiveData<ResponseProduct> getProduct() {
        return productLiveData;
    }

    public void fetchProductDetails(String barcode) {
        OpenFoodFactsApiService apiService = OpenFoodFactsClient.getApiService();

        // Asynchronous Retrofit call using enqueue()
        Call<ResponseProduct> call = apiService.getProductDetails(barcode);

        // Enqueue the call for asynchronous execution
        call.enqueue(new Callback<ResponseProduct>() {
            @Override
            public void onResponse(Call<ResponseProduct> call, Response<ResponseProduct> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Post the response body into LiveData
                    Log.d("TAG", "Response Body: " + response.body());

                    productLiveData.setValue(response.body());
                } else {
                    // Handle case when the response is not successful
                    productLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseProduct> call, Throwable t) {
                // Handle failure, such as network errors
                productLiveData.setValue(null);
            }
        });
    }


    // category products
    private final MutableLiveData<CategoryProductResponse> categoryProductLiveData = new MutableLiveData<>();

    public LiveData<CategoryProductResponse> getCategoryProducts() {
        return categoryProductLiveData;
    }

    public void fetchCategoryProducts(String category, String allergens, int pageSize) {
        OpenFoodFactsApiService apiService = OpenFoodFactsClient.getApiService();

        // Make an API call to fetch products based on category and allergens
        Call<CategoryProductResponse> call = apiService.searchProducts(category, allergens, pageSize);

        call.enqueue(new Callback<CategoryProductResponse>() {
            @Override
            public void onResponse(Call<CategoryProductResponse> call, Response<CategoryProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Category Products Response: " + response.body());
                    categoryProductLiveData.setValue(response.body());
                } else {
                    Log.e("API_ERROR", "Response unsuccessful: " + response.message());
                    categoryProductLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CategoryProductResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Failed to fetch data: " + t.getMessage());
                categoryProductLiveData.setValue(null);
            }
        });
    }


}
