package com.yhonam.penguinui;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

/**
 * PenguinHaptic - Retroalimentación táctil/vibración para la librería Penguin UI
 *
 * Uso básico:
 *   PenguinHaptic.vibrateSuccess(context);
 *   PenguinHaptic.vibrateError(context);
 *   PenguinHaptic.vibrate(context, PenguinToast.Type.SUCCESS);
 *
 * Compatible con Android 7.0+ (API 24+).
 */
public class PenguinHaptic {

    // Duraciones de vibración por tipo (ms)
    private static final int VIBRATE_SUCCESS = 50;
    private static final int VIBRATE_ERROR   = 200;
    private static final int VIBRATE_WARNING = 75;
    private static final int VIBRATE_INFO    = 30;
    private static final int DOUBLE_DELAY    = 100;

    /**
     * Vibrar según el tipo de toast
     *
     * @param context Contexto
     * @param type    Tipo de toast (mapea al patrón de vibración correspondiente)
     */
    public static void vibrate(Context context, PenguinToast.Type type) {
        switch (type) {
            case SUCCESS: vibrateSuccess(context); break;
            case ERROR:   vibrateError(context);   break;
            case WARNING: vibrateWarning(context);  break;
            case INFO:    vibrateInfo(context);     break;
        }
    }

    /** Vibración corta y suave — éxito */
    public static void vibrateSuccess(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_SUCCESS}, -1);
    }

    /** Vibración larga y fuerte — error */
    public static void vibrateError(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_ERROR}, -1);
    }

    /** Doble vibración corta — advertencia */
    public static void vibrateWarning(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_WARNING, DOUBLE_DELAY, VIBRATE_WARNING}, -1);
    }

    /** Vibración muy corta — información */
    public static void vibrateInfo(Context context) {
        vibratePattern(context, new long[]{0, VIBRATE_INFO}, -1);
    }

    /** Vibración de confirmación — diálogos */
    public static void vibrateConfirm(Context context) {
        vibratePattern(context, new long[]{0, 100}, -1);
    }

    /** Vibración de cancelación */
    public static void vibrateCancel(Context context) {
        vibratePattern(context, new long[]{0, 50, 50, 50}, -1);
    }

    /** Tres vibraciones — error crítico */
    public static void vibrateCritical(Context context) {
        vibratePattern(context, new long[]{0, 150, 50, 150, 50, 150}, -1);
    }

    /**
     * Vibración con patrón personalizado
     *
     * @param context Contexto
     * @param pattern Patrón [delay, duration, delay, duration, ...]
     * @param repeat  Índice de repetición (-1 para no repetir)
     */
    public static void vibratePattern(Context context, long[] pattern, int repeat) {
        Vibrator vibrator = getVibrator(context);
        if (vibrator == null || !vibrator.hasVibrator()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat));
        } else {
            vibrator.vibrate(pattern, repeat);
        }
    }

    /**
     * Vibración de duración única personalizada
     *
     * @param context  Contexto
     * @param duration Duración en milisegundos
     */
    public static void vibrateDuration(Context context, long duration) {
        Vibrator vibrator = getVibrator(context);
        if (vibrator == null || !vibrator.hasVibrator()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(duration);
        }
    }

    /** Cancelar vibración en curso */
    public static void cancel(Context context) {
        Vibrator vibrator = getVibrator(context);
        if (vibrator != null) vibrator.cancel();
    }

    /** Verificar si el dispositivo tiene vibrador */
    public static boolean hasVibrator(Context context) {
        Vibrator v = getVibrator(context);
        return v != null && v.hasVibrator();
    }

    private static Vibrator getVibrator(Context context) {
        if (context == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            VibratorManager vm = (VibratorManager)
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            return vm != null ? vm.getDefaultVibrator() : null;
        }
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
}
