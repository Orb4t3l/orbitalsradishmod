package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;

public class RadishArrowItem extends ArrowItem {
    public RadishArrowItem(Item.Properties properties) {
        super(properties);
    }

    // FIXED: Pass the ItemStack to the RadishArrowEntity constructor
    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        // Create RadishArrowEntity with level, shooter, AND the ItemStack (for rendering)
        RadishArrowEntity arrow = new RadishArrowEntity(level, shooter, stack);
        return arrow;
    }
}