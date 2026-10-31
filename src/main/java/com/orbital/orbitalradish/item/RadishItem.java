package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RadishItem extends Item {

    public RadishItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (face != Direction.UP) return InteractionResult.PASS;

        BlockState clickedState = level.getBlockState(clickedPos);
        if (!clickedState.is(Blocks.FARMLAND)) return InteractionResult.PASS;

        BlockPos targetPos = clickedPos.above();

        if (!level.getBlockState(targetPos).isAir()) return InteractionResult.PASS;

        if (!level.isClientSide()) {
            level.setBlock(targetPos, ModBlocks.RADISH_CROP.defaultBlockState(), 3);

            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
