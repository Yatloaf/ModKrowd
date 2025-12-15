package dev.yatloaf.modkrowd.cubekrowd.message;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.function.UnaryOperator;

public record DeathMessage(String key, Component victim, Component killer, Component item, boolean isReal) implements Message {
    public static final DeathMessage FAILURE = new DeathMessage(
            "death.attack.generic", Component.empty(), null, null, false
    );

    public static DeathMessage parseFast(ComponentContents contents) {
        if (!(contents instanceof TranslatableContents translatable)) return FAILURE;

        String key = translatable.getKey();
        if (!key.startsWith("death.attack") && !key.startsWith("death.fell")) return FAILURE;

        Object[] args = translatable.getArgs();
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

        return new DeathMessage(key, victim, killer, item, true);
    }

    public DeathMessage mapVictim(UnaryOperator<Component> mapper) {
        return new DeathMessage(this.key, mapper.apply(this.victim), this.killer, this.item, this.isReal);
    }

    public DeathMessage mapKillerIfPresent(UnaryOperator<Component> mapper) {
        if (this.killer == null) {
            return this;
        } else {
            return new DeathMessage(this.key, this.victim, mapper.apply(this.killer), this.item, this.isReal);
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
