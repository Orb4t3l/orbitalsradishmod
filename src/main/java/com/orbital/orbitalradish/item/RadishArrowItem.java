package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.RadishArrowEntity;
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
    public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter) {
        RadishArrowEntity ent = new RadishArrowEntity(world, shooter);
        // Optionally tweak damage/knockback here
        ent.setBaseDamage(2.0D); // example base damage; adjust as desired
        return ent;
    }
}
