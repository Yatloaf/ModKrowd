package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MovingPistonBlock.class)
public class MovingPistonBlockMixin extends Block {
	// TANGIBLE_MOVING_PISTONS

	public MovingPistonBlockMixin(Properties settings) {
		super(settings);
	}

	// Actually render the model
	@Override
	public @NotNull RenderShape getRenderShape(BlockState state) {
		if (Features.TANGIBLE_MOVING_PISTONS.active) {
			return RenderShape.MODEL;
		} else {
			return RenderShape.INVISIBLE;
		}
	}

	// Full cube focus outline
	@Inject(at = @At("HEAD"), method = "getShape", cancellable = true)
	private void getShapeInject(CallbackInfoReturnable<VoxelShape> cir) {
		if (Features.TANGIBLE_MOVING_PISTONS.active) {
			cir.setReturnValue(Shapes.block());
		}
	}

	// Glass-like rendering behavior
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
    }
}
