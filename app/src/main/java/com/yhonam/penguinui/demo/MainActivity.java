package com.yhonam.penguinui.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yhonam.penguinui.PenguinDialog;
import com.yhonam.penguinui.PenguinHaptic;
import com.yhonam.penguinui.PenguinInfoDialog;
import com.yhonam.penguinui.PenguinLoadingDialog;
import com.yhonam.penguinui.PenguinSheet;
import com.yhonam.penguinui.PenguinToast;
import com.yhonam.penguinui.PenguinToastQueue;
import com.yhonam.penguinui.PenguinUI;

/**
 * MainActivity - Demo interactiva de todos los componentes de Penguin UI

 * Cada sección demuestra cómo usar las clases de la librería,
 * tanto con los accesos directos de PenguinUI (fachada) como
 * con las clases individuales (PenguinToast, PenguinDialog, etc.)
 */
public class MainActivity extends AppCompatActivity {

    private PenguinLoadingDialog activeLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        setupToastButtons();
        setupToastQueueButtons();
        setupDialogButtons();
        setupInfoDialogButtons();
        setupLoadingButtons();
        setupSheetButtons();
        setupHapticButtons();
        setupFacadeButtons();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TOASTS
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupToastButtons() {
        Button btnSuccess = findViewById(R.id.btnToastSuccess);
        Button btnError   = findViewById(R.id.btnToastError);
        Button btnWarning = findViewById(R.id.btnToastWarning);
        Button btnInfo    = findViewById(R.id.btnToastInfo);
        Button btnCustom  = findViewById(R.id.btnToastCustom);

        btnSuccess.setOnClickListener(v ->
                PenguinToast.showSuccess(this, "Operación completada con éxito"));

        btnError.setOnClickListener(v ->
                PenguinToast.showError(this, "No se pudo conectar al servidor"));

        btnWarning.setOnClickListener(v ->
                PenguinToast.showWarning(this, "El espacio de almacenamiento está bajo"));

        btnInfo.setOnClickListener(v ->
                PenguinToast.showInfo(this, "Hay una actualización disponible"));

        btnCustom.setOnClickListener(v ->
                PenguinToast.showSuccess(this, "¡Guardado!", "Tu perfil fue actualizado correctamente"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  TOAST QUEUE
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupToastQueueButtons() {
        Button btnQueue       = findViewById(R.id.btnToastQueue);
        Button btnQueueHaptic = findViewById(R.id.btnToastQueueHaptic);

        btnQueue.setOnClickListener(v -> {
            PenguinToastQueue.showSuccess(this, "Paso 1: datos cargados");
            PenguinToastQueue.showInfo(this,    "Paso 2: procesando...");
            PenguinToastQueue.showWarning(this, "Paso 3: verificando permisos");
            PenguinToastQueue.showSuccess(this, "Paso 4: ¡todo listo!");
        });

        btnQueueHaptic.setOnClickListener(v -> {
            PenguinToastQueue.showSuccess(this, "Con vibración", true);
            PenguinToastQueue.showError(this,   "Error con vibración", true);
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  DIÁLOGOS (PenguinDialog - 2 botones)
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupDialogButtons() {
        Button btnConfirm = findViewById(R.id.btnDialogConfirm);
        Button btnDelete  = findViewById(R.id.btnDialogDelete);
        Button btnWarning = findViewById(R.id.btnDialogWarning);
        Button btnLogout  = findViewById(R.id.btnDialogLogout);

        btnConfirm.setOnClickListener(v -> {
            PenguinDialog.confirm("¿Guardar cambios?", "Se guardarán todos los cambios realizados.")
                    .setOnConfirmListener(() ->
                            PenguinToast.showSuccess(this, "¡Guardado!", "Cambios guardados correctamente"))
                    .setOnCancelListener(() ->
                            PenguinToast.showInfo(this, "Operación cancelada"))
                    .show(getSupportFragmentManager(), "confirm_dialog");
        });

        btnDelete.setOnClickListener(v -> {
            PenguinDialog.delete("¿Eliminar registro?", "Esta acción no se puede deshacer.")
                    .setOnConfirmListener(() ->
                            PenguinToast.showError(this, "Registro eliminado"))
                    .show(getSupportFragmentManager(), "delete_dialog");
        });

        btnWarning.setOnClickListener(v -> {
            PenguinDialog.warning("Advertencia de seguridad", "Tu sesión expirará en 5 minutos.")
                    .setOnConfirmListener(() ->
                            PenguinToast.showSuccess(this, "Sesión renovada"))
                    .show(getSupportFragmentManager(), "warning_dialog");
        });

        btnLogout.setOnClickListener(v -> {
            PenguinDialog.logout("Cerrar sesión", "¿Estás seguro de que deseas salir?")
                    .setOnConfirmListener(() ->
                            PenguinToast.showInfo(this, "Sesión cerrada"))
                    .show(getSupportFragmentManager(), "logout_dialog");
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  INFO DIALOGS (PenguinInfoDialog - 1 botón)
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupInfoDialogButtons() {
        Button btnSuccess = findViewById(R.id.btnInfoSuccess);
        Button btnInfo    = findViewById(R.id.btnInfoInfo);
        Button btnWarning = findViewById(R.id.btnInfoWarning);

        btnSuccess.setOnClickListener(v ->
                PenguinInfoDialog.success("¡Registro exitoso!", "Tu cuenta ha sido creada correctamente.")
                        .setOnDismissListener(() ->
                                PenguinToast.showSuccess(this, "Diálogo cerrado"))
                        .show(getSupportFragmentManager(), "info_success"));

        btnInfo.setOnClickListener(v ->
                PenguinInfoDialog.info("Acerca de Penguin UI",
                        "Librería de componentes UI modernos para Android.\nVersión 1.0.0")
                        .show(getSupportFragmentManager(), "info_info"));

        btnWarning.setOnClickListener(v ->
                PenguinInfoDialog.warning("Permiso requerido",
                        "Esta función necesita acceso a tu ubicación para funcionar correctamente.")
                        .show(getSupportFragmentManager(), "info_warning"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  LOADING DIALOG
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupLoadingButtons() {
        Button btnLoading    = findViewById(R.id.btnLoading);
        Button btnLoadingSub = findViewById(R.id.btnLoadingWithSub);

        btnLoading.setOnClickListener(v -> {
            PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance("Cargando datos...");
            loading.setCancelable(false);
            loading.show(getSupportFragmentManager(), "loading");

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (loading.isAdded()) {
                    loading.dismiss();
                    PenguinToast.showSuccess(this, "Datos cargados");
                }
            }, 3000);
        });

        btnLoadingSub.setOnClickListener(v -> {
            PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance(
                    "Sincronizando...", "Conectando al servidor");
            loading.setCancelable(false);
            loading.show(getSupportFragmentManager(), "loading_sub");

            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    loading.updateSubMessage("Descargando datos..."), 1500);

            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    loading.updateMessages("Finalizando...", "Casi listo"), 3000);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (loading.isAdded()) {
                    loading.dismiss();
                    PenguinToast.showSuccess(this, "¡Sincronización completa!");
                }
            }, 4500);
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  BOTTOM SHEET
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupSheetButtons() {
        Button btnSheet       = findViewById(R.id.btnSheet);
        Button btnSheetColors = findViewById(R.id.btnSheetCustomColors);

        btnSheet.setOnClickListener(v -> {
            new PenguinSheet.Builder()
                    .setTitle("Acciones del registro")
                    .addItem(android.R.drawable.ic_menu_edit,
                            "Editar",
                            () -> PenguinToast.showInfo(this, "Editando registro..."))
                    .addItem(android.R.drawable.ic_menu_share,
                            "Compartir",
                            () -> PenguinToast.showInfo(this, "Compartiendo..."))
                    .addItem(android.R.drawable.ic_menu_delete,
                            "Eliminar",
                            () -> PenguinToast.showError(this, "Registro eliminado"))
                    .build()
                    .show(getSupportFragmentManager(), "bottom_sheet");
        });

        btnSheetColors.setOnClickListener(v -> {
            new PenguinSheet.Builder()
                    .setTitle("Opciones con colores")
                    .addItem(android.R.drawable.ic_menu_edit,
                            "Editar (color cian)",
                            () -> PenguinToast.showInfo(this, "Editar"),
                            R.color.dialog_accent_cyan)
                    .addItem(android.R.drawable.ic_menu_send,
                            "Enviar (color verde)",
                            () -> PenguinToast.showSuccess(this, "Enviado"),
                            R.color.toast_success_accent)
                    .addItem(android.R.drawable.ic_menu_delete,
                            "Eliminar (color rojo)",
                            () -> PenguinToast.showError(this, "Eliminado"),
                            R.color.toast_error_accent)
                    .showCancelButton(true)
                    .build()
                    .show(getSupportFragmentManager(), "bottom_sheet_colors");
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  HAPTIC
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupHapticButtons() {
        Button btnSuccess  = findViewById(R.id.btnHapticSuccess);
        Button btnError    = findViewById(R.id.btnHapticError);
        Button btnWarning  = findViewById(R.id.btnHapticWarning);
        Button btnCritical = findViewById(R.id.btnHapticCritical);

        btnSuccess.setOnClickListener(v -> {
            PenguinHaptic.vibrateSuccess(this);
            PenguinToast.showInfo(this, "Vibración corta (50ms)");
        });

        btnError.setOnClickListener(v -> {
            PenguinHaptic.vibrateError(this);
            PenguinToast.showInfo(this, "Vibración larga (200ms)");
        });

        btnWarning.setOnClickListener(v -> {
            PenguinHaptic.vibrateWarning(this);
            PenguinToast.showInfo(this, "Doble vibración");
        });

        btnCritical.setOnClickListener(v -> {
            PenguinHaptic.vibrateCritical(this);
            PenguinToast.showInfo(this, "Vibración triple — crítico");
        });
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  FACHADA PenguinUI — mismo resultado, API unificada
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupFacadeButtons() {
        Button btnFacadeToast  = findViewById(R.id.btnFacadeToast);
        Button btnFacadeDialog = findViewById(R.id.btnFacadeDialog);
        Button btnFacadeSheet  = findViewById(R.id.btnFacadeSheet);

        btnFacadeToast.setOnClickListener(v ->
                PenguinUI.success(this, "Desde la fachada PenguinUI.success()"));

        btnFacadeDialog.setOnClickListener(v ->
                PenguinUI.confirmDialog("¿Continuar?", "Usando PenguinUI.confirmDialog()")
                        .setOnConfirmListener(() ->
                                PenguinUI.success(this, "Confirmado desde la fachada"))
                        .show(getSupportFragmentManager(), "facade_dialog"));

        btnFacadeSheet.setOnClickListener(v ->
                PenguinUI.sheet("Menú desde PenguinUI.sheet()")
                        .addItem(android.R.drawable.ic_menu_info_details,
                                "Opción A",
                                () -> PenguinUI.info(this, "Opción A seleccionada"))
                        .addItem(android.R.drawable.ic_menu_preferences,
                                "Opción B",
                                () -> PenguinUI.warning(this, "Opción B seleccionada"))
                        .build()
                        .show(getSupportFragmentManager(), "facade_sheet"));
    }
}
