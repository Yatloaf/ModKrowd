package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.queue.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.message.Aloha;
import dev.yatloaf.modkrowd.custom.SelfAlohaMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;

import java.util.HashMap;
import java.util.Map;

public class SeperateChatHistoryFeature extends Feature {

    private final Map<String, ChatHud.ChatState> chatStates;
    private String currentAddress = null;

    public SeperateChatHistoryFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
        this.chatStates = new HashMap<>();
    }

    @Override
    public void onEnable(MinecraftClient client, ActionQueue queue) {
        ClientPlayNetworkHandler handler = client.getNetworkHandler();
        if (handler != null) {
            ServerInfo info = handler.getServerInfo();
            if (info != null) {
                this.chatStates.put(info.address, client.inGameHud.getChatHud().toChatState());
            }
        }
    }

    @Override
    public void onDisable(MinecraftClient client, ActionQueue queue) {
        this.chatStates.clear();
    }

    @Override
    public void onJoin(ClientPlayNetworkHandler handler, MinecraftClient client, ActionQueue queue) {
        ServerInfo info = handler.getServerInfo();
        if (info != null) {
            if (!info.address.equals(this.currentAddress)) {
                this.currentAddress = info.address;
                if (this.chatStates.containsKey(info.address)) {
                    client.inGameHud.getChatHud().restoreChatState(this.chatStates.get(info.address));
                }
                client.inGameHud.getChatHud().addMessage(ModKrowd.CONFIG.themeCustom(new SelfAlohaMessage(Aloha.JOIN, info)).text());
            }
        }
    }

    @Override
    public void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client, ActionQueue queue) {
        ServerInfo info = handler.getServerInfo();
        if (info != null) {
            client.inGameHud.getChatHud().addMessage(ModKrowd.CONFIG.themeCustom(new SelfAlohaMessage(Aloha.LEAVE, info)).text());
            this.chatStates.put(info.address, client.inGameHud.getChatHud().toChatState());
        }
        this.currentAddress = null;
    }
}
