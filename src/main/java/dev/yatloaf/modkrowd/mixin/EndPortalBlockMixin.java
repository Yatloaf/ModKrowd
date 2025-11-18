package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin extends Block {
	// TANGIBLE_END_PORTALS

	public EndPortalBlockMixin(Properties settings) {
		super(settings);
	}

	// Actually render the model
	@Override
	public @NotNull RenderShape getRenderShape(BlockState state) {
        return ModKrowd.CONFIG.TANGIBLE_END_PORTALS.enabled ? RenderShape.MODEL : RenderShape.INVISIBLE;
	}

	// Glass-like rendering behavior
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
	}
}
