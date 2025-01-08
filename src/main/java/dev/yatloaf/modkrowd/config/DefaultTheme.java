package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.text.Text;

public class DefaultTheme {
    public static TextCache formatLatency(int latency) {
        int color = colorLatencyLevel(LatencyLevel.fromLatency(latency));
        return TextCache.of(Text.literal(Util.superscript(latency)).withColor(color));
    }

    public static int colorLatencyLevel(LatencyLevel level) {
        return switch (level) {
            case UNKNOWN -> 0xFF7F7F7F;
            case L1 -> 0xFF00DF00;
            case L2 -> 0xFFBFBF00;
            case L3 -> 0xFFBF7F00;
            case L4 -> 0xFFBF3F00;
            case L5 -> 0xFFBF0000;
        };
    }
}
