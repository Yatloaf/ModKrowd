package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;

public class BlockCutoutFeature extends Feature {
    private final Block[] blocks;

    public BlockCutoutFeature(String id, PredicateIndex allowedPredicates, Block... blocks) {
        super(id, allowedPredicates);
        this.blocks = blocks;
    }

    @Override
    public void onInitEnable(MinecraftClient client, ActionQueue queue) {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), this.blocks);
    }

    @Override
    public void onEnable(MinecraftClient client, ActionQueue queue) {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), this.blocks);
        queue.queueReloadChunks();
    }

    @Override
    public void onDisable(MinecraftClient client, ActionQueue queue) {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getSolid(), this.blocks);
        queue.queueReloadChunks();
    }
}
