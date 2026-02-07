package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndGatewayBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndGatewayBlock.class)
public class EndGatewayBlockMixin extends Block {
	// TANGIBLE_END_PORTALS

	public EndGatewayBlockMixin(Properties settings) {
		super(settings);
	}

	// Actually render the model
	@Override
	public @NotNull RenderShape getRenderShape(BlockState state) {
        return Features.TANGIBLE_END_PORTALS.active ? RenderShape.MODEL : RenderShape.INVISIBLE;
	}

	// Glass-like rendering behavior
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
	}
}
