package com.yhonam.penguinui;

/**
 * BottomSheetItem - Modelo de datos para cada item del Bottom Sheet
 *
 * Representa una opción individual en el menú Bottom Sheet con:
 * - Ícono
 * - Texto descriptivo
 * - Acción a ejecutar (Runnable)
 */
public class BottomSheetItem {

    private final int iconResId;
    private final String text;
    private final Runnable action;
    private final int iconColor;

    /**
     * Crear un item del bottom sheet
     *
     * @param iconResId Resource ID del ícono
     * @param text Texto descriptivo del item
     * @param action Acción a ejecutar cuando se selecciona el item
     */
    public BottomSheetItem(int iconResId, String text, Runnable action) {
        this(iconResId, text, action, -1); // -1 significa sin color personalizado
    }

    /**
     * Crear un item del bottom sheet con color de ícono personalizado
     *
     * @param iconResId Resource ID del ícono
     * @param text Texto descriptivo del item
     * @param action Acción a ejecutar
     * @param iconColor Resource ID del color del ícono (-1 para default)
     */
    public BottomSheetItem(int iconResId, String text, Runnable action, int iconColor) {
        this.iconResId = iconResId;
        this.text = text;
        this.action = action;
        this.iconColor = iconColor;
    }

    /**
     * Obtener Resource ID del ícono
     */
    public int getIconResId() {
        return iconResId;
    }

    /**
     * Obtener texto descriptivo
     */
    public String getText() {
        return text;
    }

    /**
     * Obtener acción a ejecutar
     */
    public Runnable getAction() {
        return action;
    }

    /**
     * Obtener color del ícono
     * @return Resource ID del color o -1 si no hay color personalizado
     */
    public int getIconColor() {
        return iconColor;
    }

    /**
     * Verificar si tiene color personalizado
     */
    public boolean hasCustomColor() {
        return iconColor != -1;
    }
}
