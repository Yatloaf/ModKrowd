package dev.yatloaf.modkrowd.cubekrowd.message;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.function.UnaryOperator;

public record MissileWarsDeathMessage(String key, Component victim, Component killer, Component item, boolean isReal) implements Message {
    public static final MissileWarsDeathMessage FAILURE = new MissileWarsDeathMessage(
            "death.attack.generic", Component.empty(), null, null, false
    );

    public static MissileWarsDeathMessage parseFast(TranslatableContents content) {
        String key = content.getKey();
        if (!key.startsWith("death.attack") && !key.startsWith("death.fell")) return FAILURE;

        Object[] args = content.getArgs();
        if (args.length < 1) return FAILURE;

        Component victim;
        if (args[0] instanceof Component text) {
            victim = text;
        } else {
            victim = Component.literal(args[0].toString());
        }

        Component killer;
        if (args.length < 2) {
            killer = null;
        } else if (args[1] instanceof Component text) {
            killer = text;
        } else {
            killer = Component.literal(args[0].toString());
        }

        Component item;
        if (args.length < 3) {
            item = null;
        } else if (args[2] instanceof Component text) {
            item = text;
        } else {
            item = Component.literal(args[0].toString());
        }

        return new MissileWarsDeathMessage(key, victim, killer, item, true);
    }

    public MissileWarsDeathMessage mapVictim(UnaryOperator<Component> mapper) {
        return new MissileWarsDeathMessage(this.key, mapper.apply(this.victim), this.killer, this.item, this.isReal);
    }

    public MissileWarsDeathMessage mapKillerIfPresent(UnaryOperator<Component> mapper) {
        if (this.killer == null) {
            return this;
        } else {
            return new MissileWarsDeathMessage(this.key, this.victim, mapper.apply(this.killer), this.item, this.isReal);
        }
    }

    public MutableComponent appearance() {
        Object[] args;
        if (this.killer == null) {
            args = new Component[]{this.victim};
        } else if (this.item == null) {
            args = new Component[]{this.victim, this.killer};
        } else {
            args = new Component[]{this.victim, this.killer, this.item};
        }
        return Component.translatable(this.key, args);
    }
}
