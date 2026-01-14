package com.orbital.orbitalradish.entity; // adjust if you use a different package

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;

public class RadishArrowEntity extends Arrow {

    // Used by the EntityType builder (EntityType::create)
    public RadishArrowEntity(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
    }

    // Convenience constructor used when creating an arrow fired by an entity
    public RadishArrowEntity(Level level, LivingEntity shooter) {
        super(level, shooter);
    }

    // Optional: override getPickupItem() if you want it to drop your radish-arrow item
    // @Override
    // protected ItemStack getPickupItem() {
    //     return new ItemStack(OrbitalRadishMod.RADISH_ARROW.get());
    // }
}
