package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightBlock.class)
public class LightBlockMixin extends Block {
	// TANGIBLE_LIGHTS

	public LightBlockMixin(Properties settings) {
		super(settings);
	}

	// Actually render the model
	@Inject(at = @At("HEAD"), method = "getRenderShape", cancellable = true)
	private void getRenderShapeInject(CallbackInfoReturnable<RenderShape> cir) {
		if (Features.TANGIBLE_LIGHTS.active) {
			cir.setReturnValue(RenderShape.MODEL);
		}
	}

	// Full cube focus outline
	@Inject(at = @At("HEAD"), method = "getShape", cancellable = true)
	private void getShapeInject(CallbackInfoReturnable<VoxelShape> cir) {
		if (Features.TANGIBLE_LIGHTS.active) {
			cir.setReturnValue(Shapes.block());
		}
	}

	// Fix the collision shape, which relies on the outline shape by default
	@Override
	public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	// Glass-like rendering behavior
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
    }
}
