package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.util.Util;

import java.util.Map;

public class TeamSet {
    public static final TeamSet NONE = new TeamSet();
    public static final TeamSet MISSILEWARS = new TeamSet(
            MinigameTeam.MW_LOBBY,
            MinigameTeam.MW_SPECTATOR,
            MinigameTeam.MW_RED,
            MinigameTeam.MW_GREEN
    );
    public static final TeamSet ROCKETRIDERS = new TeamSet(
            MinigameTeam.RR_LOBBY,
            MinigameTeam.RR_SPECTATOR,
            MinigameTeam.RR_BLUE,
            MinigameTeam.RR_YELLOW
    );
    public static final TeamSet CRAFTYCANNONEERS = new TeamSet(
            MinigameTeam.CC_LOBBY,
            MinigameTeam.CC_SPECTATOR,
            MinigameTeam.CC_PURPLE,
            MinigameTeam.CC_ORANGE
    );
    public static final TeamSet ICERUNNER = new TeamSet(
            MinigameTeam.IR_SPECTATOR,
            MinigameTeam.IR_RED,
            MinigameTeam.IR_GREEN
    );
    public static final TeamSet FISHSLAP = new TeamSet(MinigameTeam.FS);

    private final Map<CKColor, MinigameTeam> colorToTeam;

    public TeamSet(MinigameTeam... teams) {
        this.colorToTeam = Util.arrayToMap(teams, team -> team.color, team -> team);
    }

    public MinigameTeam teamFromColor(CKColor color) {
        return this.colorToTeam.getOrDefault(color, MinigameTeam.UNKNOWN);
    }
}
