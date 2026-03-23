package com.yhonam.penguinui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Queue;

/**
 * PenguinToastQueue - Cola de toasts para evitar superposición
 *
 * Cuando se lanzan múltiples toasts en rápida sucesión, los muestra
 * uno por uno respetando el orden de llegada.
 *
 * Uso:
 *   PenguinToastQueue.showSuccess(context, "Guardado");
 *   PenguinToastQueue.showError(context, "Error de red");
 *   PenguinToastQueue.showWarning(context, "Espacio bajo");
 */
public class PenguinToastQueue {

    private static final Queue<ToastItem> queue   = new LinkedList<>();
    private static final Handler          handler = new Handler(Looper.getMainLooper());
    private static       boolean          isShowing = false;
    private static volatile int           DELAY_MS  = 500;

    // ─── Item interno ──────────────────────────────────────────────────────────

    private static class ToastItem {
        final WeakReference<Activity> activityRef;
        final Context                 appContext;
        final PenguinToast.Type       type;
        final String                  title;
        final String                  message;
        final int                     duration;
        final int                     gravity;
        final boolean                 withHaptic;

        ToastItem(Context ctx, PenguinToast.Type type, String title, String message,
                  int duration, int gravity, boolean withHaptic) {
            Activity activity  = PenguinToast.getActivity(ctx);
            this.activityRef   = (activity != null) ? new WeakReference<>(activity) : null;
            this.appContext    = ctx.getApplicationContext();
            this.type          = type;
            this.title         = title;
            this.message       = message;
            this.duration      = duration;
            this.gravity       = gravity;
            this.withHaptic    = withHaptic;
        }

        /** Devuelve la Activity si sigue viva, o el applicationContext como fallback. */
        Context resolveContext() {
            if (activityRef != null) {
                Activity a = activityRef.get();
                if (a != null && !a.isFinishing()) return a;
            }
            return appContext;
        }
    }

    // ─── SUCCESS ───────────────────────────────────────────────────────────────

    public static void showSuccess(Context ctx, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.SUCCESS, null, message,
                PenguinToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    public static void showSuccess(Context ctx, String title, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.SUCCESS, title, message,
                PenguinToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    public static void showSuccess(Context ctx, String message, boolean withHaptic) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.SUCCESS, null, message,
                PenguinToast.DURATION_NORMAL, Gravity.TOP, withHaptic));
    }

    // ─── ERROR ─────────────────────────────────────────────────────────────────

    public static void showError(Context ctx, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.ERROR, null, message,
                PenguinToast.DURATION_LONG, Gravity.TOP, false));
    }

    public static void showError(Context ctx, String title, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.ERROR, title, message,
                PenguinToast.DURATION_LONG, Gravity.TOP, false));
    }

    public static void showError(Context ctx, String message, boolean withHaptic) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.ERROR, null, message,
                PenguinToast.DURATION_LONG, Gravity.TOP, withHaptic));
    }

    // ─── WARNING ───────────────────────────────────────────────────────────────

    public static void showWarning(Context ctx, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.WARNING, null, message,
                PenguinToast.DURATION_LONG, Gravity.TOP, false));
    }

    public static void showWarning(Context ctx, String title, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.WARNING, title, message,
                PenguinToast.DURATION_LONG, Gravity.TOP, false));
    }

    public static void showWarning(Context ctx, String message, boolean withHaptic) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.WARNING, null, message,
                PenguinToast.DURATION_LONG, Gravity.TOP, withHaptic));
    }

    // ─── INFO ──────────────────────────────────────────────────────────────────

    public static void showInfo(Context ctx, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.INFO, null, message,
                PenguinToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    public static void showInfo(Context ctx, String title, String message) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.INFO, title, message,
                PenguinToast.DURATION_NORMAL, Gravity.TOP, false));
    }

    public static void showInfo(Context ctx, String message, boolean withHaptic) {
        enqueue(new ToastItem(ctx, PenguinToast.Type.INFO, null, message,
                PenguinToast.DURATION_NORMAL, Gravity.TOP, withHaptic));
    }

    // ─── CONTROL ───────────────────────────────────────────────────────────────

    /** Limpiar todos los toasts pendientes */
    public static synchronized void clear() { queue.clear(); }

    /** Cancelar todo */
    public static synchronized void cancelAll() {
        queue.clear();
        isShowing = false;
    }

    /** Cantidad de toasts en espera */
    public static synchronized int getQueueSize() { return queue.size(); }

    /** ¿Hay toasts pendientes? */
    public static synchronized boolean hasPending() { return !queue.isEmpty(); }

    /** ¿Hay un toast mostrándose ahora? */
    public static synchronized boolean isShowing() { return isShowing; }

    /** Configurar delay entre toasts en ms (default: 500ms) */
    public static void setDelayBetweenToasts(int delayMs) { DELAY_MS = delayMs; }

    // ─── INTERNO ───────────────────────────────────────────────────────────────

    private static synchronized void enqueue(ToastItem item) {
        queue.offer(item);
        processQueue();
    }

    private static synchronized void processQueue() {
        if (isShowing || queue.isEmpty()) return;

        isShowing = true;
        ToastItem item = queue.poll();

        if (item != null) {
            handler.post(() -> {
                Context ctx = item.resolveContext();
                if (item.withHaptic) {
                    PenguinHaptic.vibrate(ctx, item.type);
                }
                PenguinToast.show(ctx, item.type, item.title, item.message,
                        item.duration, item.gravity);

                handler.postDelayed(() -> {
                    synchronized (PenguinToastQueue.class) {
                        isShowing = false;
                        processQueue();
                    }
                }, (long) item.duration * 1000 + DELAY_MS);
            });
        }
    }
}
