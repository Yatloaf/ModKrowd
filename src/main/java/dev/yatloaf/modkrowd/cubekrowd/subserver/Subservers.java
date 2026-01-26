package dev.yatloaf.modkrowd.cubekrowd.subserver;

import java.util.HashMap;
import java.util.Map;

public final class Subservers {
    private static final Map<String, Subserver> IDS = new HashMap<>();
    private static final Map<String, Subserver> LIST_NAMES = new HashMap<>();
    private static final Map<String, Subserver> TAB_NAMES = new HashMap<>();

    public static final Subserver NONE = Subserver.builder().notCubeKrowd().fake().build();
    public static final Subserver PENDING = Subserver.builder().fake().build();
    public static final Subserver UNKNOWN = Subserver.builder().fake().build();
    public static final Subserver LOBBY = Subserver.builder().id("lobby").name("Lobby").mainChat().build();
    public static final Subserver CREATIVE = Subserver.builder().id("creative").name("Creative").mainChat().allowCheats().build();
    public static final Subserver SKYBLOCK = Subserver.builder().id("skyblock").listName("SkyBlock").tabNames("SkyBlock", "Skyblock").mainChat().build();
    public static final Subserver SURVIVAL = Subserver.builder().id("survival").listName("Survival").tabNames("Survival1", "Survival 1").mainChat().build();
    public static final Subserver SURVIVAL2 = Subserver.builder().id("survival2").listName("Survival 2").tabNames("Survival2", "Survival 2").mainChat().build();
    public static final Subserver SURVIVAL_AMBIGUOUS = Subserver.builder().tabNames("Survival").build();
    public static final Subserver BUILDCOMP = Subserver.builder().id("buildcomp").listName("BuildComp").tabNames("BuildComp", "Buildcomp").mainChat().build();
    public static final Subserver BUILDTEAM = Subserver.builder().id("buildteam").listName("BuildTeam").tabNames("BuildTeam", "Buildteam").mixedChat().allowCheats().build();
    public static final Subserver GAMELOBBY = Subserver.builder().id("gamelobby1").listName("GameLobby").tabNames("Game Lobby", "Game lobby").mainChat().build();
    public static final Subserver MISSILEWARS0 = Subserver.builder().id("missilewars0").listName("MissileWars 0").tabNames("MW0").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS1 = Subserver.builder().id("missilewars1").listName("MissileWars 1").tabNames("MW1").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS2 = Subserver.builder().id("missilewars2").listName("MissileWars 2").tabNames("MW2").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS3 = Subserver.builder().id("missilewars3").listName("MissileWars 3").tabNames("MW3").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS4 = Subserver.builder().id("missilewars4").listName("MissileWars 4").tabNames("MW4").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS5 = Subserver.builder().id("missilewars5").listName("MissileWars 5").tabNames("MW5").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS6 = Subserver.builder().id("missilewars6").listName("MissileWars 6").tabNames("MW6").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS7 = Subserver.builder().id("missilewars7").listName("MissileWars 7").tabNames("MW7").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS8 = Subserver.builder().id("missilewars8").listName("MissileWars 8").tabNames("MW8").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS9 = Subserver.builder().id("missilewars9").listName("MissileWars 9").tabNames("MW9").minigame(Minigame.MISSILEWARS).minigameChat().build();
    public static final Subserver ROCKETRIDERS0 = Subserver.builder().id("rocketriders0").listName("RocketRiders 0").tabNames("RR0").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS1 = Subserver.builder().id("rocketriders1").listName("RocketRiders 1").tabNames("RR1").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS2 = Subserver.builder().id("rocketriders2").listName("RocketRiders 2").tabNames("RR2").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS3 = Subserver.builder().id("rocketriders3").listName("RocketRiders 3").tabNames("RR3").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS4 = Subserver.builder().id("rocketriders4").listName("RocketRiders 4").tabNames("RR4").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS5 = Subserver.builder().id("rocketriders5").listName("RocketRiders 5").tabNames("RR5").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS6 = Subserver.builder().id("rocketriders6").listName("RocketRiders 6").tabNames("RR6").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS7 = Subserver.builder().id("rocketriders7").listName("RocketRiders 7").tabNames("RR7").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS8 = Subserver.builder().id("rocketriders8").listName("RocketRiders 8").tabNames("RR8").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS9 = Subserver.builder().id("rocketriders9").listName("RocketRiders 9").tabNames("RR9").minigame(Minigame.ROCKETRIDERS).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS1 = Subserver.builder().id("craftycannoneers1").listName("CraftyCannoneers 1").tabNames("CC1").minigame(Minigame.CRAFTYCANNONEERS).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS2 = Subserver.builder().id("craftycannoneers2").listName("CraftyCannoneers 2").tabNames("CC2").minigame(Minigame.CRAFTYCANNONEERS).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS3 = Subserver.builder().id("craftycannoneers3").listName("CraftyCannoneers 3").tabNames("CC3").minigame(Minigame.CRAFTYCANNONEERS).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS4 = Subserver.builder().id("craftycannoneers4").listName("CraftyCannoneers 4").tabNames("CC4").minigame(Minigame.CRAFTYCANNONEERS).minigameChat().build();
    // TODO: Backstabbed Teams?
    public static final Subserver BACKSTABBED1 = Subserver.builder().id("backstabbed1").listName("Backstabbed! 1").tabNames("BS1").minigame(Minigame.UNKNOWN).minigameChat().build();
    public static final Subserver BACKSTABBED2 = Subserver.builder().id("backstabbed2").listName("Backstabbed! 2").tabNames("BS2").minigame(Minigame.UNKNOWN).minigameChat().build();
    public static final Subserver BACKSTABBED3 = Subserver.builder().id("backstabbed3").listName("Backstabbed! 3").tabNames("BS3").minigame(Minigame.UNKNOWN).minigameChat().build();
    public static final Subserver BACKSTABBED4 = Subserver.builder().id("backstabbed4").listName("Backstabbed! 4").tabNames("BS4").minigame(Minigame.UNKNOWN).minigameChat().build();
    public static final Subserver SNOWYSKIRMISH1 = Subserver.builder().id("snowyskirmish1").listName("SnowySkirmish 1").tabNames("SS1").minigame(Minigame.UNKNOWN).minigameChat().build();
    public static final Subserver SNOWYSKIRMISH2 = Subserver.builder().id("snowyskirmish2").listName("SnowySkirmish 2").tabNames("SS2").minigame(Minigame.UNKNOWN).minigameChat().build();
    public static final Subserver ICERUNNER = Subserver.builder().id("icerunner").name("IceRunner").minigame(Minigame.ICERUNNER).minigameChat().build();
    // TODO: Fishslap Teams...?
    public static final Subserver FISHSLAP = Subserver.builder().id("fishslap").name("FishSlap").minigame(Minigame.FISHSLAP).mixedChat().build();
    public static final Subserver SLAPFISH = Subserver.builder().id("pvp").name("PVP").minigame(Minigame.FISHSLAP).mixedChat().build(); // Might change if PVP ever comes back for real
    // TODO: UHC Teams!
    public static final Subserver UHC = Subserver.builder().id("uhc").listName("UHC").build();

