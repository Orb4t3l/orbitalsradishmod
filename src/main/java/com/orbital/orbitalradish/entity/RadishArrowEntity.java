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

    public RadishArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.itemStack = ItemStack.EMPTY;
    }

    public RadishArrowEntity(Level level, LivingEntity shooter) {
        this(level, shooter, ItemStack.EMPTY);
    }

    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.RADISH_ARROW, shooter, level);

        this.itemStack = (stack == null) ? ItemStack.EMPTY : stack.copy();
        this.setOwner(shooter);

        if (shooter instanceof Player player) {
            ItemStack main = player.getMainHandItem();
            if (!main.isEmpty()) {
                player.getCooldowns().addCooldown(main.getItem(), 5);
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
