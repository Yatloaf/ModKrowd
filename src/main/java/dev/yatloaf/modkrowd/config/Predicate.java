package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.cubekrowd.subserver.CreativeSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.CubeKrowdSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MissileWarsSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;

public enum Predicate {
    NEVER("never", Style.EMPTY.withFormatting(Formatting.RED), s -> false),
    CUBEKROWD("cubekrowd", Style.EMPTY.withFormatting(Formatting.GOLD), s -> s instanceof CubeKrowdSubserver),
    CREATIVE("creative", Style.EMPTY.withFormatting(Formatting.AQUA), s -> s instanceof CreativeSubserver),
    MISSILEWARS("missilewars", Style.EMPTY.withFormatting(Formatting.WHITE), s -> s instanceof MissileWarsSubserver),
    ALWAYS("always", Style.EMPTY.withFormatting(Formatting.GREEN), s -> true);

    public final String id;
    public final Text name;
    public final Text tooltip;
    private final java.util.function.Predicate<Subserver> enabled;

    Predicate(String id, Style style, java.util.function.Predicate<Subserver> enabled) {
        this.id = id;
        this.name = Text.translatable("modkrowd.config.predicate." + id).setStyle(style);
        this.tooltip = Text.translatable("modkrowd.config.predicate." + id + ".tooltip");
        this.enabled = enabled;
    }

    public static final Map<String, Predicate> FROM_ID = Util.arrayToMap(values(), item -> item.id, item -> item);

    public boolean enabled(Subserver subserver) {
        return this.enabled.test(subserver);
    }
}
