package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.CreativeSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.CubeKrowdSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MissileWarsSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.HashMap;
import java.util.Map;

public class Predicate {
    // Maybe this class should be renamed

    public static final Map<String, Predicate> FROM_ID = new HashMap<>();

    public static final Predicate NEVER = new Predicate("never", CKColor.RED.style, Test::never);
    public static final Predicate CUBEKROWD = new Predicate("cubekrowd", CKColor.GOLD.style, Test::cubeKrowd);
    public static final Predicate CREATIVE = new Predicate("creative", CKColor.AQUA.style, Test::creative);
    public static final Predicate MISSILEWARS = new Predicate("missilewars", CKColor.WHITE.style, Test::missileWars);
    public static final Predicate ALWAYS = new Predicate("always", CKColor.GREEN.style, Test::always);

    public final String id;
    public final Component name;
    public final Component tooltip;
    public final Test test;

    public Predicate(String id, Style style, Test test) {
        this.id = id;
        this.name = Component.translatable("modkrowd.config.predicate." + id).setStyle(style);
        this.tooltip = Component.translatable("modkrowd.config.predicate." + id + ".tooltip");
        this.test = test;
        FROM_ID.put(id, this);
    }

    public boolean enabled(Subserver subserver, int permissionLevel) {
        return this.test.test(subserver, permissionLevel);
    }

    @FunctionalInterface
    public interface Test {
        boolean test(Subserver subserver, int permissionLevel);

        static boolean never(Subserver subserver, int permissionLevel) {
            return false;
        }
        static boolean cubeKrowd(Subserver subserver, int permissionLevel) {
            return subserver instanceof CubeKrowdSubserver;
        }
        static boolean creative(Subserver subserver, int permissionLevel) {
            return switch (subserver) {
                case CreativeSubserver ignored -> true;
                case CubeKrowdSubserver ignored -> false;
                default -> permissionLevel >= 2;
            };
        }
        static boolean missileWars(Subserver subserver, int permissionLevel) {
            return subserver instanceof MissileWarsSubserver;
        }
        static boolean always(Subserver subserver, int permissionLevel) {
            return true;
        }
    }
}
