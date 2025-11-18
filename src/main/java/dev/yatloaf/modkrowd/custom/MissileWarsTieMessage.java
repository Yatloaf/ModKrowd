package dev.yatloaf.modkrowd.custom;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record MissileWarsTieMessage(long redWinTick, long greenWinTick) implements Custom {
    public static final MutableComponent RED =
            Component.translatable("modkrowd.message.missilewars_tie.red").withStyle(ChatFormatting.RED);
    public static final MutableComponent GREEN =
            Component.translatable("modkrowd.message.missilewars_tie.green").withStyle(ChatFormatting.GREEN);
    public static final TextCache SIMULTANEOUS = TextCache.of(Component.translatable(
            "modkrowd.message.missilewars_tie.simultaneous"
    ).setStyle(CKColor.GOLD.style));

    public static MutableComponent sequential(MutableComponent first, MutableComponent last, long deltaTicks) {
        return Component.translatable(
                "modkrowd.message.missilewars_tie.sequential",
                first,
                last,
                deltaTicks / 20,
                "%02d".formatted(deltaTicks % 20 * 5), // Perfect
                deltaTicks
        );
    }

    @Override
    public TextCache appearance() {
        if (this.redWinTick == this.greenWinTick) {
            return SIMULTANEOUS;
        } else {
            long deltaTicks;
            MutableComponent first;
            MutableComponent last;

            if (this.redWinTick < this.greenWinTick) {
                deltaTicks = this.greenWinTick - this.redWinTick;
                first = RED;
                last = GREEN;
            } else {
                deltaTicks = this.redWinTick - this.greenWinTick;
                first = GREEN;
                last = RED;
            }

            return TextCache.of(sequential(first, last, deltaTicks).setStyle(CKColor.DARK_AQUA.style));
        }
    }
}
