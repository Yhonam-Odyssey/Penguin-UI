package com.yhonam.penguinui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * PenguinSheetAdapter - Adaptador interno del RecyclerView de PenguinSheet
 */
class PenguinSheetAdapter extends RecyclerView.Adapter<PenguinSheetAdapter.ViewHolder> {

    interface OnItemClickListener {
        void onItemClick(PenguinSheetItem item, int position);
    }

    private final List<PenguinSheetItem> items;
    private final OnItemClickListener    listener;

    PenguinSheetAdapter(List<PenguinSheetItem> items, OnItemClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bottom_sheet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemIcon;
        private final TextView  itemText;
        private final View      itemContainer;

        ViewHolder(View itemView) {
            super(itemView);
            itemIcon      = itemView.findViewById(R.id.bottomSheetItemIcon);
            itemText      = itemView.findViewById(R.id.bottomSheetItemText);
            itemContainer = itemView.findViewById(R.id.bottomSheetItemContainer);
        }

        void bind(PenguinSheetItem item) {
            itemIcon.setImageResource(item.getIconResId());

            int color = item.hasCustomColor()
                    ? ContextCompat.getColor(itemIcon.getContext(), item.getIconColor())
                    : ContextCompat.getColor(itemIcon.getContext(), R.color.dialog_accent_cyan);

            itemIcon.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
            itemText.setText(item.getText());

            itemContainer.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(item, pos);
                }
            });
        }
    }
}
