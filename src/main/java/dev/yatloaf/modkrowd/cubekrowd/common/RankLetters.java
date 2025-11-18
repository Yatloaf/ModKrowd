package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

import java.util.EnumSet;
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

    public static final EnumSet<ChatFormatting> PF_NONE = EnumSet.noneOf(ChatFormatting.class);
    public static final EnumSet<ChatFormatting> PF_LIMITED = EnumSet.of(
            ChatFormatting.STRIKETHROUGH,
            ChatFormatting.UNDERLINE,
            ChatFormatting.ITALIC,
            ChatFormatting.RESET
    );
    public static final EnumSet<ChatFormatting> PF_ALMOST_ALL = EnumSet.allOf(ChatFormatting.class);
    static {
        PF_ALMOST_ALL.remove(ChatFormatting.OBFUSCATED);
    }

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

    public EnumSet<ChatFormatting> permittedFormattings() {
        // Can't really know for stacked ranks
        return switch (this) {
            case ADMIN, MODERATOR, HELPER, DEVELOPER -> PF_ALMOST_ALL;
            case ZIPKROWD, YOUTUBE, SPECIAL_GUEST, BUILDER, VETERAN, RESPECTED -> PF_LIMITED;
            default -> PF_NONE;
        };
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
