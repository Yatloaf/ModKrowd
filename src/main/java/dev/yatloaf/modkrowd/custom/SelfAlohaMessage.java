package dev.yatloaf.modkrowd.custom;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.Aloha;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public record SelfAlohaMessage(Aloha aloha, ServerInfo info) implements Custom {
    public String nameOrAddress() {
        return this.info.name.equals(I18n.translate("selectServer.defaultName")) ? this.info.address : this.info.name;
    }

    @Override
    public TextCache appearance() {
        return TextCache.of(Text.literal((this.aloha == Aloha.JOIN ? "Joined " : "Left ") + this.nameOrAddress()).formatted(Formatting.AQUA));
    }
}
