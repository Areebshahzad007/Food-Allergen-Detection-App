package com.example.foodallergenfinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.db.Product;
import com.example.foodallergenfinal.model.CategoryProductResponse;

import java.util.List;

public class HistoryProductAdapter extends RecyclerView.Adapter<HistoryProductAdapter.ItemViewHolder> {

    private List<Product> itemList;

    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(Product product);
    }

    public HistoryProductAdapter(List<Product> itemList, OnItemLongClickListener onItemLongClickListener) {
        this.itemList = itemList;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Product item = itemList.get(position);
        holder.textViewProductName.setText(item.getProductName());
        holder.textViewIngredients.setText(item.getIngredientsText());
        // Load image using a library like Glide or Picasso
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .error(R.drawable.item_image)
                .into(holder.imageViewProductImage);

        // Set long click listener for deleting an item
        holder.itemView.setOnLongClickListener(v -> {
            onItemLongClickListener.onItemLongClick(item);
            return true; // Returning true indicates the event is consumed
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // View holder class
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView textViewProductName; // Declare a TextView to hold item text
        ImageView imageViewProductImage; // Declare an ImageView to hold item image
        TextView textViewIngredients;

        public ItemViewHolder(View itemView) {
            super(itemView);

            imageViewProductImage = itemView.findViewById(R.id.itemIV);
            textViewProductName = itemView.findViewById(R.id.titleTV);
            textViewIngredients = itemView.findViewById(R.id.ingredientsTV);
        }
    }

    // Method to update the list after deletion
    public void removeItem(Product product) {
        int position = itemList.indexOf(product);
        if (position != -1) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
