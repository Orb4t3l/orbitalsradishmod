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

        // Check if clicking block is farmland
        BlockState clickedState = level.getBlockState(clickedPos);
        boolean isFarmland = clickedState.is(Blocks.FARMLAND);

        // FIXED: If targeting farmland, handle it completely (don't PASS to eating)
        if (isFarmland && face == Direction.UP) {
            BlockPos targetPos = clickedPos.above(); // place crop on top of farmland

            // If target position is not air, can't plant but still consume the interaction
            // This prevents eating when clicking farmland
            if (!level.getBlockState(targetPos).isAir()) {
                return InteractionResult.FAIL;
            }

            // Plant the radish crop
            if (!level.isClientSide()) {
                level.setBlock(targetPos, OrbitalRadishMod.RADISH_CROP.get().defaultBlockState(), 3);

                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        // Only PASS if NOT targeting farmland (allows eating in other situations)
        return InteractionResult.PASS;
    }
}