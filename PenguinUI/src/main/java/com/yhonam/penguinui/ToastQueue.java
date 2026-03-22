package com.yhonam.penguinui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ToastQueue - Cola de notificaciones toast para evitar superposición
 *
 * Problema que resuelve:
 * Cuando se muestran múltiples toasts rápidamente, estos se superponen
 * y crean una mala experiencia de usuario. ToastQueue los encola y
 * los muestra uno por uno.
 *
 * Uso:
 * <pre>
 * {@code
 * // En tu Application o clase principal
 * ToastQueue.init(context);
 *
 * // Mostrar toasts (se encolan automáticamente)
 * ToastQueue.showSuccess("Mensaje 1");
 * ToastQueue.showError("Mensaje 2");
 * ToastQueue.showWarning("Mensaje 3");
 * }
 * </pre>
 */
public class ToastQueue {

    // Cola de toasts pendientes
    private static Queue<ToastItem> toastQueue = new LinkedList<>();

    // Handler para ejecutar en el hilo principal
    private static final Handler handler = new Handler(Looper.getMainLooper());

    // Contexto de la aplicación
    private static Context appContext;

    // ¿Hay un toast mostrándose actualmente?
    private static boolean isShowing = false;

    // Toast actual
    private static Toast currentToast;

    // Tiempo de espera entre toasts (ms)
    private static int DELAY_BETWEEN_TOASTS = 500;

    /**
     * Item de toast en la cola
     */
    private static class ToastItem {
        final Context context;
        final CustomToast.ToastType type;
        final String title;
        final String message;
        final int duration;
        final int gravity;
        final boolean withHaptic;

        ToastItem(Context context, CustomToast.ToastType type, String title, String message,
                  int duration, int gravity, boolean withHaptic) {
            this.context = context.getApplicationContext();
            this.type = type;
            this.title = title;
            this.message = message;
            this.duration = duration;
            this.gravity = gravity;
            this.withHaptic = withHaptic;
        }
    }

    /**
     * Inicializar la cola de toasts
     *
     * @param context Contexto de la aplicación
     */
    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    /**
     * Mostrar toast de éxito (se encola automáticamente)
     *
     * @param context Contexto
     * @param message Mensaje
     */
    public static void showSuccess(Context context, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.SUCCESS, null, message,
                CustomToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de éxito con título
     */
    public static void showSuccess(Context context, String title, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.SUCCESS, title, message,
                CustomToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de éxito con vibración háptica
     */
    public static void showSuccess(Context context, String message, boolean withHaptic) {
        enqueue(new ToastItem(context, CustomToast.ToastType.SUCCESS, null, message,
                CustomToast.DURATION_NORMAL, Gravity.TOP, withHaptic));
    }

    /**
     * Mostrar toast de error
     */
    public static void showError(Context context, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.ERROR, null, message,
                CustomToast.DURATION_LONG, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de error con título
     */
    public static void showError(Context context, String title, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.ERROR, title, message,
                CustomToast.DURATION_LONG, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de error con vibración háptica
     */
    public static void showError(Context context, String message, boolean withHaptic) {
        enqueue(new ToastItem(context, CustomToast.ToastType.ERROR, null, message,
                CustomToast.DURATION_LONG, Gravity.TOP, withHaptic));
    }

    /**
     * Mostrar toast de advertencia
     */
    public static void showWarning(Context context, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.WARNING, null, message,
                CustomToast.DURATION_LONG, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de advertencia con título
     */
    public static void showWarning(Context context, String title, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.WARNING, title, message,
                CustomToast.DURATION_LONG, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de advertencia con vibración háptica
     */
    public static void showWarning(Context context, String message, boolean withHaptic) {
        enqueue(new ToastItem(context, CustomToast.ToastType.WARNING, null, message,
                CustomToast.DURATION_LONG, Gravity.TOP, withHaptic));
    }

    /**
     * Mostrar toast de información
     */
    public static void showInfo(Context context, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.INFO, null, message,
                CustomToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de información con título
     */
    public static void showInfo(Context context, String title, String message) {
        enqueue(new ToastItem(context, CustomToast.ToastType.INFO, title, message,
                CustomToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    /**
     * Mostrar toast de información con vibración háptica
     */
    public static void showInfo(Context context, String message, boolean withHaptic) {
        enqueue(new ToastItem(context, CustomToast.ToastType.INFO, null, message,
                CustomToast.DURATION_NORMAL, Gravity.TOP, withHaptic));
    }

    /**
     * Encolar un nuevo toast
     */
    private static synchronized void enqueue(ToastItem item) {
        if (appContext == null) {
            appContext = item.context;
        }

        toastQueue.offer(item);
        processQueue();
    }

    /**
     * Procesar la cola de toasts
     */
    private static synchronized void processQueue() {
        if (isShowing || toastQueue.isEmpty()) {
            return;
        }

        isShowing = true;
        ToastItem item = toastQueue.poll();

        if (item != null) {
            handler.post(() -> {
                // Vibración háptica si está habilitada
                if (item.withHaptic && item.context != null) {
                    com.yhonam.penguinui.HapticFeedback.vibrate(item.context, item.type);
                }

                // Mostrar el toast
                CustomToast.show(item.context, item.type, item.title, item.message,
                        item.duration, item.gravity);

                // Programar el siguiente toast
                int toastDurationMs = item.duration * 1000;
                handler.postDelayed(() -> {
                    isShowing = false;
                    processQueue();
                }, toastDurationMs + DELAY_BETWEEN_TOASTS);
            });
        }
    }

    /**
     * Limpiar la cola de toasts pendientes
     */
    public static synchronized void clear() {
        toastQueue.clear();
    }

    /**
     * Cancelar el toast actual y limpiar la cola
     */
    public static synchronized void cancelAll() {
        if (currentToast != null) {
            currentToast.cancel();
            currentToast = null;
        }
        clear();
        isShowing = false;
    }

    /**
     * Obtener el número de toasts en espera
     *
     * @return Tamaño de la cola
     */
    public static synchronized int getQueueSize() {
        return toastQueue.size();
    }

    /**
     * Verificar si hay toasts en espera
     *
     * @return true si hay toasts en la cola
     */
    public static synchronized boolean hasPending() {
        return !toastQueue.isEmpty();
    }

    /**
     * Verificar si se está mostrando un toast actualmente
     *
     * @return true si hay un toast mostrándose
     */
    public static synchronized boolean isShowing() {
        return isShowing;
    }

    /**
     * Establecer el retraso entre toasts (ms)
     *
     * @param delayMs Retraso en milisegundos
     */
    public static void setDelayBetweenToasts(int delayMs) {
        DELAY_BETWEEN_TOASTS = delayMs;
    }
}
