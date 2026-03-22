package com.yhonam.penguinui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

/**
 * PenguinToast - Notificaciones toast personalizadas con estilo elegante
 *
 * Uso básico:
 *   PenguinToast.showSuccess(context, "Guardado correctamente");
 *   PenguinToast.showError(context, "Ocurrió un error");
 *   PenguinToast.showWarning(context, "Advertencia");
 *   PenguinToast.showInfo(context, "Información");
 *
 * Con título personalizado:
 *   PenguinToast.showSuccess(context, "¡Listo!", "Tu perfil fue actualizado");
 *
 * Con posición personalizada:
 *   PenguinToast.show(context, PenguinToast.Type.SUCCESS, null, "msg",
 *                     PenguinToast.DURATION_NORMAL, Gravity.BOTTOM);
 */
public class PenguinToast {

    /**
     * Tipos de toast disponibles
     */
    public enum Type {
        SUCCESS(R.drawable.bg_toast_success, R.drawable.ic_toast_success, R.color.toast_success_accent, "Éxito"),
        ERROR(R.drawable.bg_toast_error, R.drawable.ic_toast_error, R.color.toast_error_accent, "Error"),
        WARNING(R.drawable.bg_toast_warning, R.drawable.ic_toast_warning, R.color.toast_warning_accent, "Advertencia"),
        INFO(R.drawable.bg_toast_info, R.drawable.ic_toast_info, R.color.toast_info_accent, "Información");

        public final int backgroundRes;
        public final int iconRes;
        public final int accentColor;
        public final String defaultTitle;

        Type(int backgroundRes, int iconRes, int accentColor, String defaultTitle) {
            this.backgroundRes = backgroundRes;
            this.iconRes = iconRes;
            this.accentColor = accentColor;
            this.defaultTitle = defaultTitle;
        }
    }

    // Duraciones predefinidas (en segundos)
    public static final int DURATION_SHORT  = 2;
    public static final int DURATION_NORMAL = 3;
    public static final int DURATION_LONG   = 5;

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    public static void showSuccess(Context context, String message) {
        show(context, Type.SUCCESS, null, message, DURATION_NORMAL, Gravity.TOP);
    }

    public static void showSuccess(Context context, String title, String message) {
        show(context, Type.SUCCESS, title, message, DURATION_NORMAL, Gravity.TOP);
    }

    public static void showSuccess(Context context, String message, int durationSeconds) {
        show(context, Type.SUCCESS, null, message, durationSeconds, Gravity.TOP);
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    public static void showError(Context context, String message) {
        show(context, Type.ERROR, null, message, DURATION_LONG, Gravity.TOP);
    }

    public static void showError(Context context, String title, String message) {
        show(context, Type.ERROR, title, message, DURATION_LONG, Gravity.TOP);
    }

    public static void showError(Context context, String message, int durationSeconds) {
        show(context, Type.ERROR, null, message, durationSeconds, Gravity.TOP);
    }

    // ─── WARNING ───────────────────────────────────────────────────────────────

    public static void showWarning(Context context, String message) {
        show(context, Type.WARNING, null, message, DURATION_LONG, Gravity.TOP);
    }

    public static void showWarning(Context context, String title, String message) {
        show(context, Type.WARNING, title, message, DURATION_LONG, Gravity.TOP);
    }

    public static void showWarning(Context context, String message, int durationSeconds) {
        show(context, Type.WARNING, null, message, durationSeconds, Gravity.TOP);
    }

    // ─── INFO ──────────────────────────────────────────────────────────────────

    public static void showInfo(Context context, String message) {
        show(context, Type.INFO, null, message, DURATION_NORMAL, Gravity.TOP);
    }

    public static void showInfo(Context context, String title, String message) {
        show(context, Type.INFO, title, message, DURATION_NORMAL, Gravity.TOP);
    }

    public static void showInfo(Context context, String message, int durationSeconds) {
        show(context, Type.INFO, null, message, durationSeconds, Gravity.TOP);
    }

    // ─── MÉTODO PRINCIPAL ──────────────────────────────────────────────────────

    /**
     * Mostrar toast con control total de parámetros
     *
     * @param context         Contexto de la aplicación
     * @param type            Tipo de toast (SUCCESS, ERROR, WARNING, INFO)
     * @param title           Título (null para usar el default del tipo)
     * @param message         Mensaje a mostrar
     * @param durationSeconds Duración en segundos
     * @param gravity         Posición (Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)
     */
    public static void show(Context context, Type type, String title, String message,
                            int durationSeconds, int gravity) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_custom_toast, null);

        CardView toastContainer = layout.findViewById(R.id.toastContainer);
        ImageView toastIcon     = layout.findViewById(R.id.toastIcon);
        TextView toastTitle     = layout.findViewById(R.id.toastTitle);
        TextView toastMessage   = layout.findViewById(R.id.toastMessage);
        View accentBar          = layout.findViewById(R.id.toastAccentBar);

        toastContainer.setCardBackgroundColor(
                context.getResources().getColor(R.color.toast_bg_dark, null));

        toastIcon.setImageResource(type.iconRes);

        String actualTitle = (title != null && !title.isEmpty()) ? title : type.defaultTitle;
        toastTitle.setText(actualTitle);
        toastMessage.setText(message);

        int accentColor = context.getResources().getColor(type.accentColor, null);
        GradientDrawable accentDrawable = new GradientDrawable();
        accentDrawable.setShape(GradientDrawable.RECTANGLE);
        accentDrawable.setColor(accentColor);
        accentDrawable.setCornerRadius(6);
        accentBar.setBackground(accentDrawable);

        Toast toast = new Toast(context);
        toast.setDuration(durationSeconds <= 2 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(gravity, 0, gravity == Gravity.TOP ? 50 : 0);
        toast.show();
    }

    /**
     * Mostrar toast con posición y offset personalizados
     */
    public static void showCustom(Context context, Type type, String title, String message,
                                  int gravity, int yOffset) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_custom_toast, null);

        CardView toastContainer = layout.findViewById(R.id.toastContainer);
        ImageView toastIcon     = layout.findViewById(R.id.toastIcon);
        TextView toastTitle     = layout.findViewById(R.id.toastTitle);
        TextView toastMessage   = layout.findViewById(R.id.toastMessage);
        View accentBar          = layout.findViewById(R.id.toastAccentBar);

        toastContainer.setCardBackgroundColor(
                context.getResources().getColor(R.color.toast_bg_dark, null));

        toastIcon.setImageResource(type.iconRes);
        String actualTitle = (title != null && !title.isEmpty()) ? title : type.defaultTitle;
        toastTitle.setText(actualTitle);
        toastMessage.setText(message);

        int accentColor = context.getResources().getColor(type.accentColor, null);
        GradientDrawable accentDrawable = new GradientDrawable();
        accentDrawable.setShape(GradientDrawable.RECTANGLE);
        accentDrawable.setColor(accentColor);
        accentDrawable.setCornerRadius(6);
        accentBar.setBackground(accentDrawable);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(gravity, 0, yOffset);
        toast.show();
    }
}
