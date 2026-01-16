package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishArrowItem extends ArrowItem {

    public RadishArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new RadishArrowEntity(level, shooter, stack);
    }
}
