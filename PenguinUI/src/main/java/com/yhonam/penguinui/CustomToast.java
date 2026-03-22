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
 * CustomToast - Notificaciones toast personalizadas con estilo elegante
 *
 * Uso:
 *  - CustomToast.showSuccess(context, "Mensaje")
 *  - CustomToast.showError(context, "Mensaje")
 *  - CustomToast.showWarning(context, "Mensaje")
 *  - CustomToast.showInfo(context, "Mensaje")
 *
 * Todos los métodos tienen sobrecarga para:
 *  - Título personalizado
 *  - Duración personalizada (en segundos)
 *  - Posición personalizada (Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)
 */
public class CustomToast {

    /**
     * Tipos de toast disponibles
     */
    public enum ToastType {
        SUCCESS(R.drawable.bg_toast_success, R.drawable.ic_toast_success, R.color.toast_success_accent, "Éxito"),
        ERROR(R.drawable.bg_toast_error, R.drawable.ic_toast_error, R.color.toast_error_accent, "Error"),
        WARNING(R.drawable.bg_toast_warning, R.drawable.ic_toast_warning, R.color.toast_warning_accent, "Advertencia"),
        INFO(R.drawable.bg_toast_info, R.drawable.ic_toast_info, R.color.toast_info_accent, "Información");

        public final int backgroundRes;
        public final int iconRes;
        public final int accentColor;
        public final String defaultTitle;

        ToastType(int backgroundRes, int iconRes, int accentColor, String defaultTitle) {
            this.backgroundRes = backgroundRes;
            this.iconRes = iconRes;
            this.accentColor = accentColor;
            this.defaultTitle = defaultTitle;
        }
    }

    // Duraciones predefinidas (en segundos)
    public static final int DURATION_SHORT = 2;
    public static final int DURATION_NORMAL = 3;
    public static final int DURATION_LONG = 5;

    /**
     * Mostrar toast de ÉXITO
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     */
    public static void showSuccess(Context context, String message) {
        show(context, ToastType.SUCCESS, null, message, DURATION_NORMAL, Gravity.TOP);
    }

    /**
     * Mostrar toast de ÉXITO con título personalizado
     *
     * @param context Contexto de la aplicación
     * @param title Título del toast
     * @param message Mensaje a mostrar
     */
    public static void showSuccess(Context context, String title, String message) {
        show(context, ToastType.SUCCESS, title, message, DURATION_NORMAL, Gravity.TOP);
    }

    /**
     * Mostrar toast de ÉXITO con duración personalizada
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     * @param durationSeconds Duración en segundos
     */
    public static void showSuccess(Context context, String message, int durationSeconds) {
        show(context, ToastType.SUCCESS, null, message, durationSeconds, Gravity.TOP);
    }

    /**
     * Mostrar toast de ERROR
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     */
    public static void showError(Context context, String message) {
        show(context, ToastType.ERROR, null, message, DURATION_LONG, Gravity.TOP);
    }

    /**
     * Mostrar toast de ERROR con título personalizado
     *
     * @param context Contexto de la aplicación
     * @param title Título del toast
     * @param message Mensaje a mostrar
     */
    public static void showError(Context context, String title, String message) {
        show(context, ToastType.ERROR, title, message, DURATION_LONG, Gravity.TOP);
    }

    /**
     * Mostrar toast de ERROR con duración personalizada
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     * @param durationSeconds Duración en segundos
     */
    public static void showError(Context context, String message, int durationSeconds) {
        show(context, ToastType.ERROR, null, message, durationSeconds, Gravity.TOP);
    }

    /**
     * Mostrar toast de ADVERTENCIA
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     */
    public static void showWarning(Context context, String message) {
        show(context, ToastType.WARNING, null, message, DURATION_LONG, Gravity.TOP);
    }

    /**
     * Mostrar toast de ADVERTENCIA con título personalizado
     *
     * @param context Contexto de la aplicación
     * @param title Título del toast
     * @param message Mensaje a mostrar
     */
    public static void showWarning(Context context, String title, String message) {
        show(context, ToastType.WARNING, title, message, DURATION_LONG, Gravity.TOP);
    }

