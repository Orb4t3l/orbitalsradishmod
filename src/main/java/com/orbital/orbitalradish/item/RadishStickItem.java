package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import com.orbital.orbitalradish.ModEntities; // for the entity type
import com.orbital.orbitalradish.OrbitalRadishMod; // for the registry item (if you use it)
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishStickItem extends Item {
    public RadishStickItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // Create the arrow entity with the item stack we want it to carry.
            // Prefer to use the item stack of the radish arrow item if you registered one.
            ItemStack arrowStack;
            try {
                // If you registered the item in your main class as OrbitalRadishMod.RADISH_ARROW:
                arrowStack = new ItemStack(OrbitalRadishMod.RADISH_ARROW.get());
            } catch (Throwable t) {
                // fallback to whatever stack is in hand (useful for quick tests)
                arrowStack = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
            }

            RadishArrowEntity arrow = new RadishArrowEntity(level, player, arrowStack);
            arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);

            // Debug log so we know the code executed
            System.out.println("[RadishStickItem] spawning arrow with stack: " + (arrow.getItem().isEmpty() ? "EMPTY" : arrow.getItem().getItem().toString()));

            level.addFreshEntity(arrow);
            if (player != null) {
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
