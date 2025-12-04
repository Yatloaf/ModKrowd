package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.text.StyledString;

@FunctionalInterface
public interface FormatChat {
    TextCache formatChat(String message);

    static TextCache empty(String message) {
        return TextCache.EMPTY;
    }

    static TextCache rank(String message) {
        StyledString prefix = SelfPlayer.rankNameSoft().appearance();

        if (StyledString.EMPTY.equals(prefix)) {
            return TextCache.EMPTY;
        } else {
            StyledString formatted = SelfPlayer.tryFormat(message);
            return TextCache.of(StyledString.concat(prefix, StyledString.SPACE, formatted));
        }
    }

    static TextCache team(String message) {
        MinigameTeamName teamName = SelfPlayer.teamNameSoft();

        if (teamName != MinigameTeamName.FAILURE) {
            StyledString formatted = SelfPlayer.tryFormat(message);
            return TextCache.of(StyledString.concat(
                    StyledString.fromString("<"),
                    teamName.appearance(),
                    StyledString.fromString("> "),
                    formatted
            ));
        } else {
            return TextCache.EMPTY;
        }
    }

    static TextCache mixed(String message) {
        StyledString prefix = SelfPlayer.rankNameSoft().appearance();

        if (StyledString.EMPTY.equals(prefix)) {
            return TextCache.EMPTY;
        } else {
            StyledString formatted = SelfPlayer.tryFormat(message);
            return TextCache.of(StyledString.concat(
                    StyledString.fromString("<"),
                    prefix,
                    StyledString.fromString("> "),
                    formatted
            ));
        }
    }
}
