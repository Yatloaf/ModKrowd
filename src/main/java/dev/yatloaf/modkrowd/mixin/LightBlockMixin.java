package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightBlock.class)
public class LightBlockMixin extends Block {
	// TANGIBLE_LIGHTS

	public LightBlockMixin(Settings settings) {
		super(settings);
	}

	// Actually render the model
	@Inject(at = @At("HEAD"), method = "getRenderType", cancellable = true)
	private void getRenderTypeInject(CallbackInfoReturnable<BlockRenderType> cir) {
		if (ModKrowd.CONFIG.TANGIBLE_LIGHTS.enabled) {
			cir.setReturnValue(BlockRenderType.MODEL);
		}
	}

	// Full cube focus outline
	@Inject(at = @At("HEAD"), method = "getOutlineShape", cancellable = true)
	private void getOutlineShapeInject(CallbackInfoReturnable<VoxelShape> cir) {
		if (ModKrowd.CONFIG.TANGIBLE_LIGHTS.enabled) {
			cir.setReturnValue(VoxelShapes.fullCube());
		}
	}

	// Fix the collision shape, which relies on the outline shape by default
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	// Glass-like rendering behavior
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
    }
}
