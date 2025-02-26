package com.example.foodallergenfinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.model.CategoryProductResponse;

import java.util.List;

public class AlternativeProductAdapter extends RecyclerView.Adapter<AlternativeProductAdapter.ItemViewHolder> {

    private List<CategoryProductResponse.Product> itemList;

    public AlternativeProductAdapter(List<CategoryProductResponse.Product> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CategoryProductResponse.Product item = itemList.get(position);
        holder.textViewProductName.setText(item.getProductName());
        // Load image using a library like Glide or Picasso
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .error(R.drawable.item_image)
                .into(holder.imageViewProductImage);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // View holder class
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView textViewProductName; // Declare a TextView to hold item text
        ImageView imageViewProductImage; // Declare an ImageView to hold item image

        public ItemViewHolder(View itemView) {
            super(itemView);

            imageViewProductImage = itemView.findViewById(R.id.itemIV);
            textViewProductName = itemView.findViewById(R.id.titleTV);
        }
    }
}
