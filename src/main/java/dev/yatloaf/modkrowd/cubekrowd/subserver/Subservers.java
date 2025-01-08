package dev.yatloaf.modkrowd.cubekrowd.subserver;

import java.util.HashMap;
import java.util.Map;

public final class Subservers {
    private static final Map<String, Subserver> IDS = new HashMap<>();
    private static final Map<String, Subserver> LIST_NAMES = new HashMap<>();
    private static final Map<String, Subserver> TAB_NAMES = new HashMap<>();

    public static final NoneSubserver NONE = subserver(new NoneSubserver(null, null));
    public static final FakeSubserver PENDING = subserver(new FakeSubserver(null, null));
    public static final FakeSubserver UNKNOWN = subserver(new FakeSubserver(null, null));
    public static final LobbySubserver LOBBY = subserver(new LobbySubserver("lobby", "Lobby", "Lobby"));
    public static final CreativeSubserver CREATIVE = subserver(new CreativeSubserver("creative", "Creative", "Creative"));
    public static final SkyblockSubserver SKYBLOCK = subserver(new SkyblockSubserver("skyblock", "SkyBlock", "Skyblock"));
    public static final SurvivalSubserver SURVIVAL = subserver(new SurvivalSubserver("survival", "Survival", "Survival1", "Survival 1"));
    public static final SurvivalSubserver SURVIVAL2 = subserver(new SurvivalSubserver("survival2", "Survival 2", "Survival2", "Survival 2"));
    public static final FakeSubserver SURVIVAL_AMBIGUOUS = subserver(new FakeSubserver(null, null, "Survival"));
    public static final BuildCompSubserver BUILDCOMP = subserver(new BuildCompSubserver("buildcomp", "BuildComp", "BuildComp"));
    public static final CreativeSubserver BUILDTEAM = subserver(new CreativeSubserver("buildteam", "BuildTeam", "BuildTeam"));
    public static final GameLobbySubserver GAMELOBBY = subserver(new GameLobbySubserver("gamelobby1", "GameLobby"));
    public static final MissileWarsSubserver MISSILEWARS0 = subserver(new MissileWarsSubserver("missilewars0", "MissileWars 0", "MissileWars+ 0"));
    public static final MissileWarsSubserver MISSILEWARS1 = subserver(new MissileWarsSubserver("missilewars1", "MissileWars 1", "MissileWars 1"));
    public static final MissileWarsSubserver MISSILEWARS2 = subserver(new MissileWarsSubserver("missilewars2", "MissileWars 2", "MissileWars 2"));
    public static final MissileWarsSubserver MISSILEWARS3 = subserver(new MissileWarsSubserver("missilewars3", "MissileWars 3", "MissileWars 3"));
    public static final MissileWarsSubserver MISSILEWARS4 = subserver(new MissileWarsSubserver("missilewars4", "MissileWars 4", "MissileWars 4"));
    public static final MissileWarsSubserver MISSILEWARS5 = subserver(new MissileWarsSubserver("missilewars5", "MissileWars 5", "MissileWars 5"));
    public static final MissileWarsSubserver MISSILEWARS6 = subserver(new MissileWarsSubserver("missilewars6", "MissileWars 6", "MissileWars 6"));
    public static final MissileWarsSubserver MISSILEWARS7 = subserver(new MissileWarsSubserver("missilewars7", "MissileWars 7", "MissileWars 7"));
    public static final MissileWarsSubserver MISSILEWARS8 = subserver(new MissileWarsSubserver("missilewars8", "MissileWars 8", "MissileWars 8"));
    public static final MissileWarsSubserver MISSILEWARS9 = subserver(new MissileWarsSubserver("missilewars9", "MissileWars 9", "MissileWars 9"));
    public static final RocketRidersSubserver ROCKETRIDERS0 = subserver(new RocketRidersSubserver("rocketriders0", "RocketRiders 0", "RocketRiders 0"));
    public static final RocketRidersSubserver ROCKETRIDERS1 = subserver(new RocketRidersSubserver("rocketriders1", "RocketRiders 1", "RocketRiders 1"));
    public static final RocketRidersSubserver ROCKETRIDERS2 = subserver(new RocketRidersSubserver("rocketriders2", "RocketRiders 2", "RocketRiders 2"));
    public static final RocketRidersSubserver ROCKETRIDERS3 = subserver(new RocketRidersSubserver("rocketriders3", "RocketRiders 3", "RocketRiders 3"));
    public static final RocketRidersSubserver ROCKETRIDERS4 = subserver(new RocketRidersSubserver("rocketriders4", "RocketRiders 4", "RocketRiders 4"));
    public static final RocketRidersSubserver ROCKETRIDERS5 = subserver(new RocketRidersSubserver("rocketriders5", "RocketRiders 5", "RocketRiders 5"));
    public static final RocketRidersSubserver ROCKETRIDERS6 = subserver(new RocketRidersSubserver("rocketriders6", "RocketRiders 6", "RocketRiders 6"));
    public static final RocketRidersSubserver ROCKETRIDERS7 = subserver(new RocketRidersSubserver("rocketriders7", "RocketRiders 7", "RocketRiders 7"));
    public static final RocketRidersSubserver ROCKETRIDERS8 = subserver(new RocketRidersSubserver("rocketriders8", "RocketRiders 8", "RocketRiders 8"));
    public static final RocketRidersSubserver ROCKETRIDERS9 = subserver(new RocketRidersSubserver("rocketriders9", "RocketRiders 9", "RocketRiders 9"));
    public static final BackstabbedSubserver BACKSTABBED1 = subserver(new BackstabbedSubserver("backstabbed1", "Backstabbed! 1", "Backstabbed! 1"));
    public static final BackstabbedSubserver BACKSTABBED2 = subserver(new BackstabbedSubserver("backstabbed2", "Backstabbed! 2", "Backstabbed! 2"));
    public static final BackstabbedSubserver BACKSTABBED3 = subserver(new BackstabbedSubserver("backstabbed3", "Backstabbed! 3", "Backstabbed! 3"));
    public static final BackstabbedSubserver BACKSTABBED4 = subserver(new BackstabbedSubserver("backstabbed4", "Backstabbed! 4", "Backstabbed! 4"));
    public static final CraftyCannoneersSubserver CRAFTYCANNONEERS1 = subserver(new CraftyCannoneersSubserver("craftycannoneers1", "CraftyCannoneers 1", "CraftyCannoneers 1"));
    public static final CraftyCannoneersSubserver CRAFTYCANNONEERS2 = subserver(new CraftyCannoneersSubserver("craftycannoneers2", "CraftyCannoneers 2", "CraftyCannoneers 2"));
    public static final CraftyCannoneersSubserver CRAFTYCANNONEERS3 = subserver(new CraftyCannoneersSubserver("craftycannoneers3", "CraftyCannoneers 3", "CraftyCannoneers 3"));
    public static final CraftyCannoneersSubserver CRAFTYCANNONEERS4 = subserver(new CraftyCannoneersSubserver("craftycannoneers4", "CraftyCannoneers 4", "CraftyCannoneers 4"));
    public static final SnowySkirmishSubserver SNOWYSKIRMISH1 = subserver(new SnowySkirmishSubserver("snowyskirmish1", "SnowySkirmish 1", "SnowySkirmish 1"));
    public static final SnowySkirmishSubserver SNOWYSKIRMISH2 = subserver(new SnowySkirmishSubserver("snowyskirmish2", "SnowySkirmish 2", "SnowySkirmish 2"));
    public static final IceRunnerSubserver ICERUNNER = subserver(new IceRunnerSubserver("icerunner", "IceRunner", "IceRunner"));
    public static final FishslapSubserver FISHSLAP = subserver(new FishslapSubserver("fishslap", "Fishslap", "Fishslap"));
    public static final UHCSubserver UHC = subserver(new UHCSubserver("uhc", "UHC"));
    public static final FakeSubserver PVP = subserver(new FakeSubserver("pvp", "PVP", "PVP")); // :sunglasses:

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

    private static <S extends Subserver> S subserver(S subserver) {
        if (subserver.id != null) {
            IDS.put(subserver.id, subserver);
        }
        if (subserver.listName != null) {
            LIST_NAMES.put(subserver.listName, subserver);
        }
        for (String tabName : subserver.tabNames) {
            TAB_NAMES.put(tabName, subserver);
        }
        return subserver;
    }
}
