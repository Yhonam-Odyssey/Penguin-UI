# 🐧 Penguin UI

Librería Android de componentes UI modernos que reemplaza Toast, AlertDialog y BottomSheet nativos con versiones altamente personalizables, con Material Design 3 y tema oscuro integrado.

---

## Instalación vía JitPack

### 1. Agrega el repositorio en `settings.gradle`

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Agrega la dependencia en `build.gradle` de tu módulo

```groovy
dependencies {
    implementation 'com.gitlab.library-yhonam:penguin-ui-android:1.0.0'
}
```

---

## Componentes disponibles

### PenguinUI — Fachada unificada (punto de entrada principal)

```java
// Toasts
PenguinUI.success(context, "Guardado correctamente");
PenguinUI.error(context, "Ocurrió un error");
PenguinUI.warning(context, "Advertencia");
PenguinUI.info(context, "Información");

// Diálogos
PenguinUI.confirmDialog("¿Eliminar?", "No se puede deshacer.")
    .setOnConfirmListener(() -> borrar())
    .show(getSupportFragmentManager(), "confirm");

// Loading
PenguinLoadingDialog loading = PenguinUI.loading("Cargando...");
loading.show(getSupportFragmentManager(), "loading");
// ... operación asíncrona ...
loading.dismiss();

// Bottom Sheet
PenguinUI.sheet("Opciones")
    .addItem(R.drawable.ic_edit, "Editar", () -> editar())
    .addItem(R.drawable.ic_delete, "Eliminar", () -> borrar())
    .build()
    .show(getSupportFragmentManager(), "sheet");

// Haptic
PenguinUI.hapticSuccess(context);
PenguinUI.hapticError(context);
```

---

### PenguinToast

Reemplaza el Toast nativo con estilos visuales modernos, ícono, título y barra de acento.

```java
// Simple
PenguinToast.showSuccess(context, "Perfil actualizado");
PenguinToast.showError(context, "Error de conexión");
PenguinToast.showWarning(context, "Espacio bajo");
PenguinToast.showInfo(context, "Actualización disponible");

// Con título personalizado
PenguinToast.showSuccess(context, "¡Listo!", "Tu perfil fue guardado");

// Con duración personalizada
PenguinToast.showError(context, "Error crítico", PenguinToast.DURATION_LONG);

// Control total
PenguinToast.show(context, PenguinToast.Type.SUCCESS, "Título", "Mensaje",
                  PenguinToast.DURATION_NORMAL, Gravity.BOTTOM);
```

**Tipos:** `SUCCESS` · `ERROR` · `WARNING` · `INFO`
**Duraciones:** `DURATION_SHORT` (2s) · `DURATION_NORMAL` (3s) · `DURATION_LONG` (5s)

---

### PenguinToastQueue

Evita la superposición de toasts. Los encola y muestra uno por uno.

```java
// Encola múltiples toasts — se muestran en orden
PenguinToastQueue.showSuccess(context, "Paso 1 completado");
PenguinToastQueue.showInfo(context, "Procesando paso 2...");
PenguinToastQueue.showSuccess(context, "¡Todo listo!");

// Con vibración háptica
PenguinToastQueue.showError(context, "Error de red", true);

// Control de la cola
PenguinToastQueue.clear();           // limpiar pendientes
PenguinToastQueue.cancelAll();       // cancelar todo
PenguinToastQueue.getQueueSize();    // tamaño de la cola
PenguinToastQueue.setDelayBetweenToasts(800); // ms entre toasts
```

---

### PenguinDialog

Diálogo de confirmación con dos botones (Confirmar / Cancelar). Basado en `DialogFragment`.

```java
// Factory methods rápidos
PenguinDialog.confirm("¿Guardar?", "Se guardarán tus cambios.")
    .setOnConfirmListener(() -> guardar())
    .setOnCancelListener(() -> cancelar())
    .show(getSupportFragmentManager(), "dialog");

PenguinDialog.delete("¿Eliminar?", "Esta acción no se puede deshacer.")
    .setOnConfirmListener(() -> eliminar())
    .show(getSupportFragmentManager(), "delete");

PenguinDialog.warning("Atención", "Tu sesión expirará pronto.")
    .show(getSupportFragmentManager(), "warning");

PenguinDialog.logout("Cerrar sesión", "¿Estás seguro?")
    .setOnConfirmListener(() -> cerrarSesion())
    .show(getSupportFragmentManager(), "logout");

// Personalizado completo
PenguinDialog.newInstance("Título", "Mensaje", "Sí", "No")
    .setIcon(R.drawable.ic_my_icon)
    .setOnConfirmListener(this::onConfirm)
    .show(getSupportFragmentManager(), "custom");
```

