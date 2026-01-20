package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishArrowEntity extends AbstractArrow implements ItemSupplier {

    private final ItemStack itemStack;

    // Used by registration factory (EntityType.Builder)
    public RadishArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.itemStack = ItemStack.EMPTY;
    }

    // Convenience constructor used when spawning from code / items
    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.RADISH_ARROW.get(), level); // entity type must match your ModEntities entry
        this.itemStack = stack == null ? ItemStack.EMPTY : stack.copy();
        this.setOwner(shooter);

        // Apply a tiny cooldown (0.1s => 2 ticks) to the shooter's main-hand item if it's a player.
        if (shooter instanceof Player player) {
            try {
                // Defensive: check main hand item exists and item type is not AIR
                ItemStack main = player.getMainHandItem();
                if (!main.isEmpty()) {
                    player.getCooldowns().addCooldown(main.getItem(), 5);
                }
            } catch (Throwable ignored) {
                // In case mappings differ or method unavailable, silently ignore.
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    /**
     * Return an empty stack so picking up the arrow yields nothing.
     * The entity will still render using getItem() in-flight.
     */
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
