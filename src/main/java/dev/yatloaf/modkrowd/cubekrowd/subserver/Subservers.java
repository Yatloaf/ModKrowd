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
    public static final Subserver BUILDCOMP = Subserver.builder().id("buildcomp").name("BuildComp").mainChat().build();
    public static final Subserver BUILDTEAM = Subserver.builder().id("buildteam").listName("BuildTeam").tabNames("BuildTeam", "Buildteam").mixedChat().allowCheats().build();
    public static final Subserver GAMELOBBY = Subserver.builder().id("gamelobby1").listName("GameLobby").mainChat().build();
    public static final Subserver MISSILEWARS0 = Subserver.builder().id("missilewars0").listName("MissileWars 0").tabNames("MissileWars+ 0").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS1 = Subserver.builder().id("missilewars1").name("MissileWars 1").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS2 = Subserver.builder().id("missilewars2").name("MissileWars 2").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS3 = Subserver.builder().id("missilewars3").name("MissileWars 3").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS4 = Subserver.builder().id("missilewars4").name("MissileWars 4").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS5 = Subserver.builder().id("missilewars5").name("MissileWars 5").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS6 = Subserver.builder().id("missilewars6").name("MissileWars 6").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS7 = Subserver.builder().id("missilewars7").name("MissileWars 7").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS8 = Subserver.builder().id("missilewars8").name("MissileWars 8").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver MISSILEWARS9 = Subserver.builder().id("missilewars9").name("MissileWars 9").teams(TeamSet.MISSILEWARS).minigameChat().build();
    public static final Subserver ROCKETRIDERS0 = Subserver.builder().id("rocketriders0").name("RocketRiders 0").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS1 = Subserver.builder().id("rocketriders1").name("RocketRiders 1").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS2 = Subserver.builder().id("rocketriders2").name("RocketRiders 2").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS3 = Subserver.builder().id("rocketriders3").name("RocketRiders 3").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS4 = Subserver.builder().id("rocketriders4").name("RocketRiders 4").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS5 = Subserver.builder().id("rocketriders5").name("RocketRiders 5").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS6 = Subserver.builder().id("rocketriders6").name("RocketRiders 6").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS7 = Subserver.builder().id("rocketriders7").name("RocketRiders 7").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS8 = Subserver.builder().id("rocketriders8").name("RocketRiders 8").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    public static final Subserver ROCKETRIDERS9 = Subserver.builder().id("rocketriders9").name("RocketRiders 9").teams(TeamSet.ROCKETRIDERS).minigameChat().build();
    // TODO: Backstabbed Teams?
    public static final Subserver BACKSTABBED1 = Subserver.builder().id("backstabbed1").name("Backstabbed! 1").teams(TeamSet.NONE).minigameChat().build();
    public static final Subserver BACKSTABBED2 = Subserver.builder().id("backstabbed2").name("Backstabbed! 2").teams(TeamSet.NONE).minigameChat().build();
    public static final Subserver BACKSTABBED3 = Subserver.builder().id("backstabbed3").name("Backstabbed! 3").teams(TeamSet.NONE).minigameChat().build();
    public static final Subserver BACKSTABBED4 = Subserver.builder().id("backstabbed4").name("Backstabbed! 4").teams(TeamSet.NONE).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS1 = Subserver.builder().id("craftycannoneers1").name("CraftyCannoneers 1").teams(TeamSet.CRAFTYCANNONEERS).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS2 = Subserver.builder().id("craftycannoneers2").name("CraftyCannoneers 2").teams(TeamSet.CRAFTYCANNONEERS).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS3 = Subserver.builder().id("craftycannoneers3").name("CraftyCannoneers 3").teams(TeamSet.CRAFTYCANNONEERS).minigameChat().build();
    public static final Subserver CRAFTYCANNONEERS4 = Subserver.builder().id("craftycannoneers4").name("CraftyCannoneers 4").teams(TeamSet.CRAFTYCANNONEERS).minigameChat().build();
    // TODO: SnowySkirmish Teams...
    public static final Subserver SNOWYSKIRMISH1 = Subserver.builder().id("snowyskirmish1").name("SnowySkirmish 1").teams(TeamSet.NONE).minigameChat().build();
    public static final Subserver SNOWYSKIRMISH2 = Subserver.builder().id("snowyskirmish2").name("SnowySkirmish 2").teams(TeamSet.NONE).minigameChat().build();
    public static final Subserver ICERUNNER = Subserver.builder().id("icerunner").name("IceRunner").teams(TeamSet.ICERUNNER).minigameChat().build();
    // TODO: Fishslap Teams...?
    public static final Subserver FISHSLAP = Subserver.builder().id("fishslap").name("Fishslap").teams(TeamSet.FISHSLAP).mixedChat().build();
    public static final Subserver SLAPFISH = Subserver.builder().id("pvp").name("PVP").teams(TeamSet.FISHSLAP).mixedChat().build(); // Might change if PVP ever comes back for real
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
