package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

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

        BlockState clickedState = level.getBlockState(clickedPos);
        boolean isFarmland = clickedState.is(Blocks.FARMLAND);

        if (isFarmland && face == Direction.UP) {
            BlockPos targetPos = clickedPos.above();

            if (!level.getBlockState(targetPos).isAir()) {
                return InteractionResult.FAIL;
            }

            if (!level.isClientSide()) {
                level.setBlock(targetPos, OrbitalRadishMod.RADISH_CROP.get().defaultBlockState(), 3);

                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }

            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}