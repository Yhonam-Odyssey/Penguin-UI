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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

/**
 * PenguinDialog - Diálogo de confirmación con dos botones (Confirmar / Cancelar)
 *
 * Uso rápido con factory methods:
 *   PenguinDialog dialog = PenguinDialog.confirm("¿Eliminar?", "Esta acción no se puede deshacer.");
 *   dialog.setOnConfirmListener(() -> borrarRegistro());
 *   dialog.show(getSupportFragmentManager(), "confirm");
 *
 * Uso completo con newInstance:
 *   PenguinDialog.newInstance("Título", "Mensaje", "Sí", "No")
 *       .setIcon(R.drawable.ic_warning)
 *       .setOnConfirmListener(this::onConfirm)
 *       .show(getSupportFragmentManager(), "dialog");
 *
 * Tipos predefinidos (también disponibles via PenguinUI facade):
 *   PenguinDialog.confirm(title, msg)
 *   PenguinDialog.delete(title, msg)
 *   PenguinDialog.warning(title, msg)
 *   PenguinDialog.logout(title, msg)
 */
public class PenguinDialog extends DialogFragment {

    // ─── Interfaces ────────────────────────────────────────────────────────────

    public interface OnConfirmListener { void onConfirm(); }
    public interface OnCancelListener  { void onCancel();  }

    // ─── Tipos predefinidos ────────────────────────────────────────────────────

    public enum Type {
        CONFIRM(R.drawable.ic_dialog_check),
        DELETE(R.drawable.ic_dialog_delete),
        WARNING(R.drawable.ic_dialog_warning),
        INFO(R.drawable.ic_dialog_check),
        LOCK(R.drawable.ic_dialog_lock);

        public final int iconResId;
        Type(int iconResId) { this.iconResId = iconResId; }
    }

    // ─── Estado ────────────────────────────────────────────────────────────────

    private String            title;
    private String            message;
    private String            positiveText;
    private String            negativeText;
    private int               iconResId = -1;
    private boolean           showIcon  = true;
    private OnConfirmListener confirmListener;
    private OnCancelListener  cancelListener;

    // Views
    private ImageView dialogIcon;
    private TextView  dialogTitle;
    private TextView  dialogMessage;
    private Button    btnPositive;
    private Button    btnNegative;

    public PenguinDialog() {}

    // ─── Creación ──────────────────────────────────────────────────────────────

    public static PenguinDialog newInstance(String title, String message) {
        PenguinDialog f = new PenguinDialog();
        Bundle args = new Bundle();
        args.putString("title",   title);
        args.putString("message", message);
        f.setArguments(args);
        return f;
    }

    public static PenguinDialog newInstance(String title, String message,
                                            String positiveText, String negativeText) {
        PenguinDialog f = newInstance(title, message);
        f.positiveText = positiveText;
        f.negativeText = negativeText;
        return f;
    }

    // ─── Factory methods ───────────────────────────────────────────────────────

    /** Diálogo de confirmación genérico */
    public static PenguinDialog confirm(String title, String message) {
        return newInstance(title, message, "Confirmar", "Cancelar")
                .setIcon(Type.CONFIRM.iconResId);
    }

    /** Diálogo de eliminación */
    public static PenguinDialog delete(String title, String message) {
        return newInstance(title, message, "Eliminar", "Cancelar")
                .setIcon(Type.DELETE.iconResId);
    }

    /** Diálogo de advertencia */
    public static PenguinDialog warning(String title, String message) {
        return newInstance(title, message, "Aceptar", "Cancelar")
                .setIcon(Type.WARNING.iconResId);
    }

    /** Diálogo de cierre de sesión */
    public static PenguinDialog logout(String title, String message) {
        return newInstance(title, message, "Sí, cerrar sesión", "Cancelar")
                .setIcon(Type.LOCK.iconResId);
    }

    // ─── Configuración fluida ──────────────────────────────────────────────────

    public PenguinDialog setIcon(int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    public PenguinDialog showIcon(boolean show) {
        this.showIcon = show;
        return this;
    }

    public PenguinDialog setOnConfirmListener(OnConfirmListener listener) {
        this.confirmListener = listener;
        return this;
    }

    public PenguinDialog setOnCancelListener(OnCancelListener listener) {
        this.cancelListener = listener;
        return this;
    }

    // ─── Ciclo de vida ─────────────────────────────────────────────────────────

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title   = getArguments().getString("title",   "Confirmar");
            message = getArguments().getString("message", "");
        }
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
        dialogIcon    = view.findViewById(R.id.dialogIcon);
        dialogTitle   = view.findViewById(R.id.dialogTitle);
        dialogMessage = view.findViewById(R.id.dialogMessage);
        btnPositive   = view.findViewById(R.id.btnPositive);
        btnNegative   = view.findViewById(R.id.btnNegative);

        setupContent();
        setupButtons();
        applyTheme(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        setupWindow(dialog);
        return dialog;
    }

    // ─── Interno ───────────────────────────────────────────────────────────────

    private void setupContent() {
        if (title != null)   dialogTitle.setText(title);
        if (message != null) dialogMessage.setText(message);

        if (showIcon) {
            dialogIcon.setImageResource(iconResId != -1 ? iconResId : R.drawable.ic_dialog_check);
            dialogIcon.setVisibility(View.VISIBLE);
        } else {
            dialogIcon.setVisibility(View.GONE);
        }

        if (positiveText != null && !positiveText.isEmpty()) btnPositive.setText(positiveText);
        if (negativeText != null && !negativeText.isEmpty()) btnNegative.setText(negativeText);
    }

    private void setupButtons() {
        btnPositive.setOnClickListener(v -> {
            if (confirmListener != null) confirmListener.onConfirm();
            dismiss();
        });
        btnNegative.setOnClickListener(v -> {
            if (cancelListener != null) cancelListener.onCancel();
            dismiss();
        });
    }

    private void applyTheme(View root) {
        PenguinTheme theme = PenguinTheme.get();
        if (theme.getPreset() == PenguinTheme.Preset.NEO
                && !theme.hasBackgroundColor()
                && !theme.hasTextPrimaryColor()
                && !theme.hasTextSecondaryColor()) return; // NEO puro: sin cambios

        CardView container = root.findViewById(R.id.dialogContainer);
        float density = getResources().getDisplayMetrics().density;

        // Fondo
        int bgColor = ContextCompat.getColor(requireContext(), R.color.dialog_bg_secondary);
        if (theme.hasBackgroundColor()) bgColor = theme.getBackgroundColor();
        if (theme.isGlass()) {
            bgColor = PenguinTheme.applyAlpha(bgColor, theme.getBackgroundAlpha());
            container.setCardElevation(0);
        }
        container.setCardBackgroundColor(bgColor);

        // Radio de esquinas
        container.setRadius(theme.getCornerRadiusDp() * density);

        // Texto
        if (theme.hasTextPrimaryColor())   dialogTitle.setTextColor(theme.getTextPrimaryColor());
        if (theme.hasTextSecondaryColor()) dialogMessage.setTextColor(theme.getTextSecondaryColor());
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
