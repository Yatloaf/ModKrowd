package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.custom.Custom;
import dev.yatloaf.modkrowd.custom.MissileWarsTieMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    // TIE_DETECTOR

    // NetherPortalBlock#onStateReplaced would be preferable but that only gets called on the server

    @Shadow @Final private Minecraft minecraft;

    private ClientLevelMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    // These fields are specific to each world, which is perfect for resetting them
    @Unique private long redWinTick = -1;
    @Unique private long greenWinTick = -1;

    @Override
    public void updatePOIOnBlockStateChange(BlockPos pos, BlockState oldBlock, BlockState newBlock) {
        super.updatePOIOnBlockStateChange(pos, oldBlock, newBlock);

        if (ModKrowd.CONFIG.TIE_DETECTOR.enabled && oldBlock.is(Blocks.NETHER_PORTAL)) {

            switch (pos.getZ()) {
                case 72 -> {
                    if (this.redWinTick == -1) {
                        this.redWinTick = ModKrowd.tick;

                        if (this.greenWinTick != -1) {
                            this.sendTieMessage();
                        }
                    }
                }
                case -72 -> {
                    if (this.greenWinTick == -1) {
                        this.greenWinTick = ModKrowd.tick;

                        if (this.redWinTick != -1) {
                            this.sendTieMessage();
                        }
                    }
                }
                default -> ModKrowd.LOGGER.warn("[ClientLevelMixin] Nether portal broke at strange position: {}!", pos);
            }
        }
    }

    @Unique
    private void sendTieMessage() {
        this.minecraft.gui.getChat().addMessage(
                ModKrowd.CONFIG.themeCustom(new MissileWarsTieMessage(this.redWinTick, this.greenWinTick)).text(),
                null,
                Custom.MESSAGE_INDICATOR
        );
    }
}