    static {
        MISSILEWARS1.nextSubservers = new Subserver[]{MISSILEWARS2, MISSILEWARS3, MISSILEWARS4, MISSILEWARS5};
        MISSILEWARS2.nextSubservers = new Subserver[]{MISSILEWARS1, MISSILEWARS3, MISSILEWARS4, MISSILEWARS5};
        MISSILEWARS3.nextSubservers = new Subserver[]{MISSILEWARS1, MISSILEWARS2, MISSILEWARS4, MISSILEWARS5};
        MISSILEWARS4.nextSubservers = new Subserver[]{MISSILEWARS1, MISSILEWARS2, MISSILEWARS3, MISSILEWARS5};
        MISSILEWARS5.nextSubservers = new Subserver[]{MISSILEWARS1, MISSILEWARS2, MISSILEWARS3, MISSILEWARS4};
        MISSILEWARS7.nextSubservers = new Subserver[]{MISSILEWARS8, MISSILEWARS9};
        MISSILEWARS8.nextSubservers = new Subserver[]{MISSILEWARS7, MISSILEWARS9};
        MISSILEWARS9.nextSubservers = new Subserver[]{MISSILEWARS7, MISSILEWARS8};
    }

    public static Subserver fromId(String id) {
        return IDS.getOrDefault(id, UNKNOWN);
    }

    public static Subserver fromListName(String listName) {
        return LIST_NAMES.getOrDefault(listName, UNKNOWN);
    }

    public static Subserver fromTabName(String tabName) {
        return TAB_NAMES.getOrDefault(tabName, UNKNOWN);
    }

    public static void register(Subserver subserver) {
        if (subserver.id != null) {
            IDS.put(subserver.id, subserver);
        }
        if (subserver.listName != null) {
            LIST_NAMES.put(subserver.listName, subserver);
        }
        for (String tabName : subserver.tabNames) {
            TAB_NAMES.put(tabName, subserver);
        }
    }
}
