package com.yhonam.penguinui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

/**
 * LoadingDialogFragment - Diálogo de carga con spinner animado y gradiente
 *
 * Uso:
 * 1. Crear instancia con newInstance()
 * 2. Mostrar con show(getSupportFragmentManager(), "tag")
 * 3. Dismiss cuando la operación termine
 *
 * Ejemplo:
 * <pre>
 * {@code
 * LoadingDialogFragment loadingDialog = LoadingDialogFragment.newInstance("Cargando...");
 * loadingDialog.setCancelable(false);
 * loadingDialog.show(getSupportFragmentManager(), "loading_dialog");
 *
 * // Después de completar la operación
 * loadingDialog.dismiss();
 * }
 * </pre>
 */
public class LoadingDialogFragment extends DialogFragment {

    // Views
    private ProgressBar loadingSpinner;
    private ImageView loadingIcon;
    private TextView loadingMessage;
    private TextView loadingSubMessage;
    private View loadingAccentLine;
    private View loadingDialogContainer;

    // Parámetros
    private String message;
    private String subMessage;
    private boolean showIcon;
    private boolean showAccentLine;

    // Handler para actualizaciones UI
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Constructores requeridos
     */
    public LoadingDialogFragment() {
        // Requerido para DialogFragment
    }

    /**
     * Crear nueva instancia del diálogo de carga
     *
     * @param message Mensaje principal a mostrar
     * @return Instancia de LoadingDialogFragment
     */
    public static LoadingDialogFragment newInstance(String message) {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message != null ? message : "Cargando...");
        args.putString("subMessage", "");
        args.putBoolean("showIcon", true);
        args.putBoolean("showAccentLine", true);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Crear nueva instancia con mensaje secundario
     *
     * @param message Mensaje principal
     * @param subMessage Mensaje secundario (detalle adicional)
     * @return Instancia de LoadingDialogFragment
     */
    public static LoadingDialogFragment newInstance(String message, String subMessage) {
        LoadingDialogFragment fragment = newInstance(message);
        fragment.subMessage = subMessage;
        if (fragment.getArguments() != null) {
            fragment.getArguments().putString("subMessage", subMessage != null ? subMessage : "");
        }
        return fragment;
    }

