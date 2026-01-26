package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RadishStickItem extends BowItem {

    public RadishStickItem(Properties props) {
        super(props);
    }


    /**We still advertise supported projectiles as sticks so the vanilla UI treats it like a bow. */

    @Override
    public java.util.function.Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() == Items.STICK;
    }

    /**
     * Called when the player releases the bow charge (or we can call it directly).
     * We override to consume a vanilla stick from the player's inventory (main/offhand first),
     * spawn a RadishArrowEntity whose damage is set to 1.0 (half a heart), and play a sound.
     */
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        // Find a stick in main hand, offhand, or inventory
        ItemStack found = ItemStack.EMPTY;
        InteractionHand foundHand = null;

        ItemStack main = player.getMainHandItem();
        if (main.getItem() == Items.STICK) {
            found = main;
            foundHand = InteractionHand.MAIN_HAND;
        } else {
            ItemStack off = player.getOffhandItem();
            if (off.getItem() == Items.STICK) {
                found = off;
                foundHand = InteractionHand.OFF_HAND;
            } else {
                // search inventory (nonArmor slots)
                NonNullList<ItemStack> items = player.getInventory().items;
                for (int i = 0; i < items.size(); i++) {
                    ItemStack s = items.get(i);
                    if (s.getItem() == Items.STICK) {
                        found = s;
                        break;
                    }
                }
            }
        }

        // If no stick found, nothing to do (no ammo)
        if (found == null || found.isEmpty()) {
            return;
        }

        // Consume one stick unless in creative
        if (!player.getAbilities().instabuild) {
            if (foundHand != null) {
                // shrink the hand-held stack
                player.getItemInHand(foundHand).shrink(1);
            } else {
                // shrink inventory slot (found points into the inventory list)
                found.shrink(1);
            }
        }

        // Spawn the radish arrow entity carrying a stick (so it visually looks like whatever you want)
        RadishArrowEntity arrow = new RadishArrowEntity(level, player, new ItemStack(Items.STICK));

        // Set damage to 1.0 (half a heart). If you want smaller/larger, change this value.
        try {
            // This is the standard API on AbstractArrow in 1.20.x
            arrow.setBaseDamage(1.0D);
        } catch (Throwable ignored) {
            // If your mappings differ, you can set it via reflection or change to arrow.setDamage(...)
        }

        // Position, shoot and add to world
        arrow.setPos(player.getX(), player.getEyeY() - 0.10000000149011612D, player.getZ());
        // Fixed velocity & inaccuracy; you can compute charge-based velocity if you want
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 1.0F);
        level.addFreshEntity(arrow);

        // Small sound feedback
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
