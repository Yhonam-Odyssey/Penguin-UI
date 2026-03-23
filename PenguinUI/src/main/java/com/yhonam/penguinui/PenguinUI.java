package com.yhonam.penguinui;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

/**
 * PenguinUI - Punto de entrada principal de la librería Penguin UI
 *
 * ═══════════════════════════════════════════════════════════════════════════
 *  USO RÁPIDO (accesos directos)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *  // Toasts
 *  PenguinUI.success(context, "Guardado correctamente");
 *  PenguinUI.error(context, "Ocurrió un error");
 *  PenguinUI.warning(context, "Advertencia importante");
 *  PenguinUI.info(context, "Versión 1.0.0");
 *
 *  // Diálogos
 *  PenguinUI.confirmDialog("¿Eliminar?", "No se puede deshacer.")
 *      .setOnConfirmListener(() -> borrar())
 *      .show(getSupportFragmentManager(), "confirm");
 *
 *  PenguinUI.infoDialog("¡Listo!", "Tu perfil fue actualizado.")
 *      .show(getSupportFragmentManager(), "info");
 *
 *  PenguinUI.loading("Procesando...")
 *      .show(getSupportFragmentManager(), "loading");
 *
 *  // Bottom Sheet
 *  PenguinUI.sheet("Opciones")
 *      .addItem(R.drawable.ic_edit,   "Editar",   () -> editar())
 *      .addItem(R.drawable.ic_delete, "Eliminar", () -> borrar())
 *      .build()
 *      .show(getSupportFragmentManager(), "sheet");
 *
 *  // Haptic
 *  PenguinUI.hapticSuccess(context);
 *  PenguinUI.hapticError(context);
 *
 * ═══════════════════════════════════════════════════════════════════════════
 *  USO AVANZADO — accede directamente a cada clase Penguin
 * ═══════════════════════════════════════════════════════════════════════════
 *
 *  PenguinToast.show(context, PenguinToast.Type.SUCCESS, "Título", "Mensaje", 5, Gravity.BOTTOM);
 *  PenguinToastQueue.showSuccess(context, "msg", true);  // con haptic
 *  PenguinSheet.Builder builder = new PenguinSheet.Builder();
 *  PenguinHaptic.vibrateCritical(context);
 */
public final class PenguinUI {

    private PenguinUI() {}

    // ═══════════════════════════════════════════════════════════════════════════
    //  TEMA
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Configurar el tema global de PenguinUI.
     * Llamar una vez al inicio (Application.onCreate o primer Activity).
     * Sin llamar a este método, se usa el preset NEO (comportamiento v1.0).
     *
     * Ejemplo:
     *   PenguinUI.setTheme(
     *       PenguinTheme.builder()
     *           .preset(PenguinTheme.Preset.GLASS)
     *           .backgroundAlpha(0.8f)
     *           .accentColor(ContextCompat.getColor(this, R.color.mi_color))
     *           .cornerRadius(PenguinTheme.Radius.SOFT)
     *           .build()
     *   );
     */
    public static void setTheme(PenguinTheme theme) {
        PenguinTheme.apply(theme);
    }

    /** Volver al tema por defecto (preset NEO). */
    public static void resetTheme() {
        PenguinTheme.reset();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TOASTS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Toast de éxito (verde) */
    public static void success(Context context, String message) {
        if (context == null || message == null) return;
        PenguinToast.showSuccess(context, message);
    }

    public static void success(Context context, String title, String message) {
        if (context == null || message == null) return;
        PenguinToast.showSuccess(context, title != null ? title : "", message);
    }

    /** Toast de error (rojo) */
    public static void error(Context context, String message) {
        if (context == null || message == null) return;
        PenguinToast.showError(context, message);
    }

    public static void error(Context context, String title, String message) {
        if (context == null || message == null) return;
        PenguinToast.showError(context, title != null ? title : "", message);
    }

    /** Toast de advertencia (amarillo) */
    public static void warning(Context context, String message) {
        if (context == null || message == null) return;
        PenguinToast.showWarning(context, message);
    }

    public static void warning(Context context, String title, String message) {
        if (context == null || message == null) return;
        PenguinToast.showWarning(context, title != null ? title : "", message);
    }

    /** Toast de información (cian) */
    public static void info(Context context, String message) {
        if (context == null || message == null) return;
        PenguinToast.showInfo(context, message);
    }

    public static void info(Context context, String title, String message) {
        if (context == null || message == null) return;
        PenguinToast.showInfo(context, title != null ? title : "", message);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DIÁLOGOS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Diálogo de confirmación (Confirmar / Cancelar)
     * Llama a .setOnConfirmListener() y luego .show(fm, tag)
     */
    public static PenguinDialog confirmDialog(String title, String message) {
        return PenguinDialog.confirm(title != null ? title : "", message != null ? message : "");
    }

    /**
     * Diálogo de eliminación (Eliminar / Cancelar)
     */
    public static PenguinDialog deleteDialog(String title, String message) {
        return PenguinDialog.delete(title != null ? title : "", message != null ? message : "");
    }

    /**
     * Diálogo de advertencia (Aceptar / Cancelar)
     */
    public static PenguinDialog warningDialog(String title, String message) {
        return PenguinDialog.warning(title != null ? title : "", message != null ? message : "");
    }

    /**
     * Diálogo de cierre de sesión
     */
    public static PenguinDialog logoutDialog(String title, String message) {
        return PenguinDialog.logout(title != null ? title : "", message != null ? message : "");
    }

    /**
     * Diálogo informativo con un solo botón "Aceptar" — éxito
     */
    public static PenguinInfoDialog infoDialog(String title, String message) {
        return PenguinInfoDialog.info(title != null ? title : "", message != null ? message : "");
    }

    /**
     * Diálogo de éxito informativo
     */
    public static PenguinInfoDialog successDialog(String title, String message) {
        return PenguinInfoDialog.success(title != null ? title : "", message != null ? message : "");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  LOADING
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Diálogo de carga con spinner.
     * Llama a loading.show(fm, "loading") para mostrarlo.
     * Llama a loading.dismiss() al terminar.
     */
    public static PenguinLoadingDialog loading(String message) {
        PenguinLoadingDialog dialog = PenguinLoadingDialog.newInstance(message != null ? message : "");
        dialog.setCancelable(false);
        return dialog;
    }

    public static PenguinLoadingDialog loading(String message, String subMessage) {
        PenguinLoadingDialog dialog = PenguinLoadingDialog.newInstance(
                message != null ? message : "",
                subMessage != null ? subMessage : "");
        dialog.setCancelable(false);
        return dialog;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  BOTTOM SHEET
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Inicia un builder de PenguinSheet con el título dado.
     *
     * Ejemplo:
     *   PenguinUI.sheet("Acciones")
     *       .addItem(R.drawable.ic_edit, "Editar", () -> editar())
     *       .build()
     *       .show(getSupportFragmentManager(), "sheet");
     */
    public static PenguinSheet.Builder sheet(String title) {
        return new PenguinSheet.Builder().setTitle(title != null ? title : "");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HAPTIC
    // ═══════════════════════════════════════════════════════════════════════════

    public static void hapticSuccess(Context context) { if (context != null) PenguinHaptic.vibrateSuccess(context); }
    public static void hapticError(Context context)   { if (context != null) PenguinHaptic.vibrateError(context);   }
    public static void hapticWarning(Context context) { if (context != null) PenguinHaptic.vibrateWarning(context); }
    public static void hapticInfo(Context context)    { if (context != null) PenguinHaptic.vibrateInfo(context);    }
}
