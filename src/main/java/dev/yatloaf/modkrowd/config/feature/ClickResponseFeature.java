package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.CubeKrowdMessageCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;

public class ClickResponseFeature extends Feature {
    public ClickResponseFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onMessage(MessageCache message, MinecraftClient client, ActionQueue queue) {
        if (message instanceof CubeKrowdMessageCache ckMessage) {
            DirectMessage dm = ckMessage.directMessageFast();
            if (dm.isReal()) {
                message.setThemed(TextCache.of(ckMessage.original.text().copy().styled(style -> style.withClickEvent(
                        new ClickEvent.SuggestCommand("/msg " + dm.other() + " ")
                ))));
            }
        }
    }
}
