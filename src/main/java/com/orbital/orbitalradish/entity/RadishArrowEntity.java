package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishArrowEntity extends AbstractArrow implements ItemSupplier {

    private ItemStack itemStack = ItemStack.EMPTY;

    // Used by registration factory (EntityType.Builder)
    public RadishArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    // Convenience constructor used by RadishArrowItem.createArrow and RadishStickItem
    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.RADISH_ARROW.get(), level); // ensure matches your ModEntities entry
        this.itemStack = stack == null ? ItemStack.EMPTY : stack.copy();
        this.setOwner(shooter);
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    protected ItemStack getPickupItem() {
        return itemStack.copy();
    }
}
