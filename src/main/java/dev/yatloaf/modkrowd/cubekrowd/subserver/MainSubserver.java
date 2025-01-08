package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MainSubserver extends RealSubserver {

    public MainSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public TextCache formatChat(String message) {
        StyledString prefix = SelfPlayer.rankNameSoft().appearance();
        if (StyledString.EMPTY.equals(prefix)) {
            return TextCache.EMPTY;
        } else {
            return TextCache.of(prefix.toText().append(Text.literal(" " + message).formatted(Formatting.WHITE)));
        }
    }
}
