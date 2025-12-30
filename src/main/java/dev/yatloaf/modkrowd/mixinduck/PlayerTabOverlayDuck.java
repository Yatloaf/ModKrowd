package dev.yatloaf.modkrowd.mixinduck;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PlayerTabOverlayDuck {
    @Nullable MutableComponent modKrowd$getHeader();
    @Nullable MutableComponent modKrowd$getFooter();
    @NotNull List<PlayerInfo> modKrowd$collectEntries();
}
