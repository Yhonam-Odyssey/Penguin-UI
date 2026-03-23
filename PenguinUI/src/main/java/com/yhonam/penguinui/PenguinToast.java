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
 * PenguinToast — Notificaciones toast personalizadas con estilo elegante.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 *  USO RÁPIDO (shortcuts, animación por defecto)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *   PenguinToast.showSuccess(context, "Guardado correctamente");
 *   PenguinToast.showError(context, "Ocurrió un error");
 *   PenguinToast.showWarning(context, "Advertencia");
 *   PenguinToast.showInfo(context, "Información");
 *
 *   // Con título personalizado
 *   PenguinToast.showSuccess(context, "¡Listo!", "Tu perfil fue actualizado");
 *
 * ═══════════════════════════════════════════════════════════════════════════
 *  USO CON BUILDER (animación y opciones personalizadas)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *   PenguinToast.success(context, "Guardado")
 *       .animation(PenguinToast.Anim.BOUNCE)
 *       .show();
 *
 *   PenguinToast.error(context, "Error", "No se pudo conectar")
 *       .animation(PenguinToast.Anim.FADE)
 *       .duration(PenguinToast.DURATION_LONG)
 *       .show();
 *
 *   PenguinToast.make(context, PenguinToast.Type.INFO, null, "Mensaje")
 *       .animation(PenguinToast.Anim.SLIDE_BOTTOM)
 *       .gravity(Gravity.BOTTOM)
 *       .show();
 *
 * ═══════════════════════════════════════════════════════════════════════════
 *  ANIMACIÓN POR DEFECTO GLOBAL
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *   PenguinToast.setDefaultAnimation(PenguinToast.Anim.FADE);
 *   // A partir de aquí todos los showX() usarán FADE
 */
public class PenguinToast {

    // ─── Tipo de toast ─────────────────────────────────────────────────────────

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

    // ─── Animaciones disponibles ───────────────────────────────────────────────

    /**
     * Tipos de animación para el toast.
     * Se usa en el builder: PenguinToast.success(ctx, "msg").animation(Anim.BOUNCE).show()
     * O como global: PenguinToast.setDefaultAnimation(Anim.FADE)
     */
    public enum Anim {

        // ── Deslizamiento horizontal ────────────────────────────────────────────
        /** Entra desde la izquierda · Sale hacia la derecha  (predeterminado) */
        SLIDE_LEFT_RIGHT,
        /** Entra desde la derecha · Sale hacia la izquierda */
        SLIDE_RIGHT_LEFT,

        // ── Deslizamiento vertical ──────────────────────────────────────────────
        /** Entra desde arriba · Sale hacia arriba */
        SLIDE_TOP,
        /** Entra desde abajo · Sale hacia abajo */
        SLIDE_BOTTOM,

        // ── Fundido ─────────────────────────────────────────────────────────────
        /** Aparece y desaparece con fundido suave */
        FADE,

        // ── Escala ──────────────────────────────────────────────────────────────
        /** Aparece creciendo desde el centro · Sale encogiéndose */
        POP,

        // ── Rebote ──────────────────────────────────────────────────────────────
        /** Entra desde la izquierda con efecto rebote · Sale hacia la derecha */
        BOUNCE
    }

    // ─── Duraciones predefinidas ────────────────────────────────────────────────

    public static final int DURATION_SHORT  = 2;
    public static final int DURATION_NORMAL = 3;
    public static final int DURATION_LONG   = 5;

    // ─── Animación global por defecto ──────────────────────────────────────────

    private static Anim globalDefaultAnim = Anim.SLIDE_LEFT_RIGHT;

