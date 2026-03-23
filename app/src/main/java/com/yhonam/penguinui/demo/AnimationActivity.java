package com.yhonam.penguinui.demo;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yhonam.penguinui.PenguinToast;

/**
 * AnimationActivity — Demo de la API de animaciones de PenguinToast.
 *
 * Muestra cómo usar:
 *  1. Shortcuts (showSuccess/showError/etc.) con la animación por defecto.
 *  2. Builder (.success().animation().show()) con cada uno de los 7 Anim.
 *  3. Combinaciones de tipo + animación + opciones adicionales.
 *  4. setDefaultAnimation() para cambiar el default global en runtime.
 */
public class AnimationActivity extends AppCompatActivity {

    private TextView tvCurrentDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        tvCurrentDefault = findViewById(R.id.tvCurrentDefault);

        setupShortcuts();
        setupBuilderAnimations();
        setupCombinations();
        setupGlobalDefault();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  1. SHORTCUTS — animación por defecto global
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupShortcuts() {
        // Usan siempre la animación definida en PenguinToast.setDefaultAnimation()
        // Por defecto: SLIDE_LEFT_RIGHT

        findViewById(R.id.btnDefaultSuccess).setOnClickListener(v ->
                PenguinToast.showSuccess(this, "Operación completada con éxito"));

        findViewById(R.id.btnDefaultError).setOnClickListener(v ->
                PenguinToast.showError(this, "No se pudo conectar al servidor"));

        findViewById(R.id.btnDefaultWarning).setOnClickListener(v ->
                PenguinToast.showWarning(this, "El espacio de almacenamiento está bajo"));

        findViewById(R.id.btnDefaultInfo).setOnClickListener(v ->
                PenguinToast.showInfo(this, "Hay una actualización disponible"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  2. BUILDER — un botón por cada Anim disponible
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupBuilderAnimations() {

        // SLIDE_LEFT_RIGHT — entra izquierda, sale derecha
        findViewById(R.id.btnAnimSlideLR_S).setOnClickListener(v ->
                PenguinToast.success(this, "SLIDE_LEFT_RIGHT")
                        .animation(PenguinToast.Anim.SLIDE_LEFT_RIGHT)
                        .show());

        findViewById(R.id.btnAnimSlideLR_E).setOnClickListener(v ->
                PenguinToast.error(this, "SLIDE_LEFT_RIGHT")
                        .animation(PenguinToast.Anim.SLIDE_LEFT_RIGHT)
                        .show());

        // SLIDE_RIGHT_LEFT — entra derecha, sale izquierda
        findViewById(R.id.btnAnimSlideRL_W).setOnClickListener(v ->
                PenguinToast.warning(this, "SLIDE_RIGHT_LEFT")
                        .animation(PenguinToast.Anim.SLIDE_RIGHT_LEFT)
                        .show());

        findViewById(R.id.btnAnimSlideRL_I).setOnClickListener(v ->
                PenguinToast.info(this, "SLIDE_RIGHT_LEFT")
                        .animation(PenguinToast.Anim.SLIDE_RIGHT_LEFT)
                        .show());

        // SLIDE_TOP — entra y sale desde arriba
        findViewById(R.id.btnAnimTop_S).setOnClickListener(v ->
                PenguinToast.success(this, "SLIDE_TOP")
                        .animation(PenguinToast.Anim.SLIDE_TOP)
                        .show());

        findViewById(R.id.btnAnimTop_E).setOnClickListener(v ->
                PenguinToast.error(this, "SLIDE_TOP")
                        .animation(PenguinToast.Anim.SLIDE_TOP)
                        .show());

        // SLIDE_BOTTOM — entra y sale desde abajo
        findViewById(R.id.btnAnimBottom_W).setOnClickListener(v ->
                PenguinToast.warning(this, "SLIDE_BOTTOM")
                        .animation(PenguinToast.Anim.SLIDE_BOTTOM)
                        .show());

        findViewById(R.id.btnAnimBottom_I).setOnClickListener(v ->
                PenguinToast.info(this, "SLIDE_BOTTOM")
                        .animation(PenguinToast.Anim.SLIDE_BOTTOM)
                        .show());

        // FADE — fundido suave
        findViewById(R.id.btnAnimFade_S).setOnClickListener(v ->
                PenguinToast.success(this, "FADE")
                        .animation(PenguinToast.Anim.FADE)
                        .show());

        findViewById(R.id.btnAnimFade_E).setOnClickListener(v ->
                PenguinToast.error(this, "FADE")
                        .animation(PenguinToast.Anim.FADE)
                        .show());

        // POP — crece desde el centro / encoge al salir
        findViewById(R.id.btnAnimPop_W).setOnClickListener(v ->
                PenguinToast.warning(this, "POP")
                        .animation(PenguinToast.Anim.POP)
                        .show());

        findViewById(R.id.btnAnimPop_I).setOnClickListener(v ->
                PenguinToast.info(this, "POP")
                        .animation(PenguinToast.Anim.POP)
                        .show());

        // BOUNCE — entra desde la izquierda con rebote (overshoot)
        findViewById(R.id.btnAnimBounce_S).setOnClickListener(v ->
                PenguinToast.success(this, "BOUNCE")
                        .animation(PenguinToast.Anim.BOUNCE)
                        .show());

        findViewById(R.id.btnAnimBounce_E).setOnClickListener(v ->
                PenguinToast.error(this, "BOUNCE")
                        .animation(PenguinToast.Anim.BOUNCE)
                        .show());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  3. COMBINACIONES — tipo + animación + opciones adicionales
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupCombinations() {

        // Success + BOUNCE + título personalizado
        findViewById(R.id.btnCombo1).setOnClickListener(v ->
                PenguinToast.success(this, "¡Guardado!", "Los cambios fueron guardados correctamente")
                        .animation(PenguinToast.Anim.BOUNCE)
                        .show());

        // Error + FADE + duración larga
        findViewById(R.id.btnCombo2).setOnClickListener(v ->
                PenguinToast.error(this, "Error de red", "No se pudo conectar al servidor")
                        .animation(PenguinToast.Anim.FADE)
                        .duration(PenguinToast.DURATION_LONG)
                        .show());

        // Warning + POP + título personalizado
        findViewById(R.id.btnCombo3).setOnClickListener(v ->
                PenguinToast.warning(this, "Atención", "El espacio de almacenamiento es bajo")
                        .animation(PenguinToast.Anim.POP)
                        .show());

        // Info + SLIDE_BOTTOM + gravity bottom
        findViewById(R.id.btnCombo4).setOnClickListener(v ->
                PenguinToast.info(this, "Actualización disponible")
                        .animation(PenguinToast.Anim.SLIDE_BOTTOM)
                        .gravity(Gravity.BOTTOM)
                        .show());
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  4. GLOBAL DEFAULT — cambiar el default en runtime
    // ═══════════════════════════════════════════════════════════════════════════

    private void setupGlobalDefault() {

        // SLIDE — vuelve al default original
        ((Button) findViewById(R.id.btnSetDefaultSlide)).setOnClickListener(v -> {
            PenguinToast.setDefaultAnimation(PenguinToast.Anim.SLIDE_LEFT_RIGHT);
            updateDefaultLabel("SLIDE_LEFT_RIGHT");
            PenguinToast.showInfo(this, "Default → SLIDE_LEFT_RIGHT");
        });

        // FADE
        ((Button) findViewById(R.id.btnSetDefaultFade)).setOnClickListener(v -> {
            PenguinToast.setDefaultAnimation(PenguinToast.Anim.FADE);
            updateDefaultLabel("FADE");
            PenguinToast.showInfo(this, "Default → FADE");
        });

        // POP
        ((Button) findViewById(R.id.btnSetDefaultPop)).setOnClickListener(v -> {
            PenguinToast.setDefaultAnimation(PenguinToast.Anim.POP);
            updateDefaultLabel("POP");
            PenguinToast.showInfo(this, "Default → POP");
        });

        // BOUNCE
        ((Button) findViewById(R.id.btnSetDefaultBounce)).setOnClickListener(v -> {
            PenguinToast.setDefaultAnimation(PenguinToast.Anim.BOUNCE);
            updateDefaultLabel("BOUNCE");
            PenguinToast.showInfo(this, "Default → BOUNCE");
        });
    }

    private void updateDefaultLabel(String animName) {
        tvCurrentDefault.setText("Default actual: " + animName);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    //  Restaurar el default al salir de la demo
    // ═══════════════════════════════════════════════════════════════════════════

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PenguinToast.setDefaultAnimation(PenguinToast.Anim.SLIDE_LEFT_RIGHT);
    }
}
