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
 * BottomSheetAdapter - Adaptador para el RecyclerView del Bottom Sheet
 */
public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {

    private final List<BottomSheetItem> items;
    private final OnItemClickListener listener;

    /**
     * Listener para clicks en los items
     */
    public interface OnItemClickListener {
        void onItemClick(BottomSheetItem item, int position);
    }

    /**
     * Constructor
     *
     * @param items Lista de items
     * @param listener Listener para clicks
     */
    public BottomSheetAdapter(List<BottomSheetItem> items, OnItemClickListener listener) {
        this.items = items;
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
        BottomSheetItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * ViewHolder para los items del bottom sheet
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemIcon;
        private final TextView itemText;
        private final View itemContainer;

        ViewHolder(View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById(R.id.bottomSheetItemIcon);
            itemText = itemView.findViewById(R.id.bottomSheetItemText);
            itemContainer = itemView.findViewById(R.id.bottomSheetItemContainer);
        }

        void bind(BottomSheetItem item) {
            // Configurar ícono
            itemIcon.setImageResource(item.getIconResId());

            // Configurar color del ícono si es personalizado
            if (item.hasCustomColor()) {
                itemIcon.setColorFilter(
                        ContextCompat.getColor(itemIcon.getContext(), item.getIconColor()),
                        android.graphics.PorterDuff.Mode.SRC_IN
                );
            } else {
                // Color por defecto (cyan)
                itemIcon.setColorFilter(
                        ContextCompat.getColor(itemIcon.getContext(), R.color.dialog_accent_cyan),
                        android.graphics.PorterDuff.Mode.SRC_IN
                );
            }

            // Configurar texto
            itemText.setText(item.getText());

            // Configurar click listener
            itemContainer.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(item, position);
                }
            });
        }
    }
}
