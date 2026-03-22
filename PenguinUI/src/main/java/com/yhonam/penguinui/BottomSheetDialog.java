package com.yhonam.penguinui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
 * BottomSheetDialog - Menú de acciones que se desliza desde la parte inferior
 *
 * Uso:
 * 1. Crear lista de BottomSheetItem
 * 2. Crear instancia con newInstance()
 * 3. Mostrar con show(getSupportFragmentManager(), "tag")
 *
 * Ejemplo:
 * <pre>
 * {@code
 * List<BottomSheetItem> items = new ArrayList<>();
 * items.add(new BottomSheetItem(R.drawable.ic_edit, "Editar", () -> editRecord()));
 * items.add(new BottomSheetItem(R.drawable.ic_delete, "Eliminar", () -> deleteRecord()));
 *
 * BottomSheetDialog bottomSheet = BottomSheetDialog.newInstance(
 *     "Opciones de registro",
 *     items
 * );
 * bottomSheet.show(getSupportFragmentManager(), "bottom_sheet");
 * }
 * </pre>
 */
public class BottomSheetDialog extends BottomSheetDialogFragment {

    // Views
    private TextView bottomSheetTitle;
    private RecyclerView bottomSheetRecyclerView;
    private Button btnCancel;
    private View bottomSheetHandle;
    private View bottomSheetContainer;

    // Parámetros
    private String title;
    private List<BottomSheetItem> items;
    private boolean showCancelButton;

    // Adapter
    private BottomSheetAdapter adapter;

    /**
     * Constructores requeridos
     */
    public BottomSheetDialog() {
        // Requerido para BottomSheetDialogFragment
    }

    /**
     * Crear nueva instancia del bottom sheet
     *
     * @param title Título del bottom sheet
     * @param items Lista de items a mostrar
     * @return Instancia de BottomSheetDialog
     */
    public static BottomSheetDialog newInstance(String title, List<BottomSheetItem> items) {
        return newInstance(title, items, true);
    }

    /**
     * Crear nueva instancia del bottom sheet
     *
     * @param title Título del bottom sheet
     * @param items Lista de items a mostrar
     * @param showCancelButton Mostrar botón de cancelar al final
     * @return Instancia de BottomSheetDialog
     */
    public static BottomSheetDialog newInstance(String title, List<BottomSheetItem> items, boolean showCancelButton) {
        BottomSheetDialog fragment = new BottomSheetDialog();
        Bundle args = new Bundle();
        args.putString("title", title != null ? title : "Opciones");
        args.putBoolean("showCancelButton", showCancelButton);
        fragment.setArguments(args);
        fragment.title = title;
        fragment.items = items;
        fragment.showCancelButton = showCancelButton;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener parámetros de los argumentos
        if (getArguments() != null) {
            title = getArguments().getString("title", "Opciones");
            showCancelButton = getArguments().getBoolean("showCancelButton", true);
        }

        // Inicializar lista si es null
        if (items == null) {
            items = new ArrayList<>();
        }
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

        // Inicializar vistas
        initViews(view);

        // Configurar contenido
        setupContent();

        // Configurar listeners
        setupListeners();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Configurar ventana
        setupDialogWindow(dialog);

        return dialog;
    }

    /**
     * Inicializar referencias a las vistas
     */
    private void initViews(View view) {
        bottomSheetTitle = view.findViewById(R.id.bottomSheetTitle);
        bottomSheetRecyclerView = view.findViewById(R.id.bottomSheetRecyclerView);
        btnCancel = view.findViewById(R.id.btnCancel);
        bottomSheetHandle = view.findViewById(R.id.bottomSheetHandle);
        bottomSheetContainer = view.findViewById(R.id.bottomSheetContainer);
    }

