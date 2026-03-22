package com.yhonam.penguinui;

/**
 * PenguinSheetItem - Modelo de datos para cada opción del PenguinSheet
 *
 * Representa una fila del menú bottom sheet con ícono, texto y acción.
 *
 * Uso:
 *   new PenguinSheetItem(R.drawable.ic_edit, "Editar", () -> editarRegistro())
 *   new PenguinSheetItem(R.drawable.ic_delete, "Eliminar", this::borrar, R.color.red)
 */
public class PenguinSheetItem {

    private final int      iconResId;
    private final String   text;
    private final Runnable action;
    private final int      iconColor;

    /**
     * Item con color de ícono por defecto (cian de Penguin UI)
     *
     * @param iconResId Resource ID del ícono
     * @param text      Texto descriptivo
     * @param action    Acción a ejecutar al seleccionar
     */
    public PenguinSheetItem(int iconResId, String text, Runnable action) {
        this(iconResId, text, action, -1);
    }

    /**
     * Item con color de ícono personalizado
     *
     * @param iconResId Resource ID del ícono
     * @param text      Texto descriptivo
     * @param action    Acción a ejecutar al seleccionar
     * @param iconColor Resource ID del color del ícono (-1 para usar el default)
     */
    public PenguinSheetItem(int iconResId, String text, Runnable action, int iconColor) {
        this.iconResId = iconResId;
        this.text      = text;
        this.action    = action;
        this.iconColor = iconColor;
    }

    public int      getIconResId()     { return iconResId; }
    public String   getText()          { return text; }
    public Runnable getAction()        { return action; }
    public int      getIconColor()     { return iconColor; }
    public boolean  hasCustomColor()   { return iconColor != -1; }
}
