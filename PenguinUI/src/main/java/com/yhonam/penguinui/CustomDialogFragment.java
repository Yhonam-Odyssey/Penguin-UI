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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/**
 * CustomDialogFragment - Diálogo personalizado con tema oscuro y colores vibrantes
 *
 * Uso:
 * 1. Crear instancia con newInstance()
 * 2. Configurar listeners con setOnConfirmListener() y setOnCancelListener()
 * 3. Mostrar con show(getSupportFragmentManager(), "tag")
 */
public class CustomDialogFragment extends DialogFragment {

    // Callbacks
    public interface OnConfirmListener {
        void onConfirm();
    }

    public interface OnCancelListener {
        void onCancel();
    }

    // Parámetros del diálogo
    private String title;
    private String message;
    private String positiveButtonText;
    private String negativeButtonText;
    private int iconResId = -1;
    private boolean showIcon = true;

    // Listeners
    private OnConfirmListener confirmListener;
    private OnCancelListener cancelListener;

    // Views
    private ImageView dialogIcon;
    private TextView dialogTitle;
    private TextView dialogMessage;
    private Button btnPositive;
    private Button btnNegative;
    private View headerContainer;
    private View buttonContainer;

    // Constructores requeridos
    public CustomDialogFragment() {
        // Requerido para DialogFragment
    }

    /**
     * Crear nueva instancia del diálogo
     *
     * @param title Título del diálogo
     * @param message Mensaje del diálogo
     * @return Instancia de CustomDialogFragment
     */
    public static CustomDialogFragment newInstance(String title, String message) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Crear nueva instancia con texto personalizado para botones
     *
     * @param title Título del diálogo
     * @param message Mensaje del diálogo
     * @param positiveButtonText Texto del botón positivo (confirmar)
     * @param negativeButtonText Texto del botón negativo (cancelar)
     * @return Instancia de CustomDialogFragment
     */
    public static CustomDialogFragment newInstance(String title, String message,
                                                   String positiveButtonText, String negativeButtonText) {
        CustomDialogFragment fragment = newInstance(title, message);
        fragment.positiveButtonText = positiveButtonText;
        fragment.negativeButtonText = negativeButtonText;
        return fragment;
    }

    /**
     * Configurar listener para botón de confirmar
     */
    public void setOnConfirmListener(OnConfirmListener listener) {
        this.confirmListener = listener;
    }

    /**
     * Configurar listener para botón de cancelar
     */
    public void setOnCancelListener(OnCancelListener listener) {
        this.cancelListener = listener;
    }

    /**
     * Configurar ícono personalizado
     *
     * @param iconResId Resource ID del ícono
     * @return this (para método fluido)
     */
    public CustomDialogFragment setIcon(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    /**
     * Mostrar u ocultar el ícono
     *
     * @param show true para mostrar, false para ocultar
     * @return this (para método fluido)
     */
    public CustomDialogFragment showIcon(boolean show) {
        this.showIcon = show;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener parámetros de los argumentos
        if (getArguments() != null) {
            title = getArguments().getString("title", "Confirmar");
            message = getArguments().getString("message", "");
        }

        // Configurar estilo sin título nativo
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_custom_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar vistas
        initViews(view);

        // Configurar contenido
        setupContent();

        // Configurar listeners de botones
        setupButtons();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Configurar ventana del diálogo
        setupDialogWindow(dialog);

        return dialog;
    }

    /**
     * Inicializar referencias a las vistas
     */
    private void initViews(View view) {
        dialogIcon = view.findViewById(R.id.dialogIcon);
        dialogTitle = view.findViewById(R.id.dialogTitle);
        dialogMessage = view.findViewById(R.id.dialogMessage);
        btnPositive = view.findViewById(R.id.btnPositive);
        btnNegative = view.findViewById(R.id.btnNegative);
        headerContainer = view.findViewById(R.id.headerContainer);
        buttonContainer = view.findViewById(R.id.buttonContainer);
    }

    /**
     * Configurar el contenido del diálogo (título, mensaje, ícono)
     */
    private void setupContent() {
        // Configurar título
        if (title != null && !title.isEmpty()) {
            dialogTitle.setText(title);
        }

        // Configurar mensaje
        if (message != null && !message.isEmpty()) {
            dialogMessage.setText(message);
        }

        // Configurar ícono
        if (showIcon) {
            if (iconResId != -1) {
                dialogIcon.setImageResource(iconResId);
                dialogIcon.setVisibility(View.VISIBLE);
            } else {
                // Ícono por defecto (información)
                dialogIcon.setImageResource(R.drawable.ic_dialog_check);
                dialogIcon.setVisibility(View.VISIBLE);
            }
        } else {
            dialogIcon.setVisibility(View.GONE);
        }

        // Configurar textos de botones
        if (positiveButtonText != null && !positiveButtonText.isEmpty()) {
            btnPositive.setText(positiveButtonText);
        }
        if (negativeButtonText != null && !negativeButtonText.isEmpty()) {
            btnNegative.setText(negativeButtonText);
        }
    }

    /**
     * Configurar listeners y comportamientos de los botones
     */
    private void setupButtons() {
        // Botón positivo (confirmar)
        btnPositive.setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onConfirm();
            }
            dismiss();
        });

        // Botón negativo (cancelar)
        btnNegative.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
            dismiss();
        });
    }

    /**
     * Configurar la ventana del diálogo para apariencia personalizada
     */
    private void setupDialogWindow(Dialog dialog) {
        if (dialog.getWindow() == null) return;

        Window window = dialog.getWindow();

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        window.setWindowAnimations(R.style.DialogAnimation);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int maxWidthPx = (int) (400 * metrics.density);
        int desiredWidthPx = (int) (screenWidth * 0.85);
        params.width = Math.min(desiredWidthPx, maxWidthPx);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);

        window.setDimAmount(0.5f);
    }

    /**
     * Tipos predefinidos de diálogo con sus íconos y colores
     */
    public enum DialogType {
        CONFIRM(R.drawable.ic_dialog_check),
        DELETE(R.drawable.ic_dialog_delete),
        WARNING(R.drawable.ic_dialog_warning),
        INFO(R.drawable.ic_dialog_check),
        LOCK(R.drawable.ic_dialog_lock);

        public final int iconResId;

        DialogType(int iconResId) {
            this.iconResId = iconResId;
        }
    }

    /**
     * Crear diálogo de confirmación genérico
     */
    public static CustomDialogFragment createConfirmDialog(String title, String message) {
        return newInstance(title, message, "Confirmar", "Cancelar")
                .setIcon(R.drawable.ic_dialog_check);
    }

    /**
     * Crear diálogo de eliminación
     */
    public static CustomDialogFragment createDeleteDialog(String title, String message) {
        return newInstance(title, message, "Eliminar", "Cancelar")
                .setIcon(R.drawable.ic_dialog_delete);
    }

    /**
     * Crear diálogo de advertencia
     */
    public static CustomDialogFragment createWarningDialog(String title, String message) {
        return newInstance(title, message, "Aceptar", "Cancelar")
                .setIcon(R.drawable.ic_dialog_warning);
    }

    /**
     * Crear diálogo de cierre de sesión
     */
    public static CustomDialogFragment createLogoutDialog(String title, String message) {
        return newInstance(title, message, "Sí, cerrar sesión", "Cancelar")
                .setIcon(R.drawable.ic_dialog_lock);
    }
}
