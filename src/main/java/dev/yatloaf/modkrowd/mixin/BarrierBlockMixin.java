package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrierBlock.class)
public class BarrierBlockMixin extends Block {
	// TANGIBLE_BARRIERS

	// Don't block vision
	public BarrierBlockMixin(Settings settings) {
		super(settings.blockVision(Blocks::never));
	}

	// Actually render the model
	@Inject(method = "getRenderType", cancellable = true, at = @At("HEAD"))
	private void getRenderTypeInject(CallbackInfoReturnable<BlockRenderType> cir) {
		if (ModKrowd.CONFIG.TANGIBLE_BARRIERS.enabled) {
			cir.setReturnValue(BlockRenderType.MODEL);
		}
	}

	// Glass-like rendering behavior
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
    }
}
