package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import com.orbital.orbitalradish.ModEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishArrowItem extends ArrowItem {
    public RadishArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public Arrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        // Use the (Level, LivingEntity) constructor so it uses the shooter/owner properly
        return new RadishArrowEntity(level, shooter);
    }
}