---

### PenguinInfoDialog

Diálogo informativo con un único botón "Aceptar".

```java
PenguinInfoDialog.success("¡Registro exitoso!", "Tu cuenta fue creada.")
    .setOnDismissListener(() -> irAlLogin())
    .show(getSupportFragmentManager(), "info");

PenguinInfoDialog.info("Versión", "Penguin UI v1.0.0")
    .show(getSupportFragmentManager(), "about");

PenguinInfoDialog.warning("Permiso requerido",
    "Esta función necesita acceso a la cámara.")
    .show(getSupportFragmentManager(), "permission");

// Actualización dinámica
PenguinInfoDialog dialog = PenguinInfoDialog.info("Título", "Mensaje inicial");
dialog.show(getSupportFragmentManager(), "dynamic");
dialog.updateMessage("Mensaje actualizado");
dialog.updateTitle("Nuevo título");
```

---

### PenguinLoadingDialog

Diálogo de carga con spinner animado. Actualizable desde cualquier hilo.

```java
PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance("Sincronizando...");
loading.setCancelable(false);
loading.show(getSupportFragmentManager(), "loading");

// Actualizar desde hilo de trabajo (thread-safe)
loading.updateMessage("Descargando datos...");
loading.updateSubMessage("Paso 2 de 3");
loading.updateMessages("Finalizando...", "Casi listo");

// Al terminar
loading.dismiss();

// Con submensaje desde el inicio
PenguinLoadingDialog.newInstance("Cargando", "Conectando al servidor")
    .show(getSupportFragmentManager(), "loading");
```

---

### PenguinSheet

Menú de acciones que se desliza desde la parte inferior.

```java
// Con Builder (recomendado)
new PenguinSheet.Builder()
    .setTitle("Acciones")
    .addItem(R.drawable.ic_edit,   "Editar",    () -> editar())
    .addItem(R.drawable.ic_share,  "Compartir", () -> compartir())
    .addItem(R.drawable.ic_delete, "Eliminar",  () -> eliminar(), R.color.red)
    .showCancelButton(true)
    .build()
    .show(getSupportFragmentManager(), "sheet");

// Con lista de items
List<PenguinSheetItem> items = new ArrayList<>();
items.add(new PenguinSheetItem(R.drawable.ic_edit, "Editar", () -> editar()));
items.add(new PenguinSheetItem(R.drawable.ic_delete, "Eliminar", () -> eliminar(), R.color.red));
PenguinSheet.newInstance("Opciones", items).show(getSupportFragmentManager(), "sheet");
```

---

### PenguinHaptic

Retroalimentación táctil. Compatible con Android 7.0+ (API 24+).

```java
PenguinHaptic.vibrateSuccess(context);  // 50ms — suave
PenguinHaptic.vibrateError(context);    // 200ms — fuerte
PenguinHaptic.vibrateWarning(context);  // doble vibración
PenguinHaptic.vibrateInfo(context);     // 30ms — muy suave
PenguinHaptic.vibrateCritical(context); // triple vibración

// Patrón personalizado
PenguinHaptic.vibratePattern(context, new long[]{0, 100, 50, 100}, -1);

// Duración única
PenguinHaptic.vibrateDuration(context, 150);

// Verificar soporte
if (PenguinHaptic.hasVibrator(context)) { ... }
```

---

## Requisitos

- **minSdk:** 24 (Android 7.0 Nougat)
- **targetSdk:** 36
- **Java:** 11+
- Dependencias incluidas automáticamente: AppCompat, Material 3, RecyclerView, CardView

---

## Permisos necesarios

Agrega en tu `AndroidManifest.xml` si usas `PenguinHaptic`:

```xml
<uses-permission android:name="android.permission.VIBRATE"/>
```

---

## Licencia

MIT License — libre para uso personal y comercial.
