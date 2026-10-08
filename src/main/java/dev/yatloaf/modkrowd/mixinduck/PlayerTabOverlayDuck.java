package dev.yatloaf.modkrowd.mixinduck;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface PlayerTabOverlayDuck {
    @Nullable MutableComponent modKrowd$getHeader();
    @Nullable MutableComponent modKrowd$getFooter();
    @NonNull List<PlayerInfo> modKrowd$getPlayerInfos();
}
