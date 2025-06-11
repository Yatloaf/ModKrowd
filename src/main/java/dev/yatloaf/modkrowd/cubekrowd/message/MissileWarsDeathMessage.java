package dev.yatloaf.modkrowd.cubekrowd.message;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.function.UnaryOperator;

public record MissileWarsDeathMessage(String key, Text victim, Text killer, Text item, boolean isReal) implements Message {
    public static final MissileWarsDeathMessage FAILURE = new MissileWarsDeathMessage(
            "death.attack.generic", Text.empty(), null, null, false
    );

    public static MissileWarsDeathMessage parseFast(TranslatableTextContent content) {
        String key = content.getKey();
        if (!key.startsWith("death.attack") && !key.startsWith("death.fell")) return FAILURE;

        Object[] args = content.getArgs();
        if (args.length < 1) return FAILURE;

        Text victim;
        if (args[0] instanceof Text text) {
            victim = text;
        } else {
            victim = Text.literal(args[0].toString());
        }

        Text killer;
        if (args.length < 2) {
            killer = null;
        } else if (args[1] instanceof Text text) {
            killer = text;
        } else {
            killer = Text.literal(args[0].toString());
        }

        Text item;
        if (args.length < 3) {
            item = null;
        } else if (args[2] instanceof Text text) {
            item = text;
        } else {
            item = Text.literal(args[0].toString());
        }

        return new MissileWarsDeathMessage(key, victim, killer, item, true);
    }

    public MissileWarsDeathMessage mapVictim(UnaryOperator<Text> mapper) {
        return new MissileWarsDeathMessage(this.key, mapper.apply(this.victim), this.killer, this.item, this.isReal);
    }

    public MissileWarsDeathMessage mapKillerIfPresent(UnaryOperator<Text> mapper) {
        if (this.killer == null) {
            return this;
        } else {
            return new MissileWarsDeathMessage(this.key, this.victim, mapper.apply(this.killer), this.item, this.isReal);
        }
    }

    public MutableText appearance() {
        Object[] args;
        if (this.killer == null) {
            args = new Text[]{this.victim};
        } else if (this.item == null) {
            args = new Text[]{this.victim, this.killer};
        } else {
            args = new Text[]{this.victim, this.killer, this.item};
        }
        return Text.translatable(this.key, args);
    }
}
