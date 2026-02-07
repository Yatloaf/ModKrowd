package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrierBlock.class)
public class BarrierBlockMixin extends Block {
	// TANGIBLE_BARRIERS

	// Don't block vision
	public BarrierBlockMixin(Properties settings) {
		super(settings.isViewBlocking(Blocks::never));
	}

	// Actually render the model
	@Inject(method = "getRenderShape", cancellable = true, at = @At("HEAD"))
	private void getRenderShapeInject(CallbackInfoReturnable<RenderShape> cir) {
		if (Features.TANGIBLE_BARRIERS.active) {
			cir.setReturnValue(RenderShape.MODEL);
		}
	}

	// Glass-like rendering behavior
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
    }
}
