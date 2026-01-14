package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishArrowEntity extends Arrow implements ItemSupplier {

    public RadishArrowEntity(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    public RadishArrowEntity(Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(OrbitalRadishMod.RADISH_ARROW.get());
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(OrbitalRadishMod.RADISH_ARROW.get());
    }
}
