package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.StructureVoidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureVoidBlock.class)
public class StructureVoidBlockMixin extends Block {
	// TANGIBLE_STRUCTURE_VOIDS

	public StructureVoidBlockMixin(Properties settings) {
		super(settings);
	}

	// Actually render the model
	@Inject(at = @At("HEAD"), method = "getRenderShape", cancellable = true)
	private void getRenderShapeInject(CallbackInfoReturnable<RenderShape> cir) {
		if (ModKrowd.CONFIG.TANGIBLE_STRUCTURE_VOIDS.enabled) {
			cir.setReturnValue(RenderShape.MODEL);
		}
	}

	// Full cube focus outline
	@Inject(at = @At("HEAD"), method = "getShape", cancellable = true)
	private void getShapeInject(CallbackInfoReturnable<VoxelShape> cir) {
		if (ModKrowd.CONFIG.TANGIBLE_STRUCTURE_VOIDS.enabled) {
			cir.setReturnValue(Shapes.block());
		}
	}

	// Glass-like rendering behavior
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
    }
}
