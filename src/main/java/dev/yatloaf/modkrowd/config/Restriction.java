package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.server.permissions.Permissions;

import java.util.HashMap;
import java.util.Map;

public class Restriction {
    public static final Map<String, Restriction> FROM_ID = new HashMap<>();

    public static final Restriction ALWAYS = new Restriction(null, CKColor.GREEN.style, Test::always);
    public static final Restriction CREATIVE = new Restriction("creative", CKColor.AQUA.style, Test::creative);

    public final Component suffix;
    public final Test test;

    public Restriction(String id, Style style, Test test) {
        this.suffix = id != null ? Component.translatable("modkrowd.config.restriction." + id + ".suffix").withStyle(style) : null;
        this.test = test;
        FROM_ID.put(id, this);
    }

    @FunctionalInterface
    public interface Test {
        boolean test(Subserver subserver, PermissionSet permissions);

        static boolean always(Subserver subserver, PermissionSet permissions) {
            return true;
        }

        static boolean creative(Subserver subserver, PermissionSet permissions) {
            if (subserver.allowCheats) {
                return true;
            } else if (subserver.isCubeKrowd) {
                return false;
            } else {
                return permissions.hasPermission(Permissions.COMMANDS_GAMEMASTER);
            }
        }
    }
}