    /**
     * Cambia la animación por defecto para todos los toasts futuros.
     * Los shortcuts showSuccess/showError/etc. la usarán automáticamente.
     * Por defecto: {@link Anim#SLIDE_LEFT_RIGHT}
     */
    public static void setDefaultAnimation(Anim anim) {
        globalDefaultAnim = (anim != null) ? anim : Anim.SLIDE_LEFT_RIGHT;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  BUILDER — estado de instancia
    // ═══════════════════════════════════════════════════════════════════════════

    private final Context context;
    private final Type    type;
    private final String  title;
    private final String  message;
    private       int     durationSeconds;
    private       int     gravity;
    private       Anim    anim;

    private PenguinToast(Context context, Type type, String title, String message,
                          int duration, int gravity) {
        this.context         = context;
        this.type            = type;
        this.title           = title;
        this.message         = message;
        this.durationSeconds = duration;
        this.gravity         = gravity;
        this.anim            = globalDefaultAnim;
    }

    // ─── Opciones del builder ─────────────────────────────────────────────────

    /** Selecciona la animación de entrada/salida del toast. */
    public PenguinToast animation(Anim anim) {
        this.anim = (anim != null) ? anim : globalDefaultAnim;
        return this;
    }

    /** Duración en segundos (usa las constantes DURATION_SHORT/NORMAL/LONG). */
    public PenguinToast duration(int seconds) {
        this.durationSeconds = seconds;
        return this;
    }

    /** Posición en pantalla: Gravity.TOP (default) o Gravity.BOTTOM. */
    public PenguinToast gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /** Muestra el toast con las opciones configuradas. */
    public void show() {
        showInternal(context, type, title, message, durationSeconds, gravity, anim);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FACTORY — puntos de entrada del builder
    // ═══════════════════════════════════════════════════════════════════════════

    /** Builder para toast de éxito. Termina con .show() */
    public static PenguinToast success(Context context, String message) {
        return new PenguinToast(context, Type.SUCCESS, null, message, DURATION_NORMAL, Gravity.TOP);
    }

    /** Builder para toast de éxito con título. Termina con .show() */
    public static PenguinToast success(Context context, String title, String message) {
        return new PenguinToast(context, Type.SUCCESS, title, message, DURATION_NORMAL, Gravity.TOP);
    }

    /** Builder para toast de error. Termina con .show() */
    public static PenguinToast error(Context context, String message) {
        return new PenguinToast(context, Type.ERROR, null, message, DURATION_LONG, Gravity.TOP);
    }

    /** Builder para toast de error con título. Termina con .show() */
    public static PenguinToast error(Context context, String title, String message) {
        return new PenguinToast(context, Type.ERROR, title, message, DURATION_LONG, Gravity.TOP);
    }

    /** Builder para toast de advertencia. Termina con .show() */
    public static PenguinToast warning(Context context, String message) {
        return new PenguinToast(context, Type.WARNING, null, message, DURATION_LONG, Gravity.TOP);
    }

    /** Builder para toast de advertencia con título. Termina con .show() */
    public static PenguinToast warning(Context context, String title, String message) {
        return new PenguinToast(context, Type.WARNING, title, message, DURATION_LONG, Gravity.TOP);
    }

    /** Builder para toast de información. Termina con .show() */
    public static PenguinToast info(Context context, String message) {
        return new PenguinToast(context, Type.INFO, null, message, DURATION_NORMAL, Gravity.TOP);
    }

    /** Builder para toast de información con título. Termina con .show() */
    public static PenguinToast info(Context context, String title, String message) {
        return new PenguinToast(context, Type.INFO, title, message, DURATION_NORMAL, Gravity.TOP);
    }

    /**
     * Builder genérico con control total de parámetros. Termina con .show()
     *
     * @param type    Tipo de toast (Type.SUCCESS, ERROR, WARNING, INFO)
     * @param title   Título, o null para usar el título por defecto del tipo
     * @param message Mensaje principal
     */
    public static PenguinToast make(Context context, Type type, String title, String message) {
        int duration = (type == Type.SUCCESS || type == Type.INFO) ? DURATION_NORMAL : DURATION_LONG;
        return new PenguinToast(context, type, title, message, duration, Gravity.TOP);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  SHORTCUTS — métodos estáticos void (compatibilidad, uso rápido)
    // ═══════════════════════════════════════════════════════════════════════════

    public static void showSuccess(Context context, String message) {
        showInternal(context, Type.SUCCESS, null, message, DURATION_NORMAL, Gravity.TOP, globalDefaultAnim);
    }

    public static void showSuccess(Context context, String title, String message) {
        showInternal(context, Type.SUCCESS, title, message, DURATION_NORMAL, Gravity.TOP, globalDefaultAnim);
    }

    public static void showSuccess(Context context, String message, int durationSeconds) {
        showInternal(context, Type.SUCCESS, null, message, durationSeconds, Gravity.TOP, globalDefaultAnim);
    }

    public static void showError(Context context, String message) {
        showInternal(context, Type.ERROR, null, message, DURATION_LONG, Gravity.TOP, globalDefaultAnim);
    }

    public static void showError(Context context, String title, String message) {
        showInternal(context, Type.ERROR, title, message, DURATION_LONG, Gravity.TOP, globalDefaultAnim);
    }

    public static void showError(Context context, String message, int durationSeconds) {
        showInternal(context, Type.ERROR, null, message, durationSeconds, Gravity.TOP, globalDefaultAnim);
    }

    public static void showWarning(Context context, String message) {
        showInternal(context, Type.WARNING, null, message, DURATION_LONG, Gravity.TOP, globalDefaultAnim);
    }

    public static void showWarning(Context context, String title, String message) {
        showInternal(context, Type.WARNING, title, message, DURATION_LONG, Gravity.TOP, globalDefaultAnim);
    }

    public static void showWarning(Context context, String message, int durationSeconds) {
        showInternal(context, Type.WARNING, null, message, durationSeconds, Gravity.TOP, globalDefaultAnim);
    }

    public static void showInfo(Context context, String message) {
        showInternal(context, Type.INFO, null, message, DURATION_NORMAL, Gravity.TOP, globalDefaultAnim);
    }

    public static void showInfo(Context context, String title, String message) {
        showInternal(context, Type.INFO, title, message, DURATION_NORMAL, Gravity.TOP, globalDefaultAnim);
    }

    public static void showInfo(Context context, String message, int durationSeconds) {
        showInternal(context, Type.INFO, null, message, durationSeconds, Gravity.TOP, globalDefaultAnim);
    }

    // ─── Método estático completo (usado por PenguinToastQueue) ───────────────

    /**
     * Control total de parámetros — usa la animación por defecto global.
     * Usado internamente por PenguinToastQueue y PenguinUI.
     */
    public static void show(Context context, Type type, String title, String message,
                             int durationSeconds, int gravity) {
        showInternal(context, type, title, message, durationSeconds, gravity, globalDefaultAnim);
    }

    /**
     * Control total incluyendo tipo de animación.
     */
    public static void show(Context context, Type type, String title, String message,
                             int durationSeconds, int gravity, Anim anim) {
        showInternal(context, type, title, message, durationSeconds, gravity, anim);
    }

    /**
     * Mostrar con posición y offset personalizados.
     */
    public static void showCustom(Context context, Type type, String title, String message,
                                   int gravity, int yOffset) {
        showCustom(context, type, title, message, gravity, yOffset, globalDefaultAnim);
    }

    /**
     * Mostrar con posición, offset y animación personalizados.
     */
    public static void showCustom(Context context, Type type, String title, String message,
                                   int gravity, int yOffset, Anim anim) {
        if (context == null || type == null || message == null) return;
        View layout = buildLayout(context, type, title, message);
        Activity activity = getActivity(context);
        if (activity != null && !activity.isFinishing()) {
            showOverActivity(activity, layout, DURATION_LONG, gravity, yOffset, anim);
        } else {
            showFallbackToast(context, layout, DURATION_LONG, gravity);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  MOTOR INTERNO
    // ═══════════════════════════════════════════════════════════════════════════

    private static void showInternal(Context context, Type type, String title, String message,
                                      int durationSeconds, int gravity, Anim anim) {
        if (context == null || type == null || message == null) return;
        View layout = buildLayout(context, type, title, message);
        Activity activity = getActivity(context);
        if (activity != null && !activity.isFinishing()) {
            showOverActivity(activity, layout, durationSeconds, gravity, 0, anim);
        } else {
            showFallbackToast(context, layout, durationSeconds, gravity);
        }
    }

    /**
     * Agrega el toast al rootView de la Activity y controla las animaciones
     * de entrada/salida directamente, sin depender del sistema Toast.
     */
    private static void showOverActivity(Activity activity, View layout,
                                          int durationSeconds, int gravity, int extraOffsetPx,
                                          Anim anim) {
        FrameLayout rootView = (FrameLayout)
                activity.getWindow().getDecorView().getRootView();

        float density = activity.getResources().getDisplayMetrics().density;
        int   margin  = (int) (16 * density);

        // Compensar altura de la status bar
        int statusBarHeight = 0;
        int sbResId = activity.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (sbResId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(sbResId);
        }

        boolean isBottom = (gravity == Gravity.BOTTOM);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                isBottom
                        ? (Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        : (Gravity.TOP    | Gravity.CENTER_HORIZONTAL)
        );
        lp.leftMargin  = margin;
        lp.rightMargin = margin;
        if (isBottom) {
            lp.bottomMargin = margin + Math.max(extraOffsetPx, 0);
        } else {
            lp.topMargin = statusBarHeight + margin + Math.max(extraOffsetPx, 0);
        }

        rootView.addView(layout, lp);

        // Animación de entrada
        layout.startAnimation(AnimationUtils.loadAnimation(activity, getEnterRes(anim)));

        // Duración en pantalla, luego animación de salida
        long holdMs = durationSeconds <= 2 ? 2000L : 3500L;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (layout.getParent() == null) return;
            Animation exitAnim = AnimationUtils.loadAnimation(activity, getExitRes(anim));
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

    /** Fallback cuando no hay Activity (ej.: contexto de aplicación). */
    private static void showFallbackToast(Context context, View layout,
                                           int durationSeconds, int gravity) {
        Toast toast = new Toast(context);
        toast.setDuration(durationSeconds <= 2 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(gravity, 0, gravity == Gravity.TOP ? 50 : 0);
        toast.show();
    }

    // ─── Recursos de animación por tipo ───────────────────────────────────────

    private static int getEnterRes(Anim anim) {
        if (anim == null) return R.anim.toast_enter;
        switch (anim) {
            case SLIDE_RIGHT_LEFT: return R.anim.toast_anim_rl_enter;
            case SLIDE_TOP:        return R.anim.toast_slide_v_enter;
            case SLIDE_BOTTOM:     return R.anim.toast_anim_b_enter;
            case FADE:             return R.anim.toast_fade_enter;
            case POP:              return R.anim.toast_anim_pop_enter;
            case BOUNCE:           return R.anim.toast_anim_bounce_enter;
            default:               return R.anim.toast_enter; // SLIDE_LEFT_RIGHT
        }
    }

    private static int getExitRes(Anim anim) {
        if (anim == null) return R.anim.toast_exit;
        switch (anim) {
            case SLIDE_RIGHT_LEFT: return R.anim.toast_anim_rl_exit;
            case SLIDE_TOP:        return R.anim.toast_slide_v_exit;
            case SLIDE_BOTTOM:     return R.anim.toast_anim_b_exit;
            case FADE:             return R.anim.toast_fade_exit;
            case POP:              return R.anim.toast_anim_pop_exit;
            case BOUNCE:           return R.anim.toast_exit; // sale igual que SLIDE_LEFT_RIGHT
            default:               return R.anim.toast_exit; // SLIDE_LEFT_RIGHT
        }
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    /** Extrae la Activity del Context recorriendo ContextWrappers. */
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

        int bgColor = context.getResources().getColor(R.color.toast_bg_dark, null);
        if (theme.hasBackgroundColor()) bgColor = theme.getBackgroundColor();
        if (theme.isGlass()) {
            bgColor = PenguinTheme.applyAlpha(bgColor, theme.getBackgroundAlpha());
            container.setCardElevation(0);
        }
        container.setCardBackgroundColor(bgColor);
        container.setRadius(theme.getCornerRadiusDp() * density);

        int accentColorInt = theme.hasAccentColor()
                ? theme.getAccentColor()
                : context.getResources().getColor(type.accentColor, null);
        GradientDrawable accentDrawable = new GradientDrawable();
        accentDrawable.setShape(GradientDrawable.RECTANGLE);
        accentDrawable.setColor(accentColorInt);
        accentDrawable.setCornerRadius(4 * density);
        accentBar.setBackground(accentDrawable);

        if (theme.hasTextPrimaryColor())   titleView.setTextColor(theme.getTextPrimaryColor());
        if (theme.hasTextSecondaryColor()) messageView.setTextColor(theme.getTextSecondaryColor());
    }
}
