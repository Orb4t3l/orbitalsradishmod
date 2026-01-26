package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;

public class RadishArrowItem extends ArrowItem {
    public RadishArrowItem(Item.Properties properties) {
        super(properties);
    }

    // Optional: if you want special behavior when creating the arrow entity, override createArrow
    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        // create your RadishArrowEntity instance — adapt to your entity constructor
        RadishArrowEntity arrow = new RadishArrowEntity(shooter.level(), shooter);
        arrow.setBaseDamage(arrow.getBaseDamage()); // optionally adjust
        return arrow;
    }
}
