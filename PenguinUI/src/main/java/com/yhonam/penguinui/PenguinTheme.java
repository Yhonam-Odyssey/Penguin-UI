package com.yhonam.penguinui;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

/**
 * PenguinTheme - Sistema centralizado de personalización para PenguinUI
 *
 * Configuración única que afecta a todos los componentes (Toast, Dialog, Loading, Sheet).
 * Sin configuración → comportamiento idéntico a v1.0 (preset NEO).
 *
 * Uso recomendado — llamar UNA VEZ en Application.onCreate() o al inicio:
 *
 *   // Estilo Glass (fondos semitransparentes — se adapta a cualquier paleta)
 *   PenguinUI.setTheme(
 *       PenguinTheme.builder()
 *           .preset(PenguinTheme.Preset.GLASS)
 *           .backgroundAlpha(0.8f)
 *           .cornerRadius(PenguinTheme.Radius.SOFT)
 *           .build()
 *   );
 *
 *   // Con color de acento personalizado
 *   PenguinUI.setTheme(
 *       PenguinTheme.builder()
 *           .preset(PenguinTheme.Preset.NEO)
 *           .accentColor(ContextCompat.getColor(this, R.color.mi_color_primario))
 *           .cornerRadius(PenguinTheme.Radius.ROUND)
 *           .build()
 *   );
 *
 *   // Resetear al estilo default
 *   PenguinTheme.reset();
 */
public final class PenguinTheme {

    // ─── Presets ───────────────────────────────────────────────────────────────

    public enum Preset {
        /** Tema oscuro neón con bordes brillantes (default — idéntico a v1.0) */
        NEO,
        /** Fondos semitransparentes — se adapta a cualquier paleta de colores del app */
        GLASS
    }

    // ─── Radio de esquinas ─────────────────────────────────────────────────────

    public enum Radius {
        SHARP,   // 4dp  — bordes rectos
        SOFT,    // 12dp — bordes suaves (default)
        ROUND    // 24dp — bordes muy redondeados
    }

    // ─── Configuración almacenada ──────────────────────────────────────────────

    final Preset  preset;
    final Integer accentColor;        // null = usar default del preset por tipo
    final Integer backgroundColor;    // null = usar default del preset
    final Integer textPrimaryColor;   // null = usar default del preset
    final Integer textSecondaryColor; // null = usar default del preset
    final float   backgroundAlpha;    // 0.0–1.0
    final Radius  cornerRadius;

    // ─── Singleton ─────────────────────────────────────────────────────────────

    private static PenguinTheme instance;

    private PenguinTheme(Builder b) {
        this.preset              = b.preset;
        this.accentColor         = b.accentColor;
        this.backgroundColor     = b.backgroundColor;
        this.textPrimaryColor    = b.textPrimaryColor;
        this.textSecondaryColor  = b.textSecondaryColor;
        this.backgroundAlpha     = b.backgroundAlpha;
        this.cornerRadius        = b.cornerRadius;
    }

    /** Aplicar tema globalmente. Llamar antes del primer uso de cualquier componente. */
    public static void apply(PenguinTheme theme) {
        instance = theme;
    }

    /** Obtener el tema activo. Retorna el preset NEO por defecto si nunca se configuró. */
    public static PenguinTheme get() {
        if (instance == null) instance = builder().build();
        return instance;
    }

    /** Volver al tema default (preset NEO). */
    public static void reset() {
        instance = null;
    }

    // ─── Getters ───────────────────────────────────────────────────────────────

    public Preset getPreset()          { return preset; }
    public boolean isGlass()           { return preset == Preset.GLASS; }
    public float getBackgroundAlpha()  { return backgroundAlpha; }
    public Radius getCornerRadius()    { return cornerRadius; }

    public boolean hasAccentColor()        { return accentColor != null; }
    public boolean hasBackgroundColor()    { return backgroundColor != null; }
    public boolean hasTextPrimaryColor()   { return textPrimaryColor != null; }
    public boolean hasTextSecondaryColor() { return textSecondaryColor != null; }

    public int getAccentColor()        { return accentColor        != null ? accentColor        : Color.CYAN; }
    public int getBackgroundColor()    { return backgroundColor    != null ? backgroundColor    : Color.BLACK; }
    public int getTextPrimaryColor()   { return textPrimaryColor   != null ? textPrimaryColor   : Color.WHITE; }
    public int getTextSecondaryColor() { return textSecondaryColor != null ? textSecondaryColor : Color.LTGRAY; }

    /** Devuelve el radio de esquinas en dp (convertir a px con density). */
    public float getCornerRadiusDp() {
        switch (cornerRadius) {
            case SHARP: return 4f;
            case ROUND: return 24f;
            default:    return 12f;  // SOFT
        }
    }

    /**
     * Aplica un nivel de transparencia a un color sin modificar su tono.
     *
     * @param color Color base (opaco)
     * @param alpha Nivel de opacidad: 0.0 (transparente) — 1.0 (opaco)
     * @return Color con el alpha aplicado
     */
    public static int applyAlpha(@ColorInt int color, float alpha) {
        int a = Math.max(0, Math.min(255, (int)(alpha * 255)));
        return Color.argb(a, Color.red(color), Color.green(color), Color.blue(color));
    }

    // ─── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Preset  preset             = Preset.NEO;
        private Integer accentColor        = null;
        private Integer backgroundColor    = null;
        private Integer textPrimaryColor   = null;
        private Integer textSecondaryColor = null;
        private float   backgroundAlpha    = 1.0f;
        private Radius  cornerRadius       = Radius.SOFT;

        /** Estilo visual base del sistema. */
        public Builder preset(Preset preset) {
            this.preset = preset;
            if (preset == Preset.GLASS && backgroundAlpha == 1.0f) {
                this.backgroundAlpha = 0.75f; // alpha por defecto para GLASS
            }
            return this;
        }

        /**
         * Color de acento — afecta la barra lateral del toast, la línea decorativa
         * de los dialogs y el spinner del loading.
         *
         * Ejemplo: .accentColor(ContextCompat.getColor(context, R.color.mi_color))
         *          .accentColor(Color.parseColor("#FF6200EE"))
         */
        public Builder accentColor(@ColorInt int color) {
            this.accentColor = color;
            return this;
        }

        /**
         * Color de fondo de todos los componentes.
         * En modo GLASS se combina con backgroundAlpha automáticamente.
         */
        public Builder backgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        /** Color del texto principal (títulos de toast, dialog, loading). */
        public Builder textPrimaryColor(@ColorInt int color) {
            this.textPrimaryColor = color;
            return this;
        }

        /** Color del texto secundario (mensajes y subtítulos). */
        public Builder textSecondaryColor(@ColorInt int color) {
            this.textSecondaryColor = color;
            return this;
        }

        /**
         * Opacidad del fondo: 0.0 = completamente transparente, 1.0 = opaco.
         * Solo tiene efecto visual en preset GLASS o cuando backgroundColor está configurado.
         * El preset GLASS establece 0.75f si no se especifica.
         */
        public Builder backgroundAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
            this.backgroundAlpha = Math.max(0f, Math.min(1f, alpha));
            return this;
        }

        /** Radio de las esquinas de todos los componentes. */
        public Builder cornerRadius(Radius radius) {
            this.cornerRadius = radius;
            return this;
        }

        public PenguinTheme build() {
            return new PenguinTheme(this);
        }
    }
}
