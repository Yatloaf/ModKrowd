package dev.yatloaf.modkrowd.cubekrowd.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;

/**
 * Hacky
 */
public class PreviewResult extends CommandSyntaxException {
    public final TextCache value;

    public PreviewResult(TextCache value) {
        super(null, () -> "");
        this.value = value;
    }
}
