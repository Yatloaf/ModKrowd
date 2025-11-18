package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.message.Aloha;
import dev.yatloaf.modkrowd.custom.Custom;
import dev.yatloaf.modkrowd.custom.SelfAlohaMessage;
import dev.yatloaf.modkrowd.mixin.ClientCommonPacketListenerImplAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeparateChatHistoryFeature extends Feature {
    private static final ChatComponent.State EMPTY_CHAT_STATE = new ChatComponent.State(List.of(), List.of(), List.of());

    private final Map<Location, ChatComponent.State> chatStates = new HashMap<>();
    private Location currentLocation = null;

    public SeparateChatHistoryFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onEnable(Minecraft minecraft, ActionQueue queue) {
        ClientPacketListener listener = minecraft.getConnection();
        if (listener != null) {
            this.chatStates.put(Location.of(listener.getServerData()), minecraft.gui.getChat().storeState());
        }
    }

    @Override
    public void onDisable(Minecraft minecraft, ActionQueue queue) {
        this.chatStates.clear();
    }

    @Override
    public void onConfigurationComplete(ClientConfigurationPacketListenerImpl listener, Minecraft minecraft, ActionQueue queue) {
        Location location = Location.of(((ClientCommonPacketListenerImplAccessor) listener).getServerData());
        if (!location.equals(this.currentLocation)) {
            this.currentLocation = location;
            minecraft.gui.getChat().restoreState(this.chatStates.getOrDefault(location, EMPTY_CHAT_STATE));
            if (location instanceof Server server) {
                minecraft.gui.getChat().addMessage(
                        ModKrowd.CONFIG.themeCustom(new SelfAlohaMessage(Aloha.JOIN, server.info)).text(),
                        null,
                        Custom.MESSAGE_INDICATOR
                );
            }
        }
    }

    @Override
    public void onDisconnect(ClientPacketListener listener, Minecraft minecraft, ActionQueue queue) {
        Location location = Location.of(listener.getServerData());
        if (location instanceof Server server) {
            minecraft.gui.getChat().addMessage(
                    ModKrowd.CONFIG.themeCustom(new SelfAlohaMessage(Aloha.LEAVE, server.info)).text(),
                    null,
                    Custom.MESSAGE_INDICATOR
            );
        }
        this.chatStates.put(location, minecraft.gui.getChat().storeState());
        this.currentLocation = null;
    }

    private interface Location {
        Location ELSEWHERE = new Location() {};

        private static Location of(ServerData info) {
            return info == null ? ELSEWHERE : new Server(info);
        }
    }

    private record Server(ServerData info) implements Location {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Server server && this.info.ip.equals(server.info.ip);
        }

        @Override
        public int hashCode() {
            return this.info.ip.hashCode();
        }
    }
}
