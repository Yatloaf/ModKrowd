package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;

import java.util.Map;

public enum MinigameMode {
    _1V1("1v1"),
    _2V2("2v2"),
    _5V5("5v5"),
    _6V6("6v6"),
    _10V10("10v10"),
    UNKNOWN(""),
    ;

    public static final Map<String, MinigameMode> FROM_NAME = Util.arrayToMap(values(), item -> item.text, item -> item);

    public final String text;

    MinigameMode(String text) {
        this.text = text;
    }

    public static MinigameMode parse(StyledString source) {
        return FROM_NAME.getOrDefault(source.toUnstyledString(), UNKNOWN);
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
