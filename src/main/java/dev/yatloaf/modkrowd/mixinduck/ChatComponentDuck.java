package dev.yatloaf.modkrowd.mixinduck;

import dev.yatloaf.modkrowd.cubekrowd.message.MessageCache;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.jspecify.annotations.Nullable;

public interface ChatComponentDuck {
    MessageCache modKrowd$getMessageAt(double x, double y);

    void modKrowd$addMessage(Component contents, @Nullable MessageSignature signature, GuiMessageSource source, @Nullable GuiMessageTag tag);
}
