package com.yhonam.penguinui;

import android.app.Dialog;
import android.content.DialogInterface;
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
 * PenguinInfoDialog - Diálogo informativo con un único botón "Aceptar"
 *
 * Uso rápido con factory methods:
 *   PenguinInfoDialog.success("¡Listo!", "Tu perfil fue actualizado.")
 *       .show(getSupportFragmentManager(), "info");
 *
 *   PenguinInfoDialog.info("Información", "Versión 1.0.0")
 *       .setOnDismissListener(() -> finish())
 *       .show(getSupportFragmentManager(), "info");
 *
 * Tipos predefinidos:
 *   PenguinInfoDialog.success(title, msg)
 *   PenguinInfoDialog.info(title, msg)
 *   PenguinInfoDialog.warning(title, msg)
 */
public class PenguinInfoDialog extends DialogFragment {

    public interface OnDismissListener { void onDismiss(); }

    // ─── Estado ────────────────────────────────────────────────────────────────

    private String            title;
    private String            message;
    private String            okText;
    private int               iconResId = -1;
    private boolean           showIcon  = true;
    private OnDismissListener dismissListener;

    // Views
    private ImageView infoDialogIcon;
    private TextView  infoDialogTitle;
    private TextView  infoDialogMessage;
    private Button    btnOk;

    public PenguinInfoDialog() {}

    // ─── Creación ──────────────────────────────────────────────────────────────

    public static PenguinInfoDialog newInstance(String title, String message) {
        PenguinInfoDialog f = new PenguinInfoDialog();
        Bundle args = new Bundle();
        args.putString("title",   title != null ? title : "Información");
        args.putString("message", message);
        args.putString("okText",  "Aceptar");
        f.setArguments(args);
        return f;
    }

    public static PenguinInfoDialog newInstance(String title, String message, String okText) {
        PenguinInfoDialog f = newInstance(title, message);
        f.okText = okText;
        if (f.getArguments() != null) f.getArguments().putString("okText", okText);
        return f;
    }

    // ─── Factory methods ───────────────────────────────────────────────────────

    public static PenguinInfoDialog success(String title, String message) {
        return newInstance(title, message).setIcon(R.drawable.ic_toast_success);
    }

    public static PenguinInfoDialog info(String title, String message) {
        return newInstance(title, message).setIcon(R.drawable.ic_toast_info);
    }

    public static PenguinInfoDialog warning(String title, String message) {
        return newInstance(title, message).setIcon(R.drawable.ic_toast_warning);
    }

    // ─── Configuración fluida ──────────────────────────────────────────────────

    public PenguinInfoDialog setIcon(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    public PenguinInfoDialog showIcon(boolean show) {
        this.showIcon = show;
        return this;
    }

    public PenguinInfoDialog setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
        return this;
    }

    // ─── Ciclo de vida ─────────────────────────────────────────────────────────

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title   = getArguments().getString("title",   "Información");
            message = getArguments().getString("message", "");
            okText  = getArguments().getString("okText",  "Aceptar");
        }
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
        infoDialogIcon    = view.findViewById(R.id.infoDialogIcon);
        infoDialogTitle   = view.findViewById(R.id.infoDialogTitle);
        infoDialogMessage = view.findViewById(R.id.infoDialogMessage);
        btnOk             = view.findViewById(R.id.btnOk);

        setupContent();
        btnOk.setOnClickListener(v -> dismiss());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setupWindow(dialog);
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) dismissListener.onDismiss();
    }

    // ─── Actualización dinámica ────────────────────────────────────────────────

    public void updateMessage(String message) {
        if (infoDialogMessage != null && message != null) infoDialogMessage.setText(message);
    }

    public void updateTitle(String title) {
        if (infoDialogTitle != null && title != null) infoDialogTitle.setText(title);
    }

    public void updateIcon(int iconResId) {
        if (infoDialogIcon != null) {
            infoDialogIcon.setImageResource(iconResId);
            infoDialogIcon.setVisibility(View.VISIBLE);
        }
    }

    // ─── Interno ───────────────────────────────────────────────────────────────

    private void setupContent() {
        if (title != null)   infoDialogTitle.setText(title);
        if (message != null) infoDialogMessage.setText(message);

        if (showIcon) {
            infoDialogIcon.setImageResource(iconResId != -1 ? iconResId : R.drawable.ic_toast_info);
            infoDialogIcon.setVisibility(View.VISIBLE);
        } else {
            infoDialogIcon.setVisibility(View.GONE);
        }

        if (okText != null && !okText.isEmpty()) btnOk.setText(okText);
    }

    private void setupWindow(Dialog dialog) {
        if (dialog.getWindow() == null) return;
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int maxWidthPx     = (int) (400 * metrics.density);
        int desiredWidthPx = (int) (metrics.widthPixels * 0.85f);
        params.width  = Math.min(desiredWidthPx, maxWidthPx);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setDimAmount(0.5f);
    }
}
