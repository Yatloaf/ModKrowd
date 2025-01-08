package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.Map;

public enum RankBrackets {
    NONE(StyledString.fromString("[", CKColor.DARK_GRAY.style), StyledString.fromString("]", CKColor.DARK_GRAY.style)),
    YELLOW(StyledString.fromString("[", CKColor.YELLOW.style), StyledString.fromString("]", CKColor.YELLOW.style)),
    GOLD(StyledString.fromString("[", CKColor.GOLD.style), StyledString.fromString("]", CKColor.GOLD.style)),
    UNKNOWN(StyledString.fromString("["), StyledString.fromString("]"));

    public final StyledString leftBracket;
    public final StyledString rightBracket;

    RankBrackets(StyledString leftBracket, StyledString rightBracket) {
        this.leftBracket = leftBracket;
        this.rightBracket = rightBracket;
    }

    private static final Map<StyledString, RankBrackets> FROM_LEFT_BRACKET = Util.arrayToMap(values(), item -> item.leftBracket, item -> item);
    private static final Map<StyledString, RankBrackets> FROM_RIGHT_BRACKET = Util.arrayToMap(values(), item -> item.rightBracket, item -> item);

    public static RankBrackets readLeft(StyledStringReader source) {
        return source.mapNextOrDefault(FROM_LEFT_BRACKET, UNKNOWN);
    }

    public static RankBrackets readRight(StyledStringReader source) {
        return source.mapNextOrDefault(FROM_RIGHT_BRACKET, UNKNOWN);
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
