package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

public class BlockCutoutFeature extends Feature {
    private final Block[] blocks;

    public BlockCutoutFeature(String id, PredicateIndex allowedPredicates, Block... blocks) {
        super(id, allowedPredicates);
        this.blocks = blocks;
    }

    @Override
    public void onInitEnable(Minecraft minecraft, ActionQueue queue) {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, this.blocks);
    }

    @Override
    public void onEnable(Minecraft minecraft, ActionQueue queue) {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, this.blocks);
        queue.queueReloadChunks();
    }

    @Override
    public void onDisable(Minecraft minecraft, ActionQueue queue) {
        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.SOLID, this.blocks);
        queue.queueReloadChunks();
    }
}
