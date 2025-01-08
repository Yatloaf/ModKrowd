package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.queue.ActionQueue;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
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
    public void onMessage(MessageCache message, boolean overlay, MinecraftClient client, ActionQueue queue) {
        if (message instanceof CubeKrowdMessageCache ckMessage) {
            DirectMessage dm = ckMessage.directMessageFast();
            if (dm.isReal()) {
                switch (dm.direction()) {
                    case INCOMING -> message.setThemed(TextCache.of(ckMessage.original.text().copy().styled(style -> style.withClickEvent(
                            new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + dm.sender() + " ")
                    ))));
                    case OUTGOING -> message.setThemed(TextCache.of(ckMessage.original.text().copy().styled(style -> style.withClickEvent(
                            new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + dm.recipient() + " ")
                    ))));
                    case LOOP -> message.setThemed(TextCache.of(ckMessage.original.text().copy().styled(style -> style.withClickEvent(
                            new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + SelfPlayer.username() + " ")
                    ))));
                }
            }
        }
    }
}
