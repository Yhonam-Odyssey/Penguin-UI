package com.yhonam.penguinui;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

/**
 * HapticFeedback - Utilidad para vibración háptica en toasts y diálogos
 *
 * Proporciona retroalimentación táctil para diferentes tipos de eventos:
 * - Éxito: Vibración corta y suave
 * - Error: Vibración larga y fuerte
 * - Advertencia: Dos vibraciones cortas
 * - Información: Una vibración muy corta
 *
 * Uso:
 * <pre>
 * {@code
 * HapticFeedback.vibrate(context, CustomToast.ToastType.SUCCESS);
 * HapticFeedback.vibrateSuccess(context);
 * HapticFeedback.vibrateError(context);
 * }
 * </pre>
 */
public class HapticFeedback {

    // Duraciones de vibración por tipo (ms)
    private static final int VIBRATE_SUCCESS = 50;      // Corta y suave
    private static final int VIBRATE_ERROR = 200;       // Larga y fuerte
    private static final int VIBRATE_WARNING = 100;     // Media
    private static final int VIBRATE_INFO = 30;         // Muy corta
    private static final int VIBRATE_DOUBLE = 75;       // Para warning (doble)

    // Delay entre vibraciones dobles (ms)
    private static final int DOUBLE_VIBRATE_DELAY = 100;

    /**
     * Vibrar según el tipo de toast
     *
     * @param context Contexto
     * @param type Tipo de toast
     */
    public static void vibrate(Context context, CustomToast.ToastType type) {
        Vibrator vibrator = getVibrator(context);

        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        switch (type) {
            case SUCCESS:
                vibrateSuccess(context);
                break;
            case ERROR:
                vibrateError(context);
                break;
            case WARNING:
                vibrateWarning(context);
                break;
            case INFO:
                vibrateInfo(context);
                break;
        }
    }

    /**
     * Vibración de éxito (corta y suave)
     *
     * @param context Contexto
     */
    public static void vibrateSuccess(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_SUCCESS}, -1);
    }

    /**
     * Vibración de error (larga y fuerte)
     *
     * @param context Contexto
     */
    public static void vibrateError(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_ERROR}, -1);
    }

    /**
     * Vibración de advertencia (dos vibraciones cortas)
     *
     * @param context Contexto
     */
    public static void vibrateWarning(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_DOUBLE, DOUBLE_VIBRATE_DELAY, VIBRATE_DOUBLE}, -1);
    }

    /**
     * Vibración de información (muy corta)
     *
     * @param context Contexto
     */
    public static void vibrateInfo(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_INFO}, -1);
    }

    /**
     * Vibración de confirmación (para diálogos)
     *
     * @param context Contexto
     */
    public static void vibrateConfirm(Context context) {
        vibratePattern(context, new long[]{0, 100}, -1);
    }

    /**
     * Vibración de cancelación (para diálogos)
     *
     * @param context Contexto
     */
    public static void vibrateCancel(Context context) {
        vibratePattern(context, new long[]{0, 50, 50, 50}, -1);
    }

    /**
     * Vibración de error crítica (tres vibraciones)
     *
     * @param context Contexto
     */
    public static void vibrateCritical(Context context) {
        vibratePattern(context, new long[]{0, 150, 50, 150, 50, 150}, -1);
    }

    /**
     * Vibración personalizada con patrón
     *
     * @param context Contexto
     * @param pattern Patrón de vibración [delay, duration, delay, duration, ...]
     * @param repeat Índice donde empezar a repetir (-1 para no repetir)
     */
    public static void vibratePattern(Context context, long[] pattern, int repeat) {
        Vibrator vibrator = getVibrator(context);

        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0+ - Usar VibrationEffect
            VibrationEffect effect = VibrationEffect.createWaveform(pattern, repeat);
            vibrator.vibrate(effect);
        } else {
            // Android 7.x y anterior
            vibrator.vibrate(pattern, repeat);
        }
    }

    /**
     * Vibración personalizada con duración única
     *
     * @param context Contexto
     * @param duration Duración en milisegundos
     */
    public static void vibrateDuration(Context context, int duration) {
        Vibrator vibrator = getVibrator(context);

        if (vibrator == null || !vibrator.hasVibrator()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0+ - Usar VibrationEffect
            VibrationEffect effect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(effect);
        } else {
            // Android 7.x y anterior
            vibrator.vibrate(duration);
        }
    }

    /**
     * Cancelar cualquier vibración en curso
     *
     * @param context Contexto
     */
    public static void cancel(Context context) {
        Vibrator vibrator = getVibrator(context);

        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    /**
     * Obtener el servicio Vibrator
     */
    private static Vibrator getVibrator(Context context) {
        if (context == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ - Usar VibratorManager
            VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            return vibratorManager != null ? vibratorManager.getDefaultVibrator() : null;
        } else {
            // Android 11 y anterior
            return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    /**
     * Verificar si el dispositivo tiene vibrador
     *
     * @param context Contexto
     * @return true si tiene vibrador
     */
    public static boolean hasVibrator(Context context) {
        Vibrator vibrator = getVibrator(context);
        return vibrator != null && vibrator.hasVibrator();
    }

    /**
     * Verificar si la vibración está habilitada
     *
     * @param context Contexto
     * @return true si está habilitada
     */
    public static boolean isHapticEnabled(Context context) {
        return hasVibrator(context);
    }
}
