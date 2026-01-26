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

    /**
     * Constructor used by the entity factory / deserialization.
     */
    public RadishArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.itemStack = ItemStack.EMPTY;
    }

    /**
     * Convenience constructor used when spawning from code/items (no custom stack).
     * Delegates to the full constructor with an empty ItemStack.
     */
    public RadishArrowEntity(Level level, LivingEntity shooter) {
        this(level, shooter, ItemStack.EMPTY);
    }

    /**
     * Convenience constructor used when spawning from code/items with an ItemStack to render.
     * Example usage: new RadishArrowEntity(level, shooter, stack);
     */
    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack stack) {
        // Use the registered entity type and the shooter-aware constructor so ownership/damage attribution works.
        super(ModEntities.RADISH_ARROW.get(), shooter, level);

        this.itemStack = (stack == null) ? ItemStack.EMPTY : stack.copy();

        // setOwner is redundant when using the shooter-aware super constructor, but harmless to keep.
        this.setOwner(shooter);

        // Optional: tiny cooldown on the shooter's main hand item (defensive, and only if shooter is a player)
        if (shooter instanceof Player player) {
            try {
                ItemStack main = player.getMainHandItem();
                if (!main.isEmpty()) {
                    // 5 ticks ~= 0.25s; adjust as desired (you previously used 5)
                    player.getCooldowns().addCooldown(main.getItem(), 5);
                }
            } catch (Throwable ignored) {
                // defensive: ignore if mappings or method signatures differ across versions
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }

    /**
     * Make the arrow non-pickupable (returns nothing when picked up).
     * If you want players to be able to pick it up, return the appropriate ItemStack here.
     */
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
