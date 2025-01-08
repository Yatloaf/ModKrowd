package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.text.Style;

import java.util.Map;

public enum RankLetters {
    GUEST(CKColor.GRAY, StyledString.fromString("G")),
    MEMBER(CKColor.GREEN, StyledString.fromString("M")),
    TRUSTED(CKColor.DARK_GREEN, StyledString.fromString("T")),
    RESPECTED(CKColor.DARK_PURPLE, StyledString.fromString("R")),
    VETERAN(CKColor.INDIGO, StyledString.fromString("V")),
    BUILDER(CKColor.AQUA, StyledString.fromString("B")),
    SPECIAL_GUEST(CKColor.GOLD, StyledString.fromString("SG")),
    YOUTUBE(CKColor.GOLD, StyledString.fromString("YT")),
    ZIPKROWD(CKColor.GOLD, StyledString.fromString("ZK")),
    DEVELOPER(CKColor.BLUE, StyledString.fromString("#")),
    HELPER(CKColor.DARK_AQUA, StyledString.fromString("H")),
    MODERATOR(CKColor.AZURE, StyledString.fromString("M", Style.EMPTY.withBold(true))),
    ADMIN(CKColor.RED, StyledString.fromString("A", Style.EMPTY.withBold(true))),
    UNKNOWN(CKColor.WHITE, StyledString.fromString("?"));

    public final CKColor color;
    public final StyledString letter;

    RankLetters(CKColor color, StyledString letter) {
        this.color = color;
        this.letter = letter;
    }

    private static final Map<StyledString, RankLetters> FROM_LETTER = Util.arrayToMap(values(), item -> item.letter, item -> item);

    public static RankLetters read(StyledStringReader source) {
        return source.mapNextOrDefault(FROM_LETTER, UNKNOWN);
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
