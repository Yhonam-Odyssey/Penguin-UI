package com.yhonam.penguinui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
 *
 * Con título personalizado:
 *   PenguinToast.showSuccess(context, "¡Listo!", "Tu perfil fue actualizado");
 *
 * Con animación personalizada:
 *   PenguinToast.show(context, PenguinToast.Type.SUCCESS, null, "msg",
 *                     PenguinToast.DURATION_NORMAL, Gravity.TOP,
 *                     PenguinToast.AnimationType.FADE);
 *
 * Cambiar animación por defecto globalmente:
 *   PenguinToast.setDefaultAnimation(PenguinToast.AnimationType.SLIDE_VERTICAL);
 */
public class PenguinToast {

    // ─── Tipos de toast ────────────────────────────────────────────────────────

    public enum Type {
        SUCCESS(R.drawable.bg_toast_success, R.drawable.ic_toast_success, R.color.toast_success_accent, "Éxito"),
        ERROR  (R.drawable.bg_toast_error,   R.drawable.ic_toast_error,   R.color.toast_error_accent,   "Error"),
        WARNING(R.drawable.bg_toast_warning, R.drawable.ic_toast_warning, R.color.toast_warning_accent, "Advertencia"),
        INFO   (R.drawable.bg_toast_info,    R.drawable.ic_toast_info,    R.color.toast_info_accent,    "Información");

        public final int    backgroundRes;
        public final int    iconRes;
        public final int    accentColor;
        public final String defaultTitle;

        Type(int backgroundRes, int iconRes, int accentColor, String defaultTitle) {
            this.backgroundRes = backgroundRes;
            this.iconRes       = iconRes;
            this.accentColor   = accentColor;
            this.defaultTitle  = defaultTitle;
        }
    }

    // ─── Tipos de animación ────────────────────────────────────────────────────

    /**
     * Estilos de animación disponibles para el toast.
     *
     *  SLIDE_HORIZONTAL — entra desde la izquierda, sale hacia la derecha (default)
     *  SLIDE_VERTICAL   — entra desde arriba, sale hacia arriba
     *  FADE             — aparece y desaparece con fundido
     */
    public enum AnimationType {
        SLIDE_HORIZONTAL,
        SLIDE_VERTICAL,
        FADE
    }

    // ─── Constantes ────────────────────────────────────────────────────────────

    public static final int DURATION_SHORT  = 2;
    public static final int DURATION_NORMAL = 3;
    public static final int DURATION_LONG   = 5;

    private static AnimationType defaultAnimationType = AnimationType.SLIDE_HORIZONTAL;

    /**
     * Cambia el tipo de animación por defecto para todos los toasts futuros.
     * Por defecto: SLIDE_HORIZONTAL
     */
    public static void setDefaultAnimation(AnimationType type) {
        defaultAnimationType = (type != null) ? type : AnimationType.SLIDE_HORIZONTAL;
    }

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
     * Mostrar toast con control total de parámetros.
     * Usa la animación por defecto configurada con setDefaultAnimation().
     */
    public static void show(Context context, Type type, String title, String message,
                            int durationSeconds, int gravity) {
        show(context, type, title, message, durationSeconds, gravity, defaultAnimationType);
    }

    /**
     * Mostrar toast con tipo de animación específico.
     */
    public static void show(Context context, Type type, String title, String message,
                            int durationSeconds, int gravity, AnimationType animType) {
        if (context == null || type == null || message == null) return;
        View layout = buildLayout(context, type, title, message);

        Activity activity = getActivity(context);
        if (activity != null && !activity.isFinishing()) {
            showOverActivity(activity, layout, durationSeconds, gravity, 0, animType);
        } else {
            showFallbackToast(context, layout, durationSeconds, gravity);
        }
    }

    /**
     * Mostrar toast con posición y offset personalizados.
     */
    public static void showCustom(Context context, Type type, String title, String message,
                                  int gravity, int yOffset) {
        showCustom(context, type, title, message, gravity, yOffset, defaultAnimationType);
    }

    /**
     * Mostrar toast con posición, offset y animación personalizados.
     */
    public static void showCustom(Context context, Type type, String title, String message,
                                  int gravity, int yOffset, AnimationType animType) {
        if (context == null || type == null || message == null) return;
        View layout = buildLayout(context, type, title, message);

        Activity activity = getActivity(context);
        if (activity != null && !activity.isFinishing()) {
            showOverActivity(activity, layout, DURATION_LONG, gravity, yOffset, animType);
        } else {
            showFallbackToast(context, layout, DURATION_LONG, gravity);
        }
    }

    // ─── Motor de renderizado ──────────────────────────────────────────────────

