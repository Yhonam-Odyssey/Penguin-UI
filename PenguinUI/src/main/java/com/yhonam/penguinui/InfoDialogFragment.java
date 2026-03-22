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
 * InfoDialogFragment - Diálogo de información con único botón "Aceptar"
 *
 * Uso:
 * 1. Crear instancia con newInstance()
 * 2. Configurar listeners con setOnDismissListener()
 * 3. Mostrar con show(getSupportFragmentManager(), "tag")
 *
 * Ejemplo:
 * <pre>
 * {@code
 * InfoDialogFragment infoDialog = InfoDialogFragment.newInstance(
 *     "Información",
 *     "Los catálogos fueron actualizados correctamente."
 * );
 * infoDialog.show(getSupportFragmentManager(), "info_dialog");
 * }
 * </pre>
 */
public class InfoDialogFragment extends DialogFragment {

    // Callback opcional
    public interface OnDismissListener {
        void onDismiss();
    }

    // Parámetros del diálogo
    private String title;
    private String message;
    private String okButtonText;
    private int iconResId = -1;
    private boolean showIcon = true;

    // Views
    private ImageView infoDialogIcon;
    private TextView infoDialogTitle;
    private TextView infoDialogMessage;
    private Button btnOk;
    private View infoHeaderContainer;
    private View infoButtonContainer;

    // Listener
    private OnDismissListener dismissListener;

    /**
     * Constructores requeridos
     */
    public InfoDialogFragment() {
        // Requerido para DialogFragment
    }

    /**
     * Crear nueva instancia del diálogo de información
     *
     * @param title Título del diálogo
     * @param message Mensaje del diálogo
     * @return Instancia de InfoDialogFragment
     */
    public static InfoDialogFragment newInstance(String title, String message) {
        InfoDialogFragment fragment = new InfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title != null ? title : "Información");
        args.putString("message", message);
        args.putString("okButtonText", "Aceptar");
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Crear nueva instancia con texto personalizado para el botón OK
     *
     * @param title Título del diálogo
     * @param message Mensaje del diálogo
     * @param okButtonText Texto del botón OK
     * @return Instancia de InfoDialogFragment
     */
    public static InfoDialogFragment newInstance(String title, String message, String okButtonText) {
        InfoDialogFragment fragment = newInstance(title, message);
        fragment.okButtonText = okButtonText;
        if (fragment.getArguments() != null) {
            fragment.getArguments().putString("okButtonText", okButtonText);
        }
        return fragment;
    }

    /**
     * Configurar listener para cuando se dismiss el diálogo
     */
    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }

    /**
     * Configurar ícono personalizado
     *
     * @param iconResId Resource ID del ícono
     * @return this (para método fluido)
     */
    public InfoDialogFragment setIcon(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    /**
     * Mostrar u ocultar el ícono
     *
     * @param show true para mostrar, false para ocultar
     * @return this (para método fluido)
     */
    public InfoDialogFragment showIcon(boolean show) {
        this.showIcon = show;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener parámetros de los argumentos
        if (getArguments() != null) {
            title = getArguments().getString("title", "Información");
            message = getArguments().getString("message", "");
            okButtonText = getArguments().getString("okButtonText", "Aceptar");
        }

        // Configurar estilo
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_info_dialog, container, false);
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

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);

        // Llamar al listener si está configurado
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }

    /**
     * Inicializar referencias a las vistas
     */
    private void initViews(View view) {
        infoDialogIcon = view.findViewById(R.id.infoDialogIcon);
        infoDialogTitle = view.findViewById(R.id.infoDialogTitle);
        infoDialogMessage = view.findViewById(R.id.infoDialogMessage);
        btnOk = view.findViewById(R.id.btnOk);
        infoHeaderContainer = view.findViewById(R.id.infoHeaderContainer);
        infoButtonContainer = view.findViewById(R.id.infoButtonContainer);
    }

    /**
     * Configurar el contenido del diálogo (título, mensaje, ícono)
     */
    private void setupContent() {
        // Configurar título
        if (title != null && !title.isEmpty()) {
            infoDialogTitle.setText(title);
        }

        // Configurar mensaje
        if (message != null && !message.isEmpty()) {
            infoDialogMessage.setText(message);
        }

        // Configurar ícono
        if (showIcon) {
            if (iconResId != -1) {
                infoDialogIcon.setImageResource(iconResId);
                infoDialogIcon.setVisibility(View.VISIBLE);
            } else {
                // Ícono por defecto (información - cyan)
                infoDialogIcon.setImageResource(R.drawable.ic_toast_info);
                infoDialogIcon.setVisibility(View.VISIBLE);
            }
        } else {
            infoDialogIcon.setVisibility(View.GONE);
        }

        // Configurar texto del botón
        if (okButtonText != null && !okButtonText.isEmpty()) {
            btnOk.setText(okButtonText);
        }
    }

    /**
     * Configurar listeners y comportamientos de los botones
     */
    private void setupButtons() {
        // Botón OK
        btnOk.setOnClickListener(v -> dismiss());
    }

    /**
     * Configurar la ventana del diálogo para apariencia personalizada
     */
    private void setupDialogWindow(Dialog dialog) {
        if (dialog.getWindow() == null) return;

        Window window = dialog.getWindow();

        // Fondo transparente
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Animaciones
        window.setWindowAnimations(R.style.DialogAnimation);

        // Configurar dimensiones
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int maxWidthPx = (int) (400 * metrics.density);
        int desiredWidthPx = (int) (screenWidth * 0.85);
        params.width = Math.min(desiredWidthPx, maxWidthPx);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);

        // Dim background
        window.setDimAmount(0.5f);
    }

    /**
     * Factory methods para tipos comunes de diálogos de información
     */

    /**
     * Crear diálogo de éxito
     */
    public static InfoDialogFragment createSuccessDialog(String title, String message) {
        InfoDialogFragment dialog = newInstance(title, message);
        dialog.setIcon(R.drawable.ic_toast_success);
        return dialog;
    }

    /**
     * Crear diálogo de información general
     */
    public static InfoDialogFragment createInfoDialog(String title, String message) {
        InfoDialogFragment dialog = newInstance(title, message);
        dialog.setIcon(R.drawable.ic_toast_info);
        return dialog;
    }

    /**
     * Crear diálogo de advertencia informativa
     */
    public static InfoDialogFragment createWarningDialog(String title, String message) {
        InfoDialogFragment dialog = newInstance(title, message);
        dialog.setIcon(R.drawable.ic_toast_warning);
        return dialog;
    }

    /**
     * Actualizar el mensaje dinámicamente
     *
     * @param message Nuevo mensaje
     */
    public void updateMessage(String message) {
        if (message != null && infoDialogMessage != null) {
            infoDialogMessage.setText(message);
        }
    }

    /**
     * Actualizar el título dinámicamente
     *
     * @param title Nuevo título
     */
    public void updateTitle(String title) {
        if (title != null && infoDialogTitle != null) {
            infoDialogTitle.setText(title);
        }
    }

    /**
     * Cambiar el ícono dinámicamente
     *
     * @param iconResId Resource ID del nuevo ícono
     */
    public void updateIcon(int iconResId) {
        if (infoDialogIcon != null) {
            infoDialogIcon.setImageResource(iconResId);
            infoDialogIcon.setVisibility(View.VISIBLE);
        }
    }
}
