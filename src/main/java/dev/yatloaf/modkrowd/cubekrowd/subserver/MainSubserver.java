package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.util.Formatting;

import java.util.EnumSet;

public class MainSubserver extends RealSubserver {

    public MainSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public TextCache formatChat(String message) {
        RankName rankName = SelfPlayer.rankNameSoft();
        StyledString prefix = SelfPlayer.rankNameSoft().appearance();

        if (StyledString.EMPTY.equals(prefix)) {
            return TextCache.EMPTY;
        } else {
            EnumSet<Formatting> permittedFormattings = rankName.rank().letters().permittedFormattings();
            StyledString formatted = StyledString.fromFormattedString(message, '&', permittedFormattings);

            return TextCache.of(StyledString.concat(prefix, StyledString.SPACE, formatted));
        }
    }
}