    /**
     * Agrega el toast al rootView de la Activity y controla las animaciones
     * de entrada y salida directamente — sin depender del sistema Toast.
     */
    private static void showOverActivity(Activity activity, View layout,
                                         int durationSeconds, int gravity, int extraOffsetPx,
                                         AnimationType animType) {
        FrameLayout rootView = (FrameLayout)
                activity.getWindow().getDecorView().getRootView();

        float density = activity.getResources().getDisplayMetrics().density;
        int   margin  = (int) (16 * density);

        // Compensar la barra de estado para que el toast no quede debajo
        int statusBarHeight = 0;
        int sbResId = activity.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (sbResId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(sbResId);
        }

        boolean isBottom = (gravity == Gravity.BOTTOM);
        int layoutGravity = isBottom
                ? (Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                : (Gravity.TOP    | Gravity.CENTER_HORIZONTAL);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                layoutGravity
        );
        lp.leftMargin  = margin;
        lp.rightMargin = margin;

        if (isBottom) {
            lp.bottomMargin = margin + (extraOffsetPx > 0 ? extraOffsetPx : 0);
        } else {
            lp.topMargin = statusBarHeight + margin + (extraOffsetPx > 0 ? extraOffsetPx : 0);
        }

        rootView.addView(layout, lp);

        // --- Animación de entrada ---
        Animation enterAnim = AnimationUtils.loadAnimation(activity, getEnterAnimRes(animType));
        layout.startAnimation(enterAnim);

        // --- Programar animación de salida ---
        long holdMs = durationSeconds <= 2 ? 2000L : 3500L;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (layout.getParent() == null) return;

            Animation exitAnim = AnimationUtils.loadAnimation(activity, getExitAnimRes(animType));
            exitAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation a) {}
                @Override public void onAnimationRepeat(Animation a) {}
                @Override public void onAnimationEnd(Animation a) {
                    if (layout.getParent() != null) rootView.removeView(layout);
                }
            });
            layout.startAnimation(exitAnim);
        }, holdMs);
    }

    /** Fallback: usa el sistema Toast nativo cuando no hay Activity disponible */
    private static void showFallbackToast(Context context, View layout,
                                           int durationSeconds, int gravity) {
        Toast toast = new Toast(context);
        toast.setDuration(durationSeconds <= 2 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(gravity, 0, gravity == Gravity.TOP ? 50 : 0);
        toast.show();
    }

    // ─── Recursos de animación ─────────────────────────────────────────────────

    private static int getEnterAnimRes(AnimationType type) {
        if (type == null) return R.anim.toast_enter;
        switch (type) {
            case SLIDE_VERTICAL: return R.anim.toast_slide_v_enter;
            case FADE:           return R.anim.toast_fade_enter;
            default:             return R.anim.toast_enter;
        }
    }

    private static int getExitAnimRes(AnimationType type) {
        if (type == null) return R.anim.toast_exit;
        switch (type) {
            case SLIDE_VERTICAL: return R.anim.toast_slide_v_exit;
            case FADE:           return R.anim.toast_fade_exit;
            default:             return R.anim.toast_exit;
        }
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    /** Extrae la Activity del Context, recorriendo ContextWrappers si es necesario. */
    static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) return (Activity) context;
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    // ─── Construcción del layout ───────────────────────────────────────────────

    private static View buildLayout(Context context, Type type, String title, String message) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null);

        CardView  toastContainer = layout.findViewById(R.id.toastContainer);
        ImageView toastIcon      = layout.findViewById(R.id.toastIcon);
        TextView  toastTitle     = layout.findViewById(R.id.toastTitle);
        TextView  toastMessage   = layout.findViewById(R.id.toastMessage);
        View      accentBar      = layout.findViewById(R.id.toastAccentBar);

        toastIcon.setImageResource(type.iconRes);
        String actualTitle = (title != null && !title.isEmpty()) ? title : type.defaultTitle;
        toastTitle.setText(actualTitle);
        toastMessage.setText(message);

        applyTheme(context, type, toastContainer, toastTitle, toastMessage, accentBar);
        return layout;
    }

    private static void applyTheme(Context context, Type type, CardView container,
                                   TextView titleView, TextView messageView, View accentBar) {
        PenguinTheme theme   = PenguinTheme.get();
        float        density = context.getResources().getDisplayMetrics().density;

        // Fondo del card
        int bgColor = context.getResources().getColor(R.color.toast_bg_dark, null);
        if (theme.hasBackgroundColor()) bgColor = theme.getBackgroundColor();
        if (theme.isGlass()) {
            bgColor = PenguinTheme.applyAlpha(bgColor, theme.getBackgroundAlpha());
            container.setCardElevation(0);
        }
        container.setCardBackgroundColor(bgColor);
        container.setRadius(theme.getCornerRadiusDp() * density);

        // Barra de acento lateral
        int accentColorInt = theme.hasAccentColor()
                ? theme.getAccentColor()
                : context.getResources().getColor(type.accentColor, null);
        GradientDrawable accentDrawable = new GradientDrawable();
        accentDrawable.setShape(GradientDrawable.RECTANGLE);
        accentDrawable.setColor(accentColorInt);
        accentDrawable.setCornerRadius(4 * density);
        accentBar.setBackground(accentDrawable);

        // Colores de texto
        if (theme.hasTextPrimaryColor())   titleView.setTextColor(theme.getTextPrimaryColor());
        if (theme.hasTextSecondaryColor()) messageView.setTextColor(theme.getTextSecondaryColor());
    }
}
