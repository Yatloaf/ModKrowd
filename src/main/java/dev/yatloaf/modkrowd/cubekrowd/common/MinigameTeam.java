package dev.yatloaf.modkrowd.cubekrowd.common;

public enum MinigameTeam {
    MW_LOBBY(CKColor.GRAY),
    MW_SPECTATOR(CKColor.BLUE),
    MW_RED(CKColor.RED),
    MW_GREEN(CKColor.GREEN),
    RR_LOBBY(CKColor.GREEN),
    RR_SPECTATOR(CKColor.DARK_GRAY),
    RR_BLUE(CKColor.BLUE),
    RR_YELLOW(CKColor.GOLD),
    CC_LOBBY(CKColor.BLUE),
    CC_SPECTATOR(CKColor.DARK_GRAY),
    CC_PURPLE(CKColor.DARK_PURPLE),
    CC_ORANGE(CKColor.GOLD),
    SS_LOBBY(CKColor.GRAY),
    SS_SPECTATOR(CKColor.BLUE),
    SS_GREEN(CKColor.GREEN),
    SS_RED(CKColor.RED),
    IR_SPECTATOR(CKColor.WHITE),
    IR_RED(CKColor.RED),
    IR_GREEN(CKColor.GREEN),
    FS(CKColor.WHITE),
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