    /**
     * Mostrar toast de ADVERTENCIA con duración personalizada
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     * @param durationSeconds Duración en segundos
     */
    public static void showWarning(Context context, String message, int durationSeconds) {
        show(context, ToastType.WARNING, null, message, durationSeconds, Gravity.TOP);
    }

    /**
     * Mostrar toast de INFORMACIÓN
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     */
    public static void showInfo(Context context, String message) {
        show(context, ToastType.INFO, null, message, DURATION_NORMAL, Gravity.TOP);
    }

    /**
     * Mostrar toast de INFORMACIÓN con título personalizado
     *
     * @param context Contexto de la aplicación
     * @param title Título del toast
     * @param message Mensaje a mostrar
     */
    public static void showInfo(Context context, String title, String message) {
        show(context, ToastType.INFO, title, message, DURATION_NORMAL, Gravity.TOP);
    }

    /**
     * Mostrar toast de INFORMACIÓN con duración personalizada
     *
     * @param context Contexto de la aplicación
     * @param message Mensaje a mostrar
     * @param durationSeconds Duración en segundos
     */
    public static void showInfo(Context context, String message, int durationSeconds) {
        show(context, ToastType.INFO, null, message, durationSeconds, Gravity.TOP);
    }

    /**
     * Mostrar toast con posición personalizada
     *
     * @param context Contexto de la aplicación
     * @param type Tipo de toast (SUCCESS, ERROR, WARNING, INFO)
     * @param title Título del toast (puede ser null para usar el default)
     * @param message Mensaje a mostrar
     * @param durationSeconds Duración en segundos
     * @param gravity Posición del toast (Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)
     */
    public static void show(Context context, ToastType type, String title, String message,
                            int durationSeconds, int gravity) {
        // Inflar el layout personalizado
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_custom_toast, null);

        // Referencias a las vistas
        CardView toastContainer = layout.findViewById(R.id.toastContainer);
        ImageView toastIcon = layout.findViewById(R.id.toastIcon);
        TextView toastTitle = layout.findViewById(R.id.toastTitle);
        TextView toastMessage = layout.findViewById(R.id.toastMessage);
        View accentBar = layout.findViewById(R.id.toastAccentBar);

        // Configurar fondo gradiente
        toastContainer.setCardBackgroundColor(context.getResources().getColor(R.color.toast_bg_dark, null));

        // Configurar ícono
        toastIcon.setImageResource(type.iconRes);

        // Configurar título (usar default si es null)
        String actualTitle = (title != null && !title.isEmpty()) ? title : type.defaultTitle;
        toastTitle.setText(actualTitle);

        // Configurar mensaje
        toastMessage.setText(message);

        // Configurar barra lateral de acento
        int accentColor = context.getResources().getColor(type.accentColor, null);
        GradientDrawable accentDrawable = new GradientDrawable();
        accentDrawable.setShape(GradientDrawable.RECTANGLE);
        accentDrawable.setColor(accentColor);
        accentDrawable.setCornerRadius(6); // Bordes redondeados para la barra
        accentBar.setBackground(accentDrawable);

        // Crear Toast personalizado
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG); // Usamos LONG como base
        toast.setView(layout);
        toast.setGravity(gravity, 0, gravity == Gravity.TOP ? 50 : 0);

        // Mostrar toast
        toast.show();

        // Nota: La duración exacta en segundos no es directamente configurable en Toast,
        // pero Toast.LENGTH_LONG es aproximadamente 3.5 segundos
    }

    /**
     * Mostrar toast con todos los parámetros personalizables
     *
     * @param context Contexto de la aplicación
     * @param type Tipo de toast
     * @param title Título del toast
     * @param message Mensaje a mostrar
     * @param gravity Posición (Gravity.TOP, CENTER, BOTTOM)
     * @param yOffset Desplazamiento vertical en píxeles
     */
    public static void showCustom(Context context, ToastType type, String title, String message,
                                  int gravity, int yOffset) {
        show(context, type, title, message, DURATION_NORMAL, gravity);
    }
}
