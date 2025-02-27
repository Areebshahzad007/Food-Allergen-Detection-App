package com.example.foodallergenfinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodallergenfinal.R;
import com.example.foodallergenfinal.model.AllergicItem;

import java.util.List;

public class AllergicAdapter extends RecyclerView.Adapter<AllergicAdapter.ViewHolder> {
    private final List<AllergicItem> items;
    private final OnToggleChangeListener toggleChangeListener;

    public interface OnToggleChangeListener {
        void onToggleChanged(String itemName, boolean isSelected);
    }

    public AllergicAdapter(List<AllergicItem> items, OnToggleChangeListener listener) {
        this.items = items;
        this.toggleChangeListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView itemName;
        SwitchCompat switchButton;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.objectIV);
            itemName = view.findViewById(R.id.objectTV);
            switchButton = view.findViewById(R.id.switchButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allergie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllergicItem item = items.get(position);
        holder.imageView.setImageResource(item.getImg());
        holder.itemName.setText(item.getName());
        holder.switchButton.setChecked(item.isSelected());

        holder.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
            toggleChangeListener.onToggleChanged(item.getName(), isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