    /**
     * Crear nueva instancia sin ícono
     *
     * @param message Mensaje principal
     * @param showIcon Mostrar u ocultar el ícono central
     * @return Instancia de LoadingDialogFragment
     */
    public static LoadingDialogFragment newInstance(String message, boolean showIcon) {
        LoadingDialogFragment fragment = newInstance(message);
        fragment.showIcon = showIcon;
        if (fragment.getArguments() != null) {
            fragment.getArguments().putBoolean("showIcon", showIcon);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener parámetros de los argumentos
        if (getArguments() != null) {
            message = getArguments().getString("message", "Cargando...");
            subMessage = getArguments().getString("subMessage", "");
            showIcon = getArguments().getBoolean("showIcon", true);
            showAccentLine = getArguments().getBoolean("showAccentLine", true);
        }

        // Configurar estilo
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_loading_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar vistas
        initViews(view);

        // Configurar contenido
        setupContent();
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
    public void onStart() {
        super.onStart();

        // Iniciar animación del spinner
        startSpinnerAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Detener animación para ahorrar recursos
        stopSpinnerAnimation();
    }

    /**
     * Inicializar referencias a las vistas
     */
    private void initViews(View view) {
        loadingSpinner = view.findViewById(R.id.loadingSpinner);
        loadingIcon = view.findViewById(R.id.loadingIcon);
        loadingMessage = view.findViewById(R.id.loadingMessage);
        loadingSubMessage = view.findViewById(R.id.loadingSubMessage);
        loadingAccentLine = view.findViewById(R.id.loadingAccentLine);
        loadingDialogContainer = view.findViewById(R.id.loadingDialogContainer);
    }

    /**
     * Configurar el contenido del diálogo (mensajes, visibilidad)
     */
    private void setupContent() {
        // Configurar mensaje principal
        if (message != null && !message.isEmpty()) {
            loadingMessage.setText(message);
        }

        // Configurar mensaje secundario
        if (subMessage != null && !subMessage.isEmpty()) {
            loadingSubMessage.setText(subMessage);
            loadingSubMessage.setVisibility(View.VISIBLE);
        } else {
            loadingSubMessage.setVisibility(View.GONE);
        }

        // Configurar visibilidad del ícono
        loadingIcon.setVisibility(showIcon ? View.VISIBLE : View.GONE);

        // Configurar visibilidad de la línea de acento
        loadingAccentLine.setVisibility(showAccentLine ? View.VISIBLE : View.GONE);
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
        int maxWidthPx = (int) (350 * metrics.density);
        int desiredWidthPx = (int) (screenWidth * 0.85);
        params.width = Math.min(desiredWidthPx, maxWidthPx);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);

        // Dim background
        window.setDimAmount(0.6f);
    }

    /**
     * Iniciar animación del spinner
     */
    private void startSpinnerAnimation() {
        if (loadingSpinner != null) {
            // El spinner ya tiene animación rotatoria en el drawable
            // Pero podemos agregar animación adicional si es necesario
        }
    }

    /**
     * Detener animación del spinner
     */
    private void stopSpinnerAnimation() {
        if (loadingSpinner != null) {
            loadingSpinner.clearAnimation();
        }
    }

    /**
     * Actualizar el mensaje de carga (desde el hilo principal)
     *
     * @param message Nuevo mensaje
     */
    public void updateMessage(String message) {
        if (message == null) return;

        if (Looper.myLooper() == Looper.getMainLooper()) {
            // Ya estamos en el hilo principal
            if (loadingMessage != null) {
                loadingMessage.setText(message);
            }
        } else {
            // Postear al hilo principal
            handler.post(() -> {
                if (loadingMessage != null) {
                    loadingMessage.setText(message);
                }
            });
        }
    }

    /**
     * Actualizar el mensaje secundario (desde el hilo principal)
     *
     * @param subMessage Nuevo mensaje secundario
     */
    public void updateSubMessage(String subMessage) {
        if (subMessage == null) return;

        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (loadingSubMessage != null) {
                loadingSubMessage.setText(subMessage);
                loadingSubMessage.setVisibility(View.VISIBLE);
            }
        } else {
            handler.post(() -> {
                if (loadingSubMessage != null) {
                    loadingSubMessage.setText(subMessage);
                    loadingSubMessage.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    /**
     * Actualizar ambos mensajes (desde el hilo principal)
     *
     * @param message Mensaje principal
     * @param subMessage Mensaje secundario
     */
    public void updateMessages(String message, String subMessage) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            updateMessage(message);
            updateSubMessage(subMessage);
        } else {
            handler.post(() -> updateMessages(message, subMessage));
        }
    }

    /**
     * Mostrar u ocultar el ícono
     *
     * @param show true para mostrar, false para ocultar
     */
    public void showIcon(boolean show) {
        if (loadingIcon != null) {
            loadingIcon.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Mostrar u ocultar la línea de acento
     *
     * @param show true para mostrar, false para ocultar
     */
    public void showAccentLine(boolean show) {
        if (loadingAccentLine != null) {
            loadingAccentLine.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Cambiar el color del spinner (útil para diferentes estados)
     *
     * @param colorResId Resource ID del color
     */
    public void setSpinnerColor(int colorResId) {
        if (loadingSpinner != null) {
            int color = ContextCompat.getColor(requireContext(), colorResId);
            loadingSpinner.getIndeterminateDrawable().setColorFilter(
                    color,
                    android.graphics.PorterDuff.Mode.SRC_IN
            );
        }
    }

    /**
     * Cambiar el ícono central
     *
     * @param iconResId Resource ID del ícono
     */
    public void setIcon(int iconResId) {
        if (loadingIcon != null) {
            loadingIcon.setImageResource(iconResId);
        }
    }

    /**
     * Ocultar el ícono completamente
     */
    public void hideIcon() {
        showIcon(false);
    }

    /**
     * Mostrar el ícono
     */
    public void showIcon() {
        showIcon(true);
    }
}
