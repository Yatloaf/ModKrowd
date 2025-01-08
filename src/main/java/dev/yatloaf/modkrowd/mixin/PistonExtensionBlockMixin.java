package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonExtensionBlock.class)
public class PistonExtensionBlockMixin extends Block {
	// TANGIBLE_MOVING_PISTONS

	public PistonExtensionBlockMixin(Settings settings) {
		super(settings);
	}

	// Actually render the model
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		if (ModKrowd.CONFIG.TANGIBLE_MOVING_PISTONS.enabled) {
			return BlockRenderType.MODEL;
		} else {
			return BlockRenderType.INVISIBLE;
		}
	}

	// Full cube focus outline
	@Inject(at = @At("HEAD"), method = "getOutlineShape", cancellable = true)
	private void getOutlineShapeInject(CallbackInfoReturnable<VoxelShape> cir) {
		if (ModKrowd.CONFIG.TANGIBLE_MOVING_PISTONS.enabled) {
			cir.setReturnValue(VoxelShapes.fullCube());
		}
	}

	// Glass-like rendering behavior
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
    }
}
