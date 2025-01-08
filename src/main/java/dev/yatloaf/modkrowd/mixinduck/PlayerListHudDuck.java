package dev.yatloaf.modkrowd.mixinduck;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PlayerListHudDuck {
    @Nullable MutableText modKrowd$getHeader();
    @Nullable MutableText modKrowd$getFooter();
    @NotNull List<PlayerListEntry> modKrowd$collectEntries();
}
