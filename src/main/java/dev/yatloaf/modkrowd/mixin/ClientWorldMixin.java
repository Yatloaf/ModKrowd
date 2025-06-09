package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.custom.Custom;
import dev.yatloaf.modkrowd.custom.MissileWarsTieMessage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    // TIE_DETECTOR

    // NetherPortalBlock#onStateReplaced would be preferable but that only gets called on the server

    @Shadow @Final private MinecraftClient client;

    private ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    // These fields are specific to each world, which is perfect for resetting them
    @Unique private long redWinTick = -1;
    @Unique private long greenWinTick = -1;

    @Override
    public void onBlockChanged(BlockPos pos, BlockState oldBlock, BlockState newBlock) {
        super.onBlockChanged(pos, oldBlock, newBlock);

        if (ModKrowd.CONFIG.TIE_DETECTOR.enabled && oldBlock.isOf(Blocks.NETHER_PORTAL)) {

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
                default -> ModKrowd.LOGGER.warn("[ClientWorldMixin] Nether portal broke at strange position: {}!", pos);
            }
        }
    }

    @Unique
    private void sendTieMessage() {
        this.client.inGameHud.getChatHud().addMessage(
                ModKrowd.CONFIG.themeCustom(new MissileWarsTieMessage(this.redWinTick, this.greenWinTick)).text(),
                null,
                Custom.MESSAGE_INDICATOR
        );
    }
}
