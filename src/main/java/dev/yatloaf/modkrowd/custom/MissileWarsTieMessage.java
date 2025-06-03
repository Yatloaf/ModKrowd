package dev.yatloaf.modkrowd.custom;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public record MissileWarsTieMessage(long redWinTick, long greenWinTick) implements Custom {
    public static final MutableText RED =
            Text.translatable("modkrowd.message.missilewars_tie.red").formatted(Formatting.RED);
    public static final MutableText GREEN =
            Text.translatable("modkrowd.message.missilewars_tie.green").formatted(Formatting.GREEN);
    public static final TextCache SIMULTANEOUS = TextCache.of(Text.translatable(
            "modkrowd.message.missilewars_tie.simultaneous"
    ).setStyle(CKColor.GOLD.style.withItalic(true)));

    public static MutableText sequential(MutableText first, MutableText last, long deltaTicks) {
        return Text.translatable(
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
            MutableText first;
            MutableText last;

            if (this.redWinTick < this.greenWinTick) {
                deltaTicks = this.greenWinTick - this.redWinTick;
                first = RED;
                last = GREEN;
            } else {
                deltaTicks = this.redWinTick - this.greenWinTick;
                first = GREEN;
                last = RED;
            }

            return TextCache.of(sequential(first, last, deltaTicks).setStyle(CKColor.DARK_AQUA.style.withItalic(true)));
        }
    }
}
