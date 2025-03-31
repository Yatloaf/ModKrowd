package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;

import java.util.Map;

public enum GameTabStatus implements TabEntry {
    OFFLINE(StyledString.fromString("OFFLINE", CKColor.RED.style.withBold(true))),
    ONLINE(StyledString.fromString("ONLINE", CKColor.GREEN.style.withBold(true))),
    UNKNOWN(StyledString.EMPTY);

    public static final Map<StyledString, GameTabStatus> FROM_CENTER = Util.arrayToMap(values(), item -> item.text, item -> item);

    public final StyledString text;

    GameTabStatus(StyledString text) {
        this.text = text;
    }

    public static GameTabStatus parse(StyledString source) {
        return FROM_CENTER.getOrDefault(source, UNKNOWN);
    }
}
