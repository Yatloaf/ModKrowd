package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.util.Util;

import java.util.Map;

public enum Minigame {
    MISSILEWARS("MissileWars",
            MinigameTeam.MW_LOBBY,
            MinigameTeam.MW_SPECTATOR,
            MinigameTeam.MW_RED,
            MinigameTeam.MW_GREEN
    ),
    ROCKETRIDERS("RocketRiders",
            MinigameTeam.RR_LOBBY,
            MinigameTeam.RR_SPECTATOR,
            MinigameTeam.RR_BLUE,
            MinigameTeam.RR_YELLOW
    ),
    CRAFTYCANNONEERS("CraftyCannoneers",
            MinigameTeam.CC_LOBBY,
            MinigameTeam.CC_SPECTATOR,
            MinigameTeam.CC_PURPLE,
            MinigameTeam.CC_ORANGE
    ),
    BACKSTABBED("BackStabbed!"),
    SNOWYSKIRMISH("SnowySkirmish",
            MinigameTeam.SS_LOBBY,
            MinigameTeam.SS_SPECTATOR,
            MinigameTeam.SS_GREEN,
            MinigameTeam.SS_RED
    ),
    ICERUNNER("IceRunner",
            MinigameTeam.IR_SPECTATOR,
            MinigameTeam.IR_RED,
            MinigameTeam.IR_GREEN
    ),
    FISHSLAP("FishSlap",
            MinigameTeam.FS
    ),
    UNKNOWN(null),
    ;

    private static final Map<String, Minigame> FROM_TAB_NAME = Util.arrayToMap(values(), key -> key.tabName, value -> value);

    public final String tabName;
    private final Map<CKColor, MinigameTeam> colorToTeam;

    Minigame(String tabName, MinigameTeam... teams) {
        this.tabName = tabName;
        this.colorToTeam = Util.arrayToMap(teams, team -> team.color, team -> team);
    }

    public static Minigame fromTabName(String tabName) {
        return FROM_TAB_NAME.getOrDefault(tabName, UNKNOWN);
    }

    public MinigameTeam teamFromColor(CKColor color) {
        return this.colorToTeam.getOrDefault(color, MinigameTeam.UNKNOWN);
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
