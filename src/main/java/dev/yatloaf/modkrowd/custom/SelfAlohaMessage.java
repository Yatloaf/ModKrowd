package dev.yatloaf.modkrowd.custom;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.Aloha;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public record SelfAlohaMessage(Aloha aloha, ServerData info) implements Custom {
    public String nameOrAddress() {
        return this.info.name.equals(I18n.get("selectServer.defaultName")) ? this.info.ip : this.info.name;
    }

    @Override
    public TextCache appearance() {
        return TextCache.of(Component.translatable(
                this.aloha == Aloha.JOIN ? "modkrowd.message.self_joined" : "modkrowd.message.self_left",
                this.nameOrAddress()
        ).setStyle(CKColor.AQUA.style));
    }
}
