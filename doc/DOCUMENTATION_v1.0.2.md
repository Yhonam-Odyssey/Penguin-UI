# 🐧 Penguin UI — Documentación de Uso v1.0.2

Guía completa para desarrolladores sobre cómo implementar cada componente de la librería PenguinUI.

---

## Tabla de Contenidos

1. [PenguinUI (Fachada)](#penguinui-fachada)
2. [PenguinToast](#penguintoast)
3. [PenguinToastQueue](#penguintoastqueue)
4. [PenguinDialog](#penguindialog)
5. [PenguinInfoDialog](#penguininfodialog)
6. [PenguinLoadingDialog](#penguinloadingdialog)
7. [PenguinSheet](#penguinsheet)
8. [PenguinHaptic](#penguinhaptic)
9. [PenguinTheme](#penguintheme)
10. [Permisos](#permisos)

---

## PenguinUI (Fachada)

### Descripción
Punto de entrada único que proporciona métodos快捷 para acceder a todas las funcionalidades de la librería. Recomendado para uso rápido.

### Cuándo usarlo
- Cuando necesitas una API simple y unificada
- Para operaciones frecuentes que no requieren personalización

### Importación
```java
import com.yhonam.penguinui.PenguinUI;
```

### Métodos de Toasts

#### PenguinUI.success(context, message)
Muestra toast de éxito (verde).

**Parámetros:**
- `context` (Context): Contexto de la Activity - usar `NombreActivity.this`
- `message` (String): Mensaje a mostrar

**Ejemplo:**
```java
PenguinUI.success(MainActivity.this, "Guardado correctamente");
```

---

#### PenguinUI.success(context, title, message)
Muestra toast de éxito con título personalizado.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `title` (String): Título personalizado
- `message` (String): Mensaje a mostrar

**Ejemplo:**
```java
PenguinUI.success(MainActivity.this, "¡Listo!", "Tu perfil fue actualizado");
```

---

#### PenguinUI.error(context, message)
Muestra toast de error (rojo).

**Ejemplo:**
```java
PenguinUI.error(MainActivity.this, "Error de conexión");
```

---

#### PenguinUI.error(context, title, message)
Muestra toast de error con título personalizado.

**Ejemplo:**
```java
PenguinUI.error(MainActivity.this, "Error crítico", "No se pudo guardar el archivo");
```

---

#### PenguinUI.warning(context, message)
Muestra toast de advertencia (amarillo).

**Ejemplo:**
```java
PenguinUI.warning(MainActivity.this, "El espacio de almacenamiento está bajo");
```

---

#### PenguinUI.warning(context, title, message)
Muestra toast de advertencia con título personalizado.

**Ejemplo:**
```java
PenguinUI.warning(MainActivity.this, "Advertencia", "Tu sesión expirará pronto");
```

---

#### PenguinUI.info(context, message)
Muestra toast de información (cian).

**Ejemplo:**
```java
PenguinUI.info(MainActivity.this, "Versión 1.0.2 disponible");
```

---

#### PenguinUI.info(context, title, message)
Muestra toast de información con título personalizado.

**Ejemplo:**
```java
PenguinUI.info(MainActivity.this, "Nueva actualización", "Versión 1.0.2 disponible");
```

---

### Métodos de Diálogos

#### PenguinUI.confirmDialog(title, message)
Crea diálogo de confirmación (Confirmar / Cancelar). Retorna `PenguinDialog`.

**Parámetros:**
- `title` (String): Título del diálogo
- `message` (String): Mensaje del diálogo

**Retorna:** `PenguinDialog` - usar `.setOnConfirmListener()` y `.show()`

**Ejemplo:**
```java
PenguinUI.confirmDialog("¿Guardar cambios?", "Se guardarán todos los cambios.")
    .setOnConfirmListener(() -> {
        // Acción al confirmar
        guardarCambios();
    })
    .show(getSupportFragmentManager(), "confirm_dialog");
```

---

#### PenguinUI.deleteDialog(title, message)
Crea diálogo de eliminación (Eliminar / Cancelar). Retorna `PenguinDialog`.

**Ejemplo:**
```java
PenguinUI.deleteDialog("¿Eliminar registro?", "Esta acción no se puede deshacer.")
    .setOnConfirmListener(() -> eliminarRegistro())
    .show(getSupportFragmentManager(), "delete_dialog");
```

---

#### PenguinUI.warningDialog(title, message)
Crea diálogo de advertencia (Aceptar / Cancelar).

**Ejemplo:**
```java
PenguinUI.warningDialog("Advertencia de seguridad", "Tu sesión expirará en 5 minutos.")
    .setOnConfirmListener(() -> renovarSesion())
    .show(getSupportFragmentManager(), "warning_dialog");
```

---

#### PenguinUI.logoutDialog(title, message)
Crea diálogo de cierre de sesión (Sí, cerrar sesión / Cancelar).

**Ejemplo:**
```java
PenguinUI.logoutDialog("Cerrar sesión", "¿Estás seguro de que deseas salir?")
    .setOnConfirmListener(() -> cerrarSesion())
    .show(getSupportFragmentManager(), "logout_dialog");
```

---

#### PenguinUI.infoDialog(title, message)
Crea diálogo informativo con un botón "Aceptar". Retorna `PenguinInfoDialog`.

**Ejemplo:**
```java
PenguinUI.infoDialog("Información", "Datos guardados correctamente.")
    .show(getSupportFragmentManager(), "info_dialog");
```

---

#### PenguinUI.successDialog(title, message)
Crea diálogo de éxito con un botón "Aceptar".

**Ejemplo:**
```java
PenguinUI.successDialog("¡Registro exitoso!", "Tu cuenta ha sido creada correctamente.")
    .show(getSupportFragmentManager(), "success_dialog");
```

---

### Métodos de Loading

#### PenguinUI.loading(message)
Crea diálogo de carga con spinner. Retorna `PenguinLoadingDialog`.

**Parámetros:**
- `message` (String): Mensaje principal

**Retorna:** `PenguinLoadingDialog` - usar `.show()` y `.dismiss()`

**Ejemplo:**
```java
PenguinLoadingDialog loading = PenguinUI.loading("Cargando datos...");
loading.show(getSupportFragmentManager(), "loading");
// ... operación asíncrona ...
loading.dismiss();
```

---

#### PenguinUI.loading(message, subMessage)
Crea diálogo de carga con mensaje y submensaje.

**Parámetros:**
- `message` (String): Mensaje principal
- `subMessage` (String): Mensaje secundario

**Ejemplo:**
```java
PenguinLoadingDialog loading = PenguinUI.loading("Sincronizando...", "Conectando al servidor");
loading.show(getSupportFragmentManager(), "loading");
```

---

### Métodos de Bottom Sheet

#### PenguinUI.sheet(title)
Inicia un builder para crear bottom sheet. Retorna `PenguinSheet.Builder`.

**Parámetros:**
- `title` (String): Título del sheet

**Retorna:** `PenguinSheet.Builder` - encadenar `.addItem()` y usar `.build().show()`

**Ejemplo:**
```java
PenguinUI.sheet("Opciones")
    .addItem(R.drawable.ic_edit, "Editar", () -> editar())
    .addItem(R.drawable.ic_share, "Compartir", () -> compartir())
    .addItem(R.drawable.ic_delete, "Eliminar", () -> eliminar())
    .build()
    .show(getSupportFragmentManager(), "sheet");
```

---

### Métodos de Haptic

#### PenguinUI.hapticSuccess(context)
Vibración corta y suave (50ms) — éxito.

**Parámetros:**
- `context` (Context): Contexto de la Activity

**Ejemplo:**
```java
PenguinUI.hapticSuccess(MainActivity.this);
```

---

#### PenguinUI.hapticError(context)
Vibración larga y fuerte (200ms) — error.

**Ejemplo:**
```java
PenguinUI.hapticError(MainActivity.this);
```

---

#### PenguinUI.hapticWarning(context)
Doble vibración corta — advertencia.

**Ejemplo:**
```java
PenguinUI.hapticWarning(MainActivity.this);
```

---

#### PenguinUI.hapticInfo(context)
Vibración muy corta (30ms) — información.

**Ejemplo:**
```java
PenguinUI.hapticInfo(MainActivity.this);
```

---

## PenguinToast

### Descripción
Notificaciones toast personalizadas con estilos visuales modernos, icono, título y barra de acento de color.

### Cuándo usarlo
- Feedback visual rápido al usuario
- Mensajes de éxito/error/advertencia/info
- Necesitas controlar duración y posición

### Importación
```java
import com.yhonam.penguinui.PenguinToast;
```

### Tipos de Toast
| Tipo | Color de acento | Icono | Uso recomendado |
|------|-----------------|-------|------------------|
| SUCCESS | Verde (#00FF88) | ✓ | Operaciones exitosas |
| ERROR | Rojo (#FF2E63) | ✗ | Errores, fallas |
| WARNING | Amarillo (#FFB703) | ⚠ | Advertencias |
| INFO | Cian (#00D9FF) | ℹ | Información general |

### Constantes de Duración
| Constante | Valor | Equivalente en Toast |
|-----------|-------|---------------------|
| `DURATION_SHORT` | 2 segundos | LENGTH_SHORT |
| `DURATION_NORMAL` | 3 segundos | LENGTH_LONG (corto) |
| `DURATION_LONG` | 5 segundos | LENGTH_LONG |

### Posiciones
Usa constantes de `Gravity`:
- `Gravity.TOP` — parte superior (default)
- `Gravity.CENTER` — centro
- `Gravity.BOTTOM` — parte inferior

---

### Métodos de Éxito

#### PenguinToast.showSuccess(context, message)
Muestra toast de éxito simple.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `message` (String): Mensaje a mostrar

**Ejemplo:**
```java
PenguinToast.showSuccess(MainActivity.this, "Perfil actualizado");
```

---

#### PenguinToast.showSuccess(context, title, message)
Muestra toast de éxito con título personalizado.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `title` (String): Título personalizado
- `message` (String): Mensaje a mostrar

**Ejemplo:**
```java
PenguinToast.showSuccess(MainActivity.this, "¡Listo!", "Tu perfil fue guardado");
```

---

#### PenguinToast.showSuccess(context, message, durationSeconds)
Muestra toast de éxito con duración personalizada.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `message` (String): Mensaje a mostrar
- `durationSeconds` (int): Duración en segundos (2, 3 o 5)

**Ejemplo:**
```java
PenguinToast.showSuccess(MainActivity.this, "Guardado", PenguinToast.DURATION_LONG);
```

---

### Métodos de Error

#### PenguinToast.showError(context, message)
Muestra toast de error simple.

**Ejemplo:**
```java
PenguinToast.showError(MainActivity.this, "Error de conexión");
```

---

#### PenguinToast.showError(context, title, message)
Muestra toast de error con título personalizado.

**Ejemplo:**
```java
PenguinToast.showError(MainActivity.this, "Error crítico", "No se pudo conectar al servidor");
```

---

#### PenguinToast.showError(context, message, durationSeconds)
Muestra toast de error con duración personalizada.

**Ejemplo:**
```java
PenguinToast.showError(MainActivity.this, "Error de red", PenguinToast.DURATION_LONG);
```

---

### Métodos de Advertencia

#### PenguinToast.showWarning(context, message)
Muestra toast de advertencia simple.

**Ejemplo:**
```java
PenguinToast.showWarning(MainActivity.this, "Espacio bajo");
```

---

#### PenguinToast.showWarning(context, title, message)
Muestra toast de advertencia con título personalizado.

**Ejemplo:**
```java
PenguinToast.showWarning(MainActivity.this, "Advertencia", "El espacio de almacenamiento está bajo");
```

---

#### PenguinToast.showWarning(context, message, durationSeconds)
Muestra toast de advertencia con duración personalizada.

**Ejemplo:**
```java
PenguinToast.showWarning(MainActivity.this, "Advertencia", PenguinToast.DURATION_LONG);
```

---

### Métodos de Información

#### PenguinToast.showInfo(context, message)
Muestra toast de información simple.

**Ejemplo:**
```java
PenguinToast.showInfo(MainActivity.this, "Versión 1.0.2");
```

---

#### PenguinToast.showInfo(context, title, message)
Muestra toast de información con título personalizado.

**Ejemplo:**
```java
PenguinToast.showInfo(MainActivity.this, "Nueva versión", "Versión 1.0.2 disponible");
```

---

#### PenguinToast.showInfo(context, message, durationSeconds)
Muestra toast de información con duración personalizada.

**Ejemplo:**
```java
PenguinToast.showInfo(MainActivity.this, "Información", PenguinToast.DURATION_SHORT);
```

---

### Método Principal (Control Total)

#### PenguinToast.show(context, type, title, message, durationSeconds, gravity)
Muestra toast con control completo de todos los parámetros.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `type` (PenguinToast.Type): Tipo de toast (SUCCESS, ERROR, WARNING, INFO)
- `title` (String): Título (null para usar el default del tipo)
- `message` (String): Mensaje a mostrar
- `durationSeconds` (int): Duración en segundos (2, 3 o 5)
- `gravity` (int): Posición (Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)

**Retorna:** void — el toast se muestra directamente

**Ejemplo:**
```java
PenguinToast.show(
    MainActivity.this,
    PenguinToast.Type.SUCCESS,
    "¡Éxito!",
    "Tu operación fue completada",
    PenguinToast.DURATION_NORMAL,
    Gravity.BOTTOM
);
```

---

#### PenguinToast.showCustom(context, type, title, message, gravity, yOffset)
Muestra toast con posición y offset personalizados.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `type` (PenguinToast.Type): Tipo de toast
- `title` (String): Título (null para usar el default del tipo)
- `message` (String): Mensaje a mostrar
- `gravity` (int): Posición (Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)
- `yOffset` (int): Offset vertical en píxeles

**Ejemplo:**
```java
PenguinToast.showCustom(
    MainActivity.this,
    PenguinToast.Type.INFO,
    "Notificación",
    "Mensaje con offset",
    Gravity.TOP,
    100 // 100px desde el borde
);
```

---

## PenguinToastQueue

### Descripción
Sistema de cola de toasts que evita la superposición mostrando los mensajes en secuencia, uno por uno.

### Cuándo usarlo
- Múltiples operaciones successivas que muestran varios toasts
- Cuando necesitas que los mensajes no se superpongan
- Procesos paso a paso con feedback visual

### Importación
```java
import com.yhonam.penguinui.PenguinToastQueue;
```

---

### Métodos de Éxito

#### PenguinToastQueue.showSuccess(context, message)
Encola toast de éxito.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `message` (String): Mensaje a mostrar

**Ejemplo:**
```java
PenguinToastQueue.showSuccess(MainActivity.this, "Paso 1 completado");
```

---

#### PenguinToastQueue.showSuccess(context, title, message)
Encola toast de éxito con título personalizado.

**Ejemplo:**
```java
PenguinToastQueue.showSuccess(MainActivity.this, "Éxito", "Operación completada");
```

---

#### PenguinToastQueue.showSuccess(context, message, withHaptic)
Encola toast de éxito con vibración háptica.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `message` (String): Mensaje a mostrar
- `withHaptic` (boolean): true para incluir vibración

**Ejemplo:**
```java
PenguinToastQueue.showSuccess(MainActivity.this, "Guardado", true);
```

---

### Métodos de Error

#### PenguinToastQueue.showError(context, message)
Encola toast de error.

**Ejemplo:**
```java
PenguinToastQueue.showError(MainActivity.this, "Error de red");
```

---

#### PenguinToastQueue.showError(context, title, message)
Encola toast de error con título personalizado.

**Ejemplo:**
```java
PenguinToastQueue.showError(MainActivity.this, "Error", "No se pudo conectar");
```

---

#### PenguinToastQueue.showError(context, message, withHaptic)
Encola toast de error con vibración háptica.

**Ejemplo:**
```java
PenguinToastQueue.showError(MainActivity.this, "Error crítico", true);
```

---

### Métodos de Advertencia

#### PenguinToastQueue.showWarning(context, message)
Encola toast de advertencia.

**Ejemplo:**
```java
PenguinToastQueue.showWarning(MainActivity.this, "Advertencia");
```

---

#### PenguinToastQueue.showWarning(context, title, message)
Encola toast de advertencia con título personalizado.

**Ejemplo:**
```java
PenguinToastQueue.showWarning(MainActivity.this, "Atención", "Verifica los datos");
```

---

#### PenguinToastQueue.showWarning(context, message, withHaptic)
Encola toast de advertencia con vibración háptica.

**Ejemplo:**
```java
PenguinToastQueue.showWarning(MainActivity.this, "Revisa esto", true);
```

---

### Métodos de Información

#### PenguinToastQueue.showInfo(context, message)
Encola toast de información.

**Ejemplo:**
```java
PenguinToastQueue.showInfo(MainActivity.this, "Procesando...");
```

---

#### PenguinToastQueue.showInfo(context, title, message)
Encola toast de información con título personalizado.

**Ejemplo:**
```java
PenguinToastQueue.showInfo(MainActivity.this, "Info", "Cargando datos");
```

---

#### PenguinToastQueue.showInfo(context, message, withHaptic)
Encola toast de información con vibración háptica.

**Ejemplo:**
```java
PenguinToastQueue.showInfo(MainActivity.this, "Listo", true);
```

---

### Métodos de Control de Cola

#### PenguinToastQueue.clear()
Limpia todos los toasts pendientes de la cola (los que aún no se han mostrado).

**Ejemplo:**
```java
PenguinToastQueue.clear();
```

---

#### PenguinToastQueue.cancelAll()
Cancela todos los toasts, incluyendo el que se está mostrando actualmente.

**Ejemplo:**
```java
PenguinToastQueue.cancelAll();
```

---

#### PenguinToastQueue.getQueueSize()
Retorna el número de toasts pendientes en la cola.

**Retorna:** int — número de elementos en cola

**Ejemplo:**
```java
int tamaño = PenguinToastQueue.getQueueSize();
Log.d("ToastQueue", "Toasts en cola: " + tamaño);
```

---

#### PenguinToastQueue.setDelayBetweenToasts(milliseconds)
Establece el delay entre la aparición de cada toast en la cola.

**Parámetros:**
- `milliseconds` (int): Milisegundos entre toasts

**Valor default:** 800ms

**Ejemplo:**
```java
PenguinToastQueue.setDelayBetweenToasts(1000); // 1 segundo entre toasts
```

---

### Ejemplo Completo de Uso

```java
// Proceso de múltiples pasos
btnProceso.setOnClickListener(v -> {
    PenguinToastQueue.setDelayBetweenToasts(800);
    
    PenguinToastQueue.showSuccess(MainActivity.this, "Paso 1: Datos cargados");
    PenguinToastQueue.showInfo(MainActivity.this, "Paso 2: Procesando...");
    PenguinToastQueue.showWarning(MainActivity.this, "Paso 3: Verificando permisos");
    PenguinToastQueue.showSuccess(MainActivity.this, "Paso 4: ¡Todo listo!", true);
});

// Ver tamaño de cola
int colaActual = PenguinToastQueue.getQueueSize();

// Cancelar si es necesario
if (colaActual > 0) {
    PenguinToastQueue.cancelAll();
}
```

---

## PenguinDialog

### Descripción
Diálogo de confirmación con dos botones (Confirmar / Cancelar). Basado en `DialogFragment`, por lo que sobrevive a cambios de configuración.

### Cuándo usarlo
- Confirmar acciones importantes (eliminar, guardar, salir)
- Solicitar confirmación antes de ejecutar operaciones destructivas
- Diálogos de警告 o cierre de sesión

### Importación
```java
import com.yhonam.penguinui.PenguinDialog;
```

---

### Factory Methods (Métodos de Fábrica)

#### PenguinDialog.confirm(title, message)
Crea diálogo de confirmación genérico.

**Parámetros:**
- `title` (String): Título del diálogo
- `message` (String): Mensaje del diálogo

**Retorna:** `PenguinDialog` — configurar listeners y mostrar

**Botones:** "Confirmar" (azul) / "Cancelar"

**Ejemplo:**
```java
PenguinDialog.confirm("¿Guardar cambios?", "Se guardarán todos los cambios realizados.")
    .setOnConfirmListener(() -> {
        // Acción al confirmar
        guardarCambios();
    })
    .setOnCancelListener(() -> {
        // Acción al cancelar
        cancelar();
    })
    .show(getSupportFragmentManager(), "confirm_dialog");
```

---

#### PenguinDialog.delete(title, message)
Crea diálogo de eliminación.

**Botones:** "Eliminar" (rojo) / "Cancelar"

**Icono:** Papelera

**Ejemplo:**
```java
PenguinDialog.delete("¿Eliminar registro?", "Esta acción no se puede deshacer.")
    .setOnConfirmListener(() -> {
        eliminarRegistro();
    })
    .show(getSupportFragmentManager(), "delete_dialog");
```

---

#### PenguinDialog.warning(title, message)
Crea diálogo de advertencia.

**Botones:** "Aceptar" / "Cancelar"

**Icono:** Advertencia

**Ejemplo:**
```java
PenguinDialog.warning("Advertencia de seguridad", "Tu sesión expirará en 5 minutos.")
    .setOnConfirmListener(() -> {
        renovarSesion();
    })
    .show(getSupportFragmentManager(), "warning_dialog");
```

---

#### PenguinDialog.logout(title, message)
Crea diálogo de cierre de sesión.

**Botones:** "Sí, cerrar sesión" / "Cancelar"

**Icono:** Candado

**Ejemplo:**
```java
PenguinDialog.logout("Cerrar sesión", "¿Estás seguro de que deseas salir?")
    .setOnConfirmListener(() -> {
        cerrarSesion();
        finish();
    })
    .show(getSupportFragmentManager(), "logout_dialog");
```

---

### Método newInstance (Personalizado)

#### PenguinDialog.newInstance(title, message)
Crea diálogo con título y mensaje básicos.

**Parámetros:**
- `title` (String): Título del diálogo
- `message` (String): Mensaje del diálogo

**Retorna:** `PenguinDialog`

**Ejemplo:**
```java
PenguinDialog dialog = PenguinDialog.newInstance("Título", "Mensaje");
dialog.show(getSupportFragmentManager(), "dialog");
```

---

#### PenguinDialog.newInstance(title, message, positiveText, negativeText)
Crea diálogo con textos de botones personalizados.

**Parámetros:**
- `title` (String): Título del diálogo
- `message` (String): Mensaje del diálogo
- `positiveText` (String): Texto del botón positivo
- `negativeText` (String): Texto del botón negativo

**Ejemplo:**
```java
PenguinDialog.newInstance("Confirmar", "¿Proceder?", "Sí", "No")
    .setOnConfirmListener(() -> proceder())
    .show(getSupportFragmentManager(), "custom_dialog");
```

---

### Configuración Adicional

#### setIcon(iconResId)
Establece icono personalizado para el diálogo.

**Parámetros:**
- `iconResId` (int): ID del recurso drawable

**Ejemplo:**
```java
PenguinDialog.confirm("Título", "Mensaje")
    .setIcon(R.drawable.ic_mi_icono)
    .show(getSupportFragmentManager(), "dialog");
```

---

#### showIcon(boolean)
Muestra u oculta el icono del diálogo.

**Parámetros:**
- `show` (boolean): true para mostrar, false para ocultar

**Ejemplo:**
```java
PenguinDialog.confirm("Título", "Mensaje")
    .showIcon(false)
    .show(getSupportFragmentManager(), "dialog");
```

---

#### setOnConfirmListener(OnConfirmListener)
Establece callback para el botón de confirmación.

**Parámetros:**
- `listener` (PenguinDialog.OnConfirmListener): Interface con método `onConfirm()`

**Ejemplo:**
```java
PenguinDialog.OnConfirmListener listener = () -> {
    Log.d("Dialog", "Confirmado");
};

PenguinDialog.confirm("Título", "Mensaje")
    .setOnConfirmListener(listener)
    .show(getSupportFragmentManager(), "dialog");
```

---

#### setOnCancelListener(OnCancelListener)
Establece callback para el botón de cancelación.

**Parámetros:**
- `listener` (PenguinDialog.OnCancelListener): Interface con método `onCancel()`

**Ejemplo:**
```java
PenguinDialog.OnCancelListener listener = () -> {
    Log.d("Dialog", "Cancelado");
};

PenguinDialog.confirm("Título", "Mensaje")
    .setOnCancelListener(listener)
    .show(getSupportFragmentManager(), "dialog");
```

---

### Ejemplo Completo

```java
// Diálogo de eliminación completo
PenguinDialog.delete("¿Eliminar este elemento?", "Esta acción no se puede deshacer.")
    .setIcon(R.drawable.ic_delete)
    .setOnConfirmListener(() -> {
        // Eliminar elemento
        eliminarElemento();
        
        // Mostrar feedback
        PenguinToast.showSuccess(MainActivity.this, "Elemento eliminado");
    })
    .setOnCancelListener(() -> {
        Log.d("Dialog", "Operación cancelada por el usuario");
    })
    .show(getSupportFragmentManager(), "delete_element");
```

---

## PenguinInfoDialog

### Descripción
Diálogo informativo con un único botón "Aceptar". Ideal para mostrar mensajes de éxito, información o advertencias que no requieren respuesta.

### Cuándo usarlo
- Mensajes de operación exitosa
- Información importante que el usuario debe leer
- Confirmación de acciones completadas
- Diálogos de "Acerca de"

### Importación
```java
import com.yhonam.penguinui.PenguinInfoDialog;
```

---

### Factory Methods

#### PenguinInfoDialog.success(title, message)
Crea diálogo de éxito informativo.

**Parámetros:**
- `title` (String): Título del diálogo
- `message` (String): Mensaje del diálogo

**Retorna:** `PenguinInfoDialog`

**Icono:** Check (verde)

**Botón:** "Aceptar"

**Ejemplo:**
```java
PenguinInfoDialog.success("¡Registro exitoso!", "Tu cuenta ha sido creada correctamente.")
    .show(getSupportFragmentManager(), "success_dialog");
```

---

#### PenguinInfoDialog.info(title, message)
Crea diálogo de información.

**Icono:** Información (cian)

**Ejemplo:**
```java
PenguinInfoDialog.info("Acerca de Penguin UI", "Librería de componentes UI modernos.\nVersión 1.0.2")
    .show(getSupportFragmentManager(), "about_dialog");
```

---

#### PenguinInfoDialog.warning(title, message)
Crea diálogo de advertencia informativo.

**Icono:** Advertencia (amarillo)

**Ejemplo:**
```java
PenguinInfoDialog.warning("Permiso requerido", "Esta función necesita acceso a la cámara para funcionar.")
    .show(getSupportFragmentManager(), "warning_dialog");
```

---

### Método newInstance

#### PenguinInfoDialog.newInstance(title, message)
Crea diálogo con configuración básica.

**Parámetros:**
- `title` (String): Título del diálogo
- `message` (String): Mensaje del diálogo

**Retorna:** `PenguinInfoDialog`

**Ejemplo:**
```java
PenguinInfoDialog.newInstance("Título", "Mensaje informativo")
    .show(getSupportFragmentManager(), "info_dialog");
```

---

#### PenguinInfoDialog.newInstance(title, message, okText)
Crea diálogo con texto del botón personalizado.

**Parámetros:**
- `title` (String): Título del diálogo
- `message` (String): Mensaje del diálogo
- `okText` (String): Texto del botón

**Ejemplo:**
```java
PenguinInfoDialog.newInstance("Título", "Mensaje", "Entendido")
    .show(getSupportFragmentManager(), "info_dialog");
```

---

### Configuración Adicional

#### setIcon(iconResId)
Establece icono personalizado.

**Parámetros:**
- `iconResId` (int): ID del recurso drawable

**Ejemplo:**
```java
PenguinInfoDialog.info("Título", "Mensaje")
    .setIcon(R.drawable.ic_mi_icono)
    .show(getSupportFragmentManager(), "dialog");
```

---

#### showIcon(boolean)
Muestra u oculta el icono.

**Parámetros:**
- `show` (boolean): true para mostrar, false para ocultar

**Ejemplo:**
```java
PenguinInfoDialog.info("Título", "Mensaje")
    .showIcon(false)
    .show(getSupportFragmentManager(), "dialog");
```

---

#### setOnDismissListener(OnDismissListener)
Establece callback que se ejecuta cuando el diálogo se cierra.

**Parámetros:**
- `listener` (PenguinInfoDialog.OnDismissListener): Interface con método `onDismiss()`

**Ejemplo:**
```java
PenguinInfoDialog.success("¡Completado!", "La operación fue exitosa.")
    .setOnDismissListener(() -> {
        Log.d("InfoDialog", "Diálogo cerrado");
        // Continuar con siguiente pantalla
    })
    .show(getSupportFragmentManager(), "success_dialog");
```

---

### Actualización Dinámica

El `PenguinInfoDialog` permite actualizar su contenido después de ser mostrado.

#### updateMessage(message)
Actualiza el mensaje del diálogo.

**Parámetros:**
- `message` (String): Nuevo mensaje

**Ejemplo:**
```java
PenguinInfoDialog dialog = PenguinInfoDialog.info("Procesando", "Iniciando...");
dialog.show(getSupportFragmentManager(), "loading_dialog");

// Actualizar después de un tiempo
new Handler().postDelayed(() -> {
    dialog.updateMessage("Completado");
}, 2000);
```

---

#### updateTitle(title)
Actualiza el título del diálogo.

**Parámetros:**
- `title` (String): Nuevo título

**Ejemplo:**
```java
PenguinInfoDialog dialog = PenguinInfoDialog.info("Cargando", "Por favor espere...");
dialog.updateTitle("Finalizado");
```

---

#### updateIcon(iconResId)
Actualiza el icono del diálogo.

**Parámetros:**
- `iconResId` (int): ID del recurso drawable

**Ejemplo:**
```java
PenguinInfoDialog dialog = PenguinInfoDialog.info("Título", "Mensaje");
dialog.updateIcon(R.drawable.ic_success);
```

---

### Ejemplo Completo

```java
// Mostrar diálogo de éxito con callback
PenguinInfoDialog.success("¡Compra realizada!", "Tu pedido ha sido confirmado.")
    .setOnDismissListener(() -> {
        // Navegar a la pantalla de pedidos
        startActivity(new Intent(this, PedidosActivity.class));
        finish();
    })
    .show(getSupportFragmentManager(), "compra_exitosa");

// Diálogo dinámico con actualización
PenguinInfoDialog dialog = PenguinInfoDialog.info("Sincronizando", "Conectando al servidor...");
dialog.show(getSupportFragmentManager(), "sync");

new Handler(Looper.getMainLooper()).postDelayed(() -> {
    dialog.updateTitle("Descargando");
    dialog.updateMessage("30% completado");
}, 1500);

new Handler(Looper.getMainLooper()).postDelayed(() -> {
    dialog.updateTitle("Finalizado");
    dialog.updateMessage("¡Sincronización completa!");
    dialog.updateIcon(R.drawable.ic_success);
    
    // Cerrar automáticamente después de 2 segundos
    new Handler(Looper.getMainLooper()).postDelayed(() -> dialog.dismiss(), 2000);
}, 4000);
```

---

## PenguinLoadingDialog

### Descripción
Diálogo de carga con spinner animado. Permite actualizar el mensaje y submensaje dinámicamente desde cualquier hilo (thread-safe).

### Cuándo usarlo
- Operaciones asíncronas que toman tiempo (API, descarga de archivos)
- Procesos de sincronización
- Cualquier operación que necesite feedback de progreso

### Importación
```java
import com.yhonam.penguinui.PenguinLoadingDialog;
```

---

### Creación del Diálogo

#### PenguinLoadingDialog.newInstance(message)
Crea diálogo de carga con mensaje principal.

**Parámetros:**
- `message` (String): Mensaje a mostrar

**Retorna:** `PenguinLoadingDialog`

**Ejemplo:**
```java
PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance("Cargando datos...");
loading.show(getSupportFragmentManager(), "loading");
```

---

#### PenguinLoadingDialog.newInstance(message, subMessage)
Crea diálogo de carga con mensaje y submensaje.

**Parámetros:**
- `message` (String): Mensaje principal
- `subMessage` (String): Mensaje secundario

**Ejemplo:**
```java
PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance(
    "Sincronizando...", 
    "Conectando al servidor"
);
loading.show(getSupportFragmentManager(), "loading");
```

---

### Configuración

#### setCancelable(boolean)
Establece si el diálogo es cancelable con el botón Atrás.

**Parámetros:**
- `cancelable` (boolean): true para permitir cancelación, false para obligar espera

**Default:** true

**Ejemplo:**
```java
PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance("Procesando...");
loading.setCancelable(false); // Usuario debe esperar
loading.show(getSupportFragmentManager(), "loading");
```

---

### Actualización de Mensajes

Los métodos de actualización son thread-safe y pueden llamarse desde cualquier hilo.

#### updateMessage(message)
Actualiza el mensaje principal.

**Parámetros:**
- `message` (String): Nuevo mensaje principal

**Ejemplo:**
```java
loading.updateMessage("Descargando archivos...");
```

---

#### updateSubMessage(subMessage)
Actualiza el mensaje secundario.

**Parámetros:**
- `subMessage` (String): Nuevo mensaje secundario

**Ejemplo:**
```java
loading.updateSubMessage("50% completado");
```

---

#### updateMessages(message, subMessage)
Actualiza ambos mensajes (principal y secundario) simultáneamente.

**Parámetros:**
- `message` (String): Nuevo mensaje principal
- `subMessage` (String): Nuevo mensaje secundario

**Ejemplo:**
```java
loading.updateMessages("Finalizando...", "Casi listo");
```

---

### Cierre del Diálogo

#### dismiss()
Cierra el diálogo de carga.

**Nota:** Verificar que el diálogo esté añadido antes de cerrar (`isAdded()`).

**Ejemplo:**
```java
// En un hilo secundario (background)
loading.dismiss();

// Recomendado: verificar antes de cerrar desde otro hilo
if (loading.isAdded()) {
    loading.dismiss();
}
```

---

### Ejemplo Completo de Uso

```java
// Mostrar loading
PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance(
    "Sincronizando...", 
    "Conectando al servidor"
);
loading.setCancelable(false);
loading.show(getSupportFragmentManager(), "loading");

// Simular proceso con actualizaciones
new Handler(Looper.getMainLooper()).postDelayed(() -> {
    loading.updateSubMessage("Descargando datos...");
}, 1500);

new Handler(Looper.getMainLooper()).postDelayed(() -> {
    loading.updateMessages("Procesando...", "Analizando información");
}, 3000);

new Handler(Looper.getMainLooper()).postDelayed(() -> {
    loading.updateMessages("Finalizando...", "Guardando cambios");
}, 4500);

// Cerrar cuando termine
new Handler(Looper.getMainLooper()).postDelayed(() -> {
    if (loading.isAdded()) {
        loading.dismiss();
        PenguinToast.showSuccess(MainActivity.this, "¡Sincronización completa!");
    }
}, 5500);
```

---

### Ejemplo con AsyncTask (obsoleto) / ExecutorService

```java
// Con ExecutorService
PenguinLoadingDialog loading = PenguinLoadingDialog.newInstance("Procesando...");
loading.show(getSupportFragmentManager(), "loading");

ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(() -> {
    try {
        // Operación en background
        cargarDatos();
        
        // Actualizar desde background (thread-safe)
        loading.updateMessage("Guardando...");
        guardarDatos();
        
    } finally {
        // Cerrar en main thread
        new Handler(Looper.getMainLooper()).post(() -> {
            if (loading.isAdded()) {
                loading.dismiss();
            }
        });
    }
});
```

---

## PenguinSheet

### Descripción
Menú de acciones tipo bottom sheet que se desliza desde la parte inferior de la pantalla. Ideal para mostrar opciones contextuales.

### Cuándo usarlo
- Opciones contextuales de un elemento (editar, eliminar, compartir)
- Menú de acciones rápido
- Cuando hay muchas opciones y quieres ahorrar espacio en pantalla

### Importación
```java
import com.yhonam.penguinui.PenguinSheet;
import com.yhonam.penguinui.PenguinSheetItem;
```

---

### Uso con Builder (Recomendado)

#### new PenguinSheet.Builder()
Inicia un builder para crear el bottom sheet.

**Retorna:** `PenguinSheet.Builder`

**Ejemplo:**
```java
new PenguinSheet.Builder()
    .setTitle("Opciones")
    .addItem(R.drawable.ic_edit, "Editar", () -> editar())
    .addItem(R.drawable.ic_delete, "Eliminar", () -> eliminar())
    .build()
    .show(getSupportFragmentManager(), "sheet");
```

---

#### setTitle(title)
Establece el título del bottom sheet.

**Parámetros:**
- `title` (String): Título a mostrar

**Ejemplo:**
```java
new PenguinSheet.Builder()
    .setTitle("Acciones del registro")
    // ...
```

---

#### addItem(iconResId, text, action)
Agrega un item al sheet con acción Runnable.

**Parámetros:**
- `iconResId` (int): ID del recurso drawable para el icono
- `text` (String): Texto del item
- `action` (Runnable): Acción a ejecutar al presionar

**Ejemplo:**
```java
.addItem(R.drawable.ic_edit, "Editar", () -> {
    // Acción
    editarRegistro();
})
```

---

#### addItem(iconResId, text, action, iconColorResId)
Agrega un item con color de icono personalizado.

**Parámetros:**
- `iconResId` (int): ID del recurso drawable para el icono
- `text` (String): Texto del item
- `action` (Runnable): Acción a ejecutar
- `iconColorResId` (int): ID del recurso de color

**Ejemplo:**
```java
.addItem(R.drawable.ic_delete, "Eliminar", () -> eliminar(), R.color.red)
```

---

#### showCancelButton(boolean)
Muestra u oculta el botón de cancelar.

**Parámetros:**
- `show` (boolean): true para mostrar, false para ocultar

**Default:** true

**Ejemplo:**
```java
.showCancelButton(false)
```

---

#### build()
Construye el `PenguinSheet` desde el builder.

**Retorna:** `PenguinSheet`

**Ejemplo:**
```java
PenguinSheet sheet = builder.build();
sheet.show(getSupportFragmentManager(), "sheet");
```

---

### Uso Directo con newInstance

#### PenguinSheet.newInstance(title, items)
Crea bottom sheet con título y lista de items.

**Parámetros:**
- `title` (String): Título del sheet
- `items` (List<PenguinSheetItem>): Lista de items

**Retorna:** `PenguinSheet`

**Ejemplo:**
```java
List<PenguinSheetItem> items = new ArrayList<>();
items.add(new PenguinSheetItem(R.drawable.ic_edit, "Editar", () -> editar()));
items.add(new PenguinSheetItem(R.drawable.ic_delete, "Eliminar", () -> eliminar()));

PenguinSheet.newInstance("Opciones", items)
    .show(getSupportFragmentManager(), "sheet");
```

---

#### PenguinSheet.newInstance(title, items, showCancelButton)
Crea bottom sheet con opción de mostrar/ocultar botón cancelar.

**Parámetros:**
- `title` (String): Título del sheet
- `items` (List<PenguinSheetItem>): Lista de items
- `showCancelButton` (boolean): true para mostrar botón cancelar

**Ejemplo:**
```java
PenguinSheet.newInstance("Opciones", items, false)
    .show(getSupportFragmentManager(), "sheet");
```

---

### PenguinSheetItem

#### Constructor básico
```java
new PenguinSheetItem(int iconResId, String text, Runnable action)
```

**Parámetros:**
- `iconResId` (int): ID del recurso drawable
- `text` (String): Texto del item
- `action` (Runnable): Acción a ejecutar

---

#### Constructor con color
```java
new PenguinSheetItem(int iconResId, String text, Runnable action, int iconColorResId)
```

**Parámetros:**
- `iconResId` (int): ID del recurso drawable
- `text` (String): Texto del item
- `action` (Runnable): Acción a ejecutar
- `iconColorResId` (int): ID del recurso de color para el icono

---

### Modificación Dinámica

El sheet permite agregar/eliminar items después de creado.

#### addItem(PenguinSheetItem item)
Agrega un item al sheet.

**Parámetros:**
- `item` (PenguinSheetItem): Item a agregar

**Ejemplo:**
```java
sheet.addItem(new PenguinSheetItem(R.drawable.ic_share, "Compartir", () -> compartir()));
```

---

#### addItems(List<PenguinSheetItem> newItems)
Agrega múltiples items.

**Ejemplo:**
```java
List<PenguinSheetItem> nuevos = new ArrayList<>();
nuevos.add(new PenguinSheetItem(R.drawable.ic_a, "Opción A", () -> opcionA()));
nuevos.add(new PenguinSheetItem(R.drawable.ic_b, "Opción B", () -> opcionB()));
sheet.addItems(nuevos);
```

---

#### removeItem(int position)
Elimina un item en la posición especificada.

**Parámetros:**
- `position` (int): Índice del item a eliminar (0-based)

**Ejemplo:**
```java
sheet.removeItem(2); // Elimina el tercer item
```

---

#### clearItems()
Elimina todos los items.

**Ejemplo:**
```java
sheet.clearItems();
```

---

#### updateTitle(String title)
Actualiza el título del sheet.

**Parámetros:**
- `title` (String): Nuevo título

**Ejemplo:**
```java
sheet.updateTitle("Nuevas opciones");
```

---

#### showCancelButton(boolean)
Muestra u oculta el botón cancelar dinámicamente.

**Parámetros:**
- `show` (boolean): true para mostrar, false para ocultar

**Ejemplo:**
```java
sheet.showCancelButton(false);
```

---

### Ejemplo Completo

```java
// Bottom sheet básico
new PenguinSheet.Builder()
    .setTitle("Acciones del registro")
    .addItem(android.R.drawable.ic_menu_edit, "Editar", () -> {
        PenguinToast.showInfo(MainActivity.this, "Editando...");
        iniciarEdicion();
    })
    .addItem(android.R.drawable.ic_menu_share, "Compartir", () -> {
        PenguinToast.showInfo(MainActivity.this, "Compartiendo...");
        compartirRegistro();
    })
    .addItem(android.R.drawable.ic_menu_delete, "Eliminar", () -> {
        // Mostrar diálogo de confirmación
        PenguinDialog.delete("¿Eliminar?", "Esta acción no se puede deshacer.")
            .setOnConfirmListener(() -> {
                eliminarRegistro();
                PenguinToast.showSuccess(MainActivity.this, "Eliminado");
            })
            .show(getSupportFragmentManager(), "delete");
    })
    .showCancelButton(true)
    .build()
    .show(getSupportFragmentManager(), "actions_sheet");

// Bottom sheet con colores personalizados
new PenguinSheet.Builder()
    .setTitle("Opciones con colores")
    .addItem(android.R.drawable.ic_menu_edit, "Editar (cian)", () -> editar(), R.color.cyan)
    .addItem(android.R.drawable.ic_menu_send, "Enviar (verde)", () -> enviar(), R.color.green)
    .addItem(android.R.drawable.ic_menu_delete, "Eliminar (rojo)", () -> eliminar(), R.color.red)
    .build()
    .show(getSupportFragmentManager(), "colored_sheet");
```

---

## PenguinHaptic

### Descripción
Sistema de retroalimentación háptica (vibración) que proporciona diferentes patrones según el tipo de feedback: éxito, error, advertencia, etc.

### Cuándo usarlo
- Confirmación de acciones exitosas
- Feedback de errores
- Notificaciones importantes
- Mejora de UX en interacciones táctiles

### Importación
```java
import com.yhonam.penguinui.PenguinHaptic;
```

### Compatibilidad
- **API mínima:** 24 (Android 7.0 Nougat)
- Compatible con versiones superiores

---

### Métodos Predefinidos

#### PenguinHaptic.vibrateSuccess(context)
Vibración corta y suave (50ms) — para operaciones exitosas.

**Parámetros:**
- `context` (Context): Contexto de la Activity

**Patrón:** Una vibración de 50ms

**Ejemplo:**
```java
PenguinHaptic.vibrateSuccess(MainActivity.this);
```

---

#### PenguinHaptic.vibrateError(context)
Vibración larga y fuerte (200ms) — para errores.

**Patrón:** Una vibración de 200ms

**Ejemplo:**
```java
PenguinHaptic.vibrateError(MainActivity.this);
```

---

#### PenguinHaptic.vibrateWarning(context)
Doble vibración corta — para advertencias.

**Patrón:** Vibración 75ms + pausa 100ms + vibración 75ms

**Ejemplo:**
```java
PenguinHaptic.vibrateWarning(MainActivity.this);
```

---

#### PenguinHaptic.vibrateInfo(context)
Vibración muy corta (30ms) — para información.

**Patrón:** Una vibración de 30ms

**Ejemplo:**
```java
PenguinHaptic.vibrateInfo(MainActivity.this);
```

---

#### PenguinHaptic.vibrateConfirm(context)
Vibración de confirmación — para diálogos.

**Patrón:** Vibración de 100ms

**Ejemplo:**
```java
PenguinHaptic.vibrateConfirm(MainActivity.this);
```

---

#### PenguinHaptic.vibrateCancel(context)
Vibración de cancelación — tres vibraciones cortas.

**Patrón:** 50ms + pausa 50ms + 50ms + pausa 50ms + 50ms

**Ejemplo:**
```java
PenguinHaptic.vibrateCancel(MainActivity.this);
```

---

#### PenguinHaptic.vibrateCritical(context)
Vibración de error crítico — triple vibración.

**Patrón:** 150ms + pausa 50ms + 150ms + pausa 50ms + 150ms

**Ejemplo:**
```java
PenguinHaptic.vibrateCritical(MainActivity.this);
```

---

### Método por Tipo de Toast

#### PenguinHaptic.vibrate(context, type)
Vibra según el tipo de toast especificado.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `type` (PenguinToast.Type): Tipo de toast (SUCCESS, ERROR, WARNING, INFO)

**Mapeo:**
- SUCCESS → vibrateSuccess
- ERROR → vibrateError
- WARNING → vibrateWarning
- INFO → vibrateInfo

**Ejemplo:**
```java
PenguinHaptic.vibrate(MainActivity.this, PenguinToast.Type.SUCCESS);
```

---

### Métodos Personalizados

#### PenguinHaptic.vibratePattern(context, pattern, repeat)
Vibración con patrón personalizado.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `pattern` (long[]): Array de tiempos [delay, duración, delay, duración, ...]
- `repeat` (int): Índice de repetición (-1 para no repetir)

**Ejemplo:**
```java
// Vibración: esperar 100ms, vibrar 200ms, esperar 100ms, vibrar 200ms
PenguinHaptic.vibratePattern(MainActivity.this, new long[]{100, 200, 100, 200}, -1);
```

---

#### PenguinHaptic.vibrateDuration(context, duration)
Vibración de duración única personalizada.

**Parámetros:**
- `context` (Context): Contexto de la Activity
- `duration` (long): Duración en milisegundos

**Ejemplo:**
```java
PenguinHaptic.vibrateDuration(MainActivity.this, 150); // 150ms
```

---

### Métodos de Control

#### PenguinHaptic.cancel(context)
Cancela cualquier vibración en curso.

**Parámetros:**
- `context` (Context): Contexto de la Activity

**Ejemplo:**
```java
PenguinHaptic.cancel(MainActivity.this);
```

---

#### PenguinHaptic.hasVibrator(context)
Verifica si el dispositivo tiene vibrador.

**Parámetros:**
- `context` (Context): Contexto de la Activity

**Retorna:** boolean — true si el dispositivo puede vibrar

**Ejemplo:**
```java
if (PenguinHaptic.hasVibrator(MainActivity.this)) {
    PenguinHaptic.vibrateSuccess(MainActivity.this);
}
```

---

### Ejemplo Completo

```java
// Uso simple con toast
btnSuccess.setOnClickListener(v -> {
    PenguinHaptic.vibrateSuccess(MainActivity.this);
    PenguinToast.showSuccess(MainActivity.this, "¡Guardado!");
});

btnError.setOnClickListener(v -> {
    PenguinHaptic.vibrateError(MainActivity.this);
    PenguinToast.showError(MainActivity.this, "Error de conexión");
});

// Verificar compatibilidad antes de vibrar
if (PenguinHaptic.hasVibrator(this)) {
    PenguinHaptic.vibrateWarning(this);
}

// Patrón personalizado para notificación especial
PenguinHaptic.vibratePattern(
    MainActivity.this, 
    new long[]{0, 100, 50, 100, 50, 100}, // pausa, vib, pausa, vib, pausa, vib
    -1
);
```

---

## PenguinTheme

### Descripción
Sistema centralizado de personalización que permite configurar el aspecto visual de todos los componentes de PenguinUI (Toast, Dialog, Loading, Sheet) desde un único punto.

### Novedad v1.0.2
Esta es la nueva característica de la versión 1.0.2.

### Cuándo usarlo
- Necesitas un estilo consistente en toda la app
- Quieres personalizar colores, bordes y transparencia
- Aplicar tema oscuro o modo "glass"

### Importación
```java
import com.yhonam.penguinui.PenguinUI;
import com.yhonam.penguinui.PenguinTheme;
```

---

### Presets

#### PenguinTheme.Preset.NEO
Tema oscuro neón con bordes brillantes. Comportamiento idéntico a v1.0.0.

**Características:**
- Fondo oscuro (#0F1923)
- Bordes con brillo
- Colores de acento vibrantes

**Es el preset por defecto** si no se configura ningún tema.

**Ejemplo:**
```java
PenguinUI.setTheme(
    PenguinTheme.builder()
        .preset(PenguinTheme.Preset.NEO)
        .build()
);
```

---

#### PenguinTheme.Preset.GLASS
Fondos semitransparentes que se adaptan a cualquier paleta de colores de la app.

**Características:**
- Fondo con transparencia (75% por defecto)
- Se adapta a cualquier color de acento
- Más limpio y moderno

**Ejemplo:**
```java
PenguinUI.setTheme(
    PenguinTheme.builder()
        .preset(PenguinTheme.Preset.GLASS)
        .build()
);
```

---

### Radio de Esquinas

#### PenguinTheme.Radius.SHARP
Bordes rectos (4dp). Apariencia más formal/industrial.

**Ejemplo:**
```java
PenguinTheme.builder()
    .preset(PenguinTheme.Preset.NEO)
    .cornerRadius(PenguinTheme.Radius.SHARP)
    .build()
```

---

#### PenguinTheme.Radius.SOFT
Bordes suaves (12dp). Opción por defecto.

**Ejemplo:**
```java
PenguinTheme.builder()
    .preset(PenguinTheme.Preset.NEO)
    .cornerRadius(PenguinTheme.Radius.SOFT)
    .build()
```

---

#### PenguinTheme.Radius.ROUND
Bordes muy redondeados (24dp). Apariencia moderna/rounded.

**Ejemplo:**
```java
PenguinTheme.builder()
    .preset(PenguinTheme.Preset.NEO)
    .cornerRadius(PenguinTheme.Radius.ROUND)
    .build()
```

---

### Configuración de Colores

#### accentColor(color)
Establece el color de acento que afecta:
- Barra lateral del toast
- Línea decorativa de los diálogos
- Spinner del loading

**Parámetros:**
- `color` (int): Color en formato entero (0xFF000000) o usando ContextCompat

**Ejemplo:**
```java
// Con color de recursos
PenguinTheme.builder()
    .accentColor(ContextCompat.getColor(this, R.color.mi_acento))
    .build()

// Con color directo
PenguinTheme.builder()
    .accentColor(Color.parseColor("#FF6200EE"))
    .build()
```

---

#### backgroundColor(color)
Establece el color de fondo de todos los componentes.

**Parámetros:**
- `color` (int): Color de fondo

**Ejemplo:**
```java
PenguinTheme.builder()
    .backgroundColor(Color.parseColor("#1A1A2E"))
    .build()
```

---

#### textPrimaryColor(color)
Establece el color del texto principal (títulos).

**Parámetros:**
- `color` (int): Color del texto

**Ejemplo:**
```java
PenguinTheme.builder()
    .textPrimaryColor(Color.WHITE)
    .build()
```

---

#### textSecondaryColor(color)
Establece el color del texto secundario (mensajes, subtítulos).

**Parámetros:**
- `color` (int): Color del texto secundario

**Ejemplo:**
```java
PenguinTheme.builder()
    .textSecondaryColor(Color.LTGRAY)
    .build()
```

---

### Transparencia

#### backgroundAlpha(alpha)
Establece la opacidad del fondo (solo tiene efecto en preset GLASS o con backgroundColor configurado).

**Parámetros:**
- `alpha` (float): Valor entre 0.0 (transparente) y 1.0 (opaco)

**Valor por defecto para GLASS:** 0.75

**Ejemplo:**
```java
PenguinTheme.builder()
    .preset(PenguinTheme.Preset.GLASS)
    .backgroundAlpha(0.8f)
    .build()
```

---

### Aplicar Tema

#### PenguinUI.setTheme(theme)
Aplica el tema globalmente a todos los componentes.

**Dónde llamar:** En `Application.onCreate()` o en el primer Activity (solo una vez).

**Ejemplo completo:**
```java
// En Application.java
@Override
public void onCreate() {
    super.onCreate();
    
    PenguinUI.setTheme(
        PenguinTheme.builder()
            .preset(PenguinTheme.Preset.GLASS)
            .accentColor(ContextCompat.getColor(this, R.color.mi_acento))
            .cornerRadius(PenguinTheme.Radius.ROUND)
            .backgroundAlpha(0.8f)
            .build()
    );
}
```

---

#### PenguinUI.resetTheme()
Resetea al tema por defecto (preset NEO, comportamiento v1.0).

**Ejemplo:**
```java
// Volver al tema default
PenguinUI.resetTheme();
```

---

### Utilidad: applyAlpha

#### PenguinTheme.applyAlpha(color, alpha)
Aplica un nivel de transparencia a un color sin modificar su tono.

**Parámetros:**
- `color` (int): Color base (opaco)
- `alpha` (float): Nivel de opacidad (0.0 - 1.0)

**Retorna:** int — Color con el alpha aplicado

**Ejemplo:**
```java
int colorTransparente = PenguinTheme.applyAlpha(Color.RED, 0.5f);
```

---

### Ejemplos de Configuración

#### Ejemplo 1: Modo Glass básico
```java
PenguinUI.setTheme(
    PenguinTheme.builder()
        .preset(PenguinTheme.Preset.GLASS)
        .build()
);
```

---

#### Ejemplo 2: Neo con esquinas redondeadas
```java
PenguinUI.setTheme(
    PenguinTheme.builder()
        .preset(PenguinTheme.Preset.NEO)
        .cornerRadius(PenguinTheme.Radius.ROUND)
        .build()
);
```

---

#### Ejemplo 3: Glass con color de acento personalizado
```java
PenguinUI.setTheme(
    PenguinTheme.builder()
        .preset(PenguinTheme.Preset.GLASS)
        .accentColor(Color.parseColor("#FF6200EE"))
        .backgroundAlpha(0.8f)
        .cornerRadius(PenguinTheme.Radius.SOFT)
        .build()
);
```

---

#### Ejemplo 4: Colores completamente personalizados
```java
PenguinUI.setTheme(
    PenguinTheme.builder()
        .preset(PenguinTheme.Preset.NEO)
        .backgroundColor(Color.parseColor("#1A1A2E"))
        .textPrimaryColor(Color.WHITE)
        .textSecondaryColor(Color.parseColor("#B0B0B0"))
        .accentColor(Color.parseColor("#00D9FF"))
        .cornerRadius(PenguinTheme.Radius.ROUND)
        .build()
);
```

---

## Permisos

### Vibración (requerido solo si usas PenguinHaptic)

Agrega el siguiente permiso en tu `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.VIBRATE"/>
```

### Nota importante
A partir de Android 13 (API 33), el permiso de vibración se otorga automáticamente. No necesitas solicitarlo en tiempo de ejecución.

---

## Requisitos del Sistema

| Requisito | Valor |
|-----------|-------|
| minSdk | 24 (Android 7.0 Nougat) |
| targetSdk | 36 |
| Java | 11+ |

### Dependencias automáticas
Las siguientes dependencias se incluyen automáticamente:
- AppCompat
- Material 3
- RecyclerView
- CardView

---

## Notas Importantes

### Context en listeners anónimos
Cuando uses clases anónimas `new View.OnClickListener()`, no uses `this` para el contexto, ya que se referirá al listener. Usa el nombre de la Activity:

```java
// ✅ Correcto
btnCustom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PenguinToast.showSuccess(MainActivity.this, "Mensaje");
    }
});

// ❌ Incorrecto (this se refiere al OnClickListener)
btnCustom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PenguinToast.showSuccess(this, "Mensaje"); // ERROR
    }
});
```

### Recomendación: Usar lambdas
Las lambdas son más limpias y recomendadas:

```java
// ✅ Recomendado
btnCustom.setOnClickListener(v -> 
    PenguinToast.showSuccess(MainActivity.this, "Mensaje")
);
```

---

## Versión

Documentación para **PenguinUI v1.0.2**

---

## Licencia

MIT License — libre para uso personal y comercial.