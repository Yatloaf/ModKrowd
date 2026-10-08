package dev.yatloaf.modkrowd.cubekrowd.common;

public enum MinigameTeam {
    MW_LOBBY(CKColor.GRAY),
    MW_SPECTATOR(CKColor.BLUE),
    MW_RED(CKColor.RED),
    MW_GREEN(CKColor.GREEN),
    RR_LOBBY(CKColor.GRAY),
    RR_SPECTATOR(CKColor.DARK_GRAY),
    RR_BLUE(CKColor.BLUE),
    RR_YELLOW(CKColor.GOLD),
    RR_CHASE(CKColor.DARK_RED),
    CC_LOBBY(CKColor.BLUE),
    CC_SPECTATOR(CKColor.DARK_GRAY),
    CC_PURPLE(CKColor.DARK_PURPLE),
    CC_ORANGE(CKColor.GOLD),
    FF_LOBBY(CKColor.WHITE),
    FF_SPECTATOR(CKColor.GRAY),
    FF_GUARD(CKColor.GOLD),
    FF_THIEF(CKColor.RED),
    SS_LOBBY(CKColor.GRAY),
    SS_SPECTATOR(CKColor.BLUE),
    SS_GREEN(CKColor.GREEN),
    SS_RED(CKColor.RED),
    IR_SPECTATOR(CKColor.WHITE),
    IR_RED(CKColor.RED),
    IR_GREEN(CKColor.GREEN),
    FS(CKColor.WHITE),
    PU_LOBBY(CKColor.GRAY),
    PU_SPECTATOR(CKColor.DARK_GRAY),
    PU_SURVIVOR(CKColor.GREEN),
    PU_PURPLE(CKColor.DARK_PURPLE),
    UNKNOWN(CKColor.WHITE),
    ;

    public final CKColor color;

    MinigameTeam(CKColor color) {
        this.color = color;
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
