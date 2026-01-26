package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * RadishStickItem acts like a bow that uses vanilla sticks as ammo.
 * This implementation applies bow enchantments correctly (Power, Punch, Flame, Infinity).
 */
public class RadishStickItem extends BowItem {

    public RadishStickItem(Properties props) {
        super(props);
    }

    /**
     * We advertise supported projectiles as sticks so the vanilla UI treats it like a bow.
     */
    @Override
    public java.util.function.Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() == Items.STICK;
    }

    /**
     * Called when the player releases the bow charge.
     * This implementation:
     * - finds/consumes a stick (unless Infinity is present),
     * - spawns a RadishArrowEntity,
     * - applies enchantments (Power, Punch, Flame),
     * - plays sound and adds a small cooldown.
     */
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        // Read enchantments from the bow item (the 'stack' parameter)
        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
        int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack);
        int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack);

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
                // search inventory (non-armor slots)
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

        // If no stick found, nothing to do (no ammo) — unless player is in creative
        boolean hasAmmo = !found.isEmpty() || player.getAbilities().instabuild;
        if (!hasAmmo) {
            return;
        }

        // Infinity behavior: if the bow has Infinity, do NOT consume ammo (unless player is in creative)
        boolean shouldConsume = !player.getAbilities().instabuild && infinityLevel == 0;

        // Consume one stick unless Infinity is present or in creative
        if (shouldConsume && !found.isEmpty()) {
            if (foundHand != null) {
                // shrink the hand-held stack
                player.getItemInHand(foundHand).shrink(1);
            } else {
                // shrink inventory slot (found points into the inventory list)
                found.shrink(1);
            }
        }

        // Spawn the radish arrow entity carrying a stick (or empty if none)
        ItemStack renderStack = found.isEmpty() ? ItemStack.EMPTY : new ItemStack(Items.STICK);
        RadishArrowEntity arrow = new RadishArrowEntity(level, player, renderStack);

        // Base damage: start with a small default and apply Power enchantment bonus
        // We'll keep the default base as 1.0 (half-heart) like your original.
        double baseDamage = 1.0D;
        if (powerLevel > 0) {
            // Vanilla extra damage formula ~ 0.5 * power + 0.5
            baseDamage += 0.5D * powerLevel + 0.5D;
        }

        try {
            arrow.setBaseDamage(baseDamage);
        } catch (Throwable ignored) {
            // fallback: ignore if mappings differ
        }

        // Apply Punch (knockback)
        if (punchLevel > 0) {
            try {
                arrow.setKnockback(punchLevel);
            } catch (Throwable ignored) {
            }
        }

        // Apply Flame
        if (flameLevel > 0) {
            try {
                arrow.setSecondsOnFire(100); // 5 seconds (100 ticks)
            } catch (Throwable ignored) {
            }
        }

        // Position, shoot and add to world
        arrow.setPos(player.getX(), player.getEyeY() - 0.10000000149011612D, player.getZ());

        // Fixed velocity & inaccuracy; you can compute charge-based velocity if you want
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 1.0F);

        // If the bow has Infinity, set the pickup behavior to CREATIVE_ONLY (so arrows don't drop for pickup)
        // This mirrors vanilla's behavior but is optional; if the method doesn't exist on your mappings we ignore it.
        try {
            if (infinityLevel > 0) {
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY; // mapping may vary; defensive
            }
        } catch (Throwable ignored) {
        }

        level.addFreshEntity(arrow);

        // Small sound feedback
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);

        // Small cooldown on the shooter's main-hand item (defensive; only if shooter is a player)
        if (!player.getAbilities().instabuild) {
            try {
                ItemStack mainNow = player.getMainHandItem();
                if (!mainNow.isEmpty()) {
                    player.getCooldowns().addCooldown(mainNow.getItem(), 5);
                }
            } catch (Throwable ignored) {
            }
        }
    }
}
