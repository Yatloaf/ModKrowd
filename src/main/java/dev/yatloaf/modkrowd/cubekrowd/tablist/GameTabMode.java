package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;

import java.util.Map;

public enum GameTabMode implements TabEntry {
    NORMAL("Normal"),
    _1V1("1v1"),
    _2V2("2v2"),
    _6V6("6v6"),
    _10V10("10v10"),
    UNKNOWN("");

    public static final Map<String, GameTabMode> FROM_CENTER = Util.arrayToMap(values(), item -> item.text, item -> item);

    public final String text;

    GameTabMode(String text) {
        this.text = text;
    }

    public static GameTabMode parse(StyledString source) {
        return FROM_CENTER.getOrDefault(source.toUnstyledString(), UNKNOWN);
    }
}
