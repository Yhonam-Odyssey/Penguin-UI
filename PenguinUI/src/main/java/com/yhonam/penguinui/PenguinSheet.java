package com.yhonam.penguinui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * PenguinSheet - Menú de acciones que se desliza desde la parte inferior
 *
 * Uso con Builder (recomendado):
 *   new PenguinSheet.Builder()
 *       .setTitle("Opciones")
 *       .addItem(R.drawable.ic_edit,   "Editar",   () -> editar())
 *       .addItem(R.drawable.ic_delete, "Eliminar", () -> eliminar(), R.color.red)
 *       .build()
 *       .show(getSupportFragmentManager(), "sheet");
 *
 * Uso directo con newInstance:
 *   List<PenguinSheetItem> items = new ArrayList<>();
 *   items.add(new PenguinSheetItem(R.drawable.ic_share, "Compartir", this::share));
 *   PenguinSheet.newInstance("Acciones", items)
 *       .show(getSupportFragmentManager(), "sheet");
 */
public class PenguinSheet extends BottomSheetDialogFragment {

    // Views
    private TextView         sheetTitle;
    private RecyclerView     sheetRecyclerView;
    private Button           btnCancel;

    // Parámetros
    private String                 title;
    private List<PenguinSheetItem> items;
    private boolean                showCancelButton;

    // Adapter
    private PenguinSheetAdapter adapter;

    public PenguinSheet() {}

    // ─── Creación ──────────────────────────────────────────────────────────────

    public static PenguinSheet newInstance(String title, List<PenguinSheetItem> items) {
        return newInstance(title, items, true);
    }

    public static PenguinSheet newInstance(String title, List<PenguinSheetItem> items,
                                           boolean showCancelButton) {
        PenguinSheet f = new PenguinSheet();
        Bundle args = new Bundle();
        args.putString("title", title != null ? title : "Opciones");
        args.putBoolean("showCancelButton", showCancelButton);
        f.setArguments(args);
        f.title            = title;
        f.items            = items;
        f.showCancelButton = showCancelButton;
        return f;
    }

    // ─── Ciclo de vida ─────────────────────────────────────────────────────────

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title            = getArguments().getString("title", "Opciones");
            showCancelButton = getArguments().getBoolean("showCancelButton", true);
        }
        if (items == null) items = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sheetTitle        = view.findViewById(R.id.bottomSheetTitle);
        sheetRecyclerView = view.findViewById(R.id.bottomSheetRecyclerView);
        btnCancel         = view.findViewById(R.id.btnCancel);

        setupContent();
        btnCancel.setOnClickListener(v -> dismiss());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setupWindow(dialog);
        return dialog;
    }

    // ─── Modificación dinámica ────────────────────────────────────────────────

    public void addItem(PenguinSheetItem item) {
        if (items == null) items = new ArrayList<>();
        items.add(item);
        if (adapter != null) adapter.notifyItemInserted(items.size() - 1);
    }

    public void addItems(List<PenguinSheetItem> newItems) {
        if (items == null) items = new ArrayList<>();
        int start = items.size();
        items.addAll(newItems);
        if (adapter != null) adapter.notifyItemRangeInserted(start, newItems.size());
    }

    public void removeItem(int position) {
        if (items != null && position >= 0 && position < items.size()) {
            items.remove(position);
            if (adapter != null) adapter.notifyItemRemoved(position);
        }
    }

    public void clearItems() {
        if (items != null) {
            items.clear();
            if (adapter != null) adapter.notifyDataSetChanged();
        }
    }

    public void updateTitle(String title) {
        this.title = title;
        if (sheetTitle != null) {
            sheetTitle.setText(title);
            sheetTitle.setVisibility(View.VISIBLE);
        }
    }

    public void showCancelButton(boolean show) {
        this.showCancelButton = show;
        if (btnCancel != null) btnCancel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // ─── Interno ───────────────────────────────────────────────────────────────

    private void setupContent() {
        if (title != null && !title.isEmpty()) {
            sheetTitle.setText(title);
        } else {
            sheetTitle.setVisibility(View.GONE);
        }

        btnCancel.setVisibility(showCancelButton ? View.VISIBLE : View.GONE);

        adapter = new PenguinSheetAdapter(items, (item, position) -> {
            if (item.getAction() != null) item.getAction().run();
            dismiss();
        });

        sheetRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sheetRecyclerView.setAdapter(adapter);
    }

    private void setupWindow(Dialog dialog) {
        if (dialog.getWindow() == null) return;
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.BottomSheetAnimation);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width   = WindowManager.LayoutParams.MATCH_PARENT;
        params.height  = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setDimAmount(0.5f);
    }

    // ─── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        private String                       title;
        private final List<PenguinSheetItem> items            = new ArrayList<>();
        private boolean                      showCancelButton  = true;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder addItem(int iconResId, String text, Runnable action) {
            items.add(new PenguinSheetItem(iconResId, text, action));
            return this;
        }

        public Builder addItem(int iconResId, String text, Runnable action, int iconColorResId) {
            items.add(new PenguinSheetItem(iconResId, text, action, iconColorResId));
            return this;
        }

        public Builder showCancelButton(boolean show) {
            this.showCancelButton = show;
            return this;
        }

        public PenguinSheet build() {
            return PenguinSheet.newInstance(title, items, showCancelButton);
        }
    }
}