    /**
     * Configurar el contenido del bottom sheet
     */
    private void setupContent() {
        // Configurar título
        if (title != null && !title.isEmpty()) {
            bottomSheetTitle.setText(title);
        } else {
            bottomSheetTitle.setVisibility(View.GONE);
        }

        // Configurar visibilidad del botón de cancelar
        btnCancel.setVisibility(showCancelButton ? View.VISIBLE : View.GONE);

        // Configurar RecyclerView
        adapter = new BottomSheetAdapter(items, (item, position) -> {
            // Ejecutar acción del item
            if (item.getAction() != null) {
                item.getAction().run();
            }
            // Cerrar bottom sheet
            dismiss();
        });

        bottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bottomSheetRecyclerView.setAdapter(adapter);
    }

    /**
     * Configurar listeners
     */
    private void setupListeners() {
        // Botón de cancelar
        btnCancel.setOnClickListener(v -> dismiss());
    }

    /**
     * Configurar la ventana del diálogo
     */
    private void setupDialogWindow(Dialog dialog) {
        if (dialog.getWindow() == null) return;

        Window window = dialog.getWindow();

        // Fondo transparente
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Animaciones
        window.setWindowAnimations(R.style.BottomSheetAnimation);

        // Configurar para que ocupe todo el ancho
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Max height (80% de la pantalla)
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int maxHeight = (int) (metrics.heightPixels * 0.8);
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;

        window.setAttributes(params);

        // Sin dim background para bottom sheet
        window.setDimAmount(0.5f);
    }

    /**
     * Agregar un item al bottom sheet
     *
     * @param item Item a agregar
     */
    public void addItem(BottomSheetItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        if (adapter != null) {
            adapter.notifyItemInserted(items.size() - 1);
        }
    }

    /**
     * Agregar múltiples items
     *
     * @param newItems Lista de items a agregar
     */
    public void addItems(List<BottomSheetItem> newItems) {
        if (items == null) {
            items = new ArrayList<>();
        }
        int startPosition = items.size();
        items.addAll(newItems);
        if (adapter != null) {
            adapter.notifyItemRangeInserted(startPosition, newItems.size());
        }
    }

    /**
     * Remover un item por posición
     *
     * @param position Posición del item a remover
     */
    public void removeItem(int position) {
        if (items != null && position >= 0 && position < items.size()) {
            items.remove(position);
            if (adapter != null) {
                adapter.notifyItemRemoved(position);
            }
        }
    }

    /**
     * Limpiar todos los items
     */
    public void clearItems() {
        if (items != null) {
            items.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Actualizar el título
     *
     * @param title Nuevo título
     */
    public void updateTitle(String title) {
        this.title = title;
        if (bottomSheetTitle != null) {
            bottomSheetTitle.setText(title);
            bottomSheetTitle.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Mostrar u ocultar el botón de cancelar
     *
     * @param show true para mostrar, false para ocultar
     */
    public void showCancelButton(boolean show) {
        this.showCancelButton = show;
        if (btnCancel != null) {
            btnCancel.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Builder pattern para creación fluida
     */
    public static class Builder {
        private String title;
        private final List<BottomSheetItem> items = new ArrayList<>();
        private boolean showCancelButton = true;

        /**
         * Configurar título
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Agregar un item
         */
        public Builder addItem(int iconResId, String text, Runnable action) {
            items.add(new BottomSheetItem(iconResId, text, action));
            return this;
        }

        /**
         * Agregar un item con color personalizado
         */
        public Builder addItem(int iconResId, String text, Runnable action, int iconColorResId) {
            items.add(new BottomSheetItem(iconResId, text, action, iconColorResId));
            return this;
        }

        /**
         * Configurar visibilidad del botón de cancelar
         */
        public Builder showCancelButton(boolean show) {
            this.showCancelButton = show;
            return this;
        }

        /**
         * Construir el BottomSheetDialog
         */
        public BottomSheetDialog build() {
            return BottomSheetDialog.newInstance(title, items, showCancelButton);
        }
    }
}
