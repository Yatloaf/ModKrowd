package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.message.Aloha;
import dev.yatloaf.modkrowd.custom.SelfAlohaMessage;
import dev.yatloaf.modkrowd.mixin.ClientCommonNetworkHandlerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeparateChatHistoryFeature extends Feature {
    private static final ChatHud.ChatState EMPTY_CHAT_STATE = new ChatHud.ChatState(List.of(), List.of(), List.of());

    private final Map<Location, ChatHud.ChatState> chatStates = new HashMap<>();
    private Location currentLocation = null;

    public SeparateChatHistoryFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onEnable(MinecraftClient client, ActionQueue queue) {
        ClientPlayNetworkHandler handler = client.getNetworkHandler();
        if (handler != null) {
            this.chatStates.put(Location.of(handler.getServerInfo()), client.inGameHud.getChatHud().toChatState());
        }
    }

    @Override
    public void onDisable(MinecraftClient client, ActionQueue queue) {
        this.chatStates.clear();
    }

    @Override
    public void onConfigurationComplete(ClientConfigurationNetworkHandler handler, MinecraftClient client, ActionQueue queue) {
        Location location = Location.of(((ClientCommonNetworkHandlerAccessor) handler).getServerInfo());
        if (!location.equals(this.currentLocation)) {
            this.currentLocation = location;
            client.inGameHud.getChatHud().restoreChatState(this.chatStates.getOrDefault(location, EMPTY_CHAT_STATE));
            if (location instanceof Server server) {
                client.inGameHud.getChatHud().addMessage(ModKrowd.CONFIG.themeCustom(new SelfAlohaMessage(Aloha.JOIN, server.info)).text());
            }
        }
    }

    @Override
    public void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client, ActionQueue queue) {
        Location location = Location.of(handler.getServerInfo());
        if (location instanceof Server server) {
            client.inGameHud.getChatHud().addMessage(ModKrowd.CONFIG.themeCustom(new SelfAlohaMessage(Aloha.LEAVE, server.info)).text());
        }
        this.chatStates.put(location, client.inGameHud.getChatHud().toChatState());
        this.currentLocation = null;
    }

    private interface Location {
        Location ELSEWHERE = new Location() {};

        private static Location of(ServerInfo info) {
            return info == null ? ELSEWHERE : new Server(info);
        }
    }

    private record Server(ServerInfo info) implements Location {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Server server && this.info.address.equals(server.info.address);
        }

        @Override
        public int hashCode() {
            return this.info.address.hashCode();
        }
    }
}
