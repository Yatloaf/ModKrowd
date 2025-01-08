package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin extends Block {
	// TANGIBLE_END_PORTALS

	public EndPortalBlockMixin(Settings settings) {
		super(settings);
	}

	// Actually render the model
	@Override
	public BlockRenderType getRenderType(BlockState state) {
        return ModKrowd.CONFIG.TANGIBLE_END_PORTALS.enabled ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
	}

	// Glass-like rendering behavior
	@Override
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
	}
}
