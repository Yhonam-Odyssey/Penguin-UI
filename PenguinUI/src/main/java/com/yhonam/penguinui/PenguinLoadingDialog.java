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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

/**
 * PenguinLoadingDialog - Diálogo de carga con spinner animado
 *
 * No se puede cerrar con back por defecto (setCancelable(false)).
 * Actualizable desde cualquier hilo.
 *
 * Uso:
 *   PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance("Cargando datos...");
 *   loading.setCancelable(false);
 *   loading.show(getSupportFragmentManager(), "loading");
 *
 *   // Desde hilo de trabajo:
 *   loading.updateMessage("Procesando...");
 *   loading.updateSubMessage("Esto puede tardar unos segundos");
 *
 *   // Al terminar:
 *   loading.dismiss();
 */
public class PenguinLoadingDialog extends DialogFragment {

    // Views
    private ProgressBar loadingSpinner;
    private ImageView   loadingIcon;
    private TextView    loadingMessage;
    private TextView    loadingSubMessage;
    private View        loadingAccentLine;

    // Parámetros
    private String  message;
    private String  subMessage;
    private boolean showIcon;
    private boolean showAccentLine;

    private final Handler handler = new Handler(Looper.getMainLooper());

    public PenguinLoadingDialog() {}

    // ─── Creación ──────────────────────────────────────────────────────────────

    public static PenguinLoadingDialog newInstance(String message) {
        PenguinLoadingDialog f = new PenguinLoadingDialog();
        Bundle args = new Bundle();
        args.putString("message",       message != null ? message : "Cargando...");
        args.putString("subMessage",    "");
        args.putBoolean("showIcon",       true);
        args.putBoolean("showAccentLine", true);
        f.setArguments(args);
        return f;
    }

    public static PenguinLoadingDialog newInstance(String message, String subMessage) {
        PenguinLoadingDialog f = newInstance(message);
        if (f.getArguments() != null)
            f.getArguments().putString("subMessage", subMessage != null ? subMessage : "");
        return f;
    }

    public static PenguinLoadingDialog newInstance(String message, boolean showIcon) {
        PenguinLoadingDialog f = newInstance(message);
        if (f.getArguments() != null)
            f.getArguments().putBoolean("showIcon", showIcon);
        return f;
    }

    // ─── Ciclo de vida ─────────────────────────────────────────────────────────

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message       = getArguments().getString("message",       "Cargando...");
            subMessage    = getArguments().getString("subMessage",    "");
            showIcon      = getArguments().getBoolean("showIcon",       true);
            showAccentLine = getArguments().getBoolean("showAccentLine", true);
        }
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
        loadingSpinner    = view.findViewById(R.id.loadingSpinner);
        loadingIcon       = view.findViewById(R.id.loadingIcon);
        loadingMessage    = view.findViewById(R.id.loadingMessage);
        loadingSubMessage = view.findViewById(R.id.loadingSubMessage);
        loadingAccentLine = view.findViewById(R.id.loadingAccentLine);
        setupContent();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setupWindow(dialog);
        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loadingSpinner != null) loadingSpinner.clearAnimation();
    }

    // ─── Actualización dinámica (thread-safe) ──────────────────────────────────

    public void updateMessage(String message) {
        if (message == null) return;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (loadingMessage != null) loadingMessage.setText(message);
        } else {
            handler.post(() -> { if (loadingMessage != null) loadingMessage.setText(message); });
        }
    }

    public void updateSubMessage(String subMessage) {
        if (subMessage == null) return;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (loadingSubMessage != null) {
                loadingSubMessage.setText(subMessage);
                loadingSubMessage.setVisibility(View.VISIBLE);
            }
        } else {
            handler.post(() -> updateSubMessage(subMessage));
        }
    }

    public void updateMessages(String message, String subMessage) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            updateMessage(message);
            updateSubMessage(subMessage);
        } else {
            handler.post(() -> updateMessages(message, subMessage));
        }
    }

    public void showIcon(boolean show) {
        if (loadingIcon != null) loadingIcon.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showAccentLine(boolean show) {
        if (loadingAccentLine != null) loadingAccentLine.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setSpinnerColor(int colorResId) {
        if (loadingSpinner != null) {
            int color = ContextCompat.getColor(requireContext(), colorResId);
            loadingSpinner.getIndeterminateDrawable()
                    .setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public void setIcon(int iconResId) {
        if (loadingIcon != null) loadingIcon.setImageResource(iconResId);
    }

    // ─── Interno ───────────────────────────────────────────────────────────────

    private void setupContent() {
        if (message != null && !message.isEmpty()) loadingMessage.setText(message);

        if (subMessage != null && !subMessage.isEmpty()) {
            loadingSubMessage.setText(subMessage);
            loadingSubMessage.setVisibility(View.VISIBLE);
        } else {
            loadingSubMessage.setVisibility(View.GONE);
        }

        loadingIcon.setVisibility(showIcon ? View.VISIBLE : View.GONE);
        loadingAccentLine.setVisibility(showAccentLine ? View.VISIBLE : View.GONE);
    }

    private void setupWindow(Dialog dialog) {
        if (dialog.getWindow() == null) return;
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int maxWidthPx     = (int) (350 * metrics.density);
        int desiredWidthPx = (int) (metrics.widthPixels * 0.85f);
        params.width  = Math.min(desiredWidthPx, maxWidthPx);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setDimAmount(0.6f);
    }
}
