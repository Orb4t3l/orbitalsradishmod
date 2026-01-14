package com.orbital.orbitalradish;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishArrowEntity extends Arrow {
    // Standard constructor used by the entity type builder
    public RadishArrowEntity(EntityType<? extends RadishArrowEntity> type, Level world) {
        super((EntityType<? extends Arrow>) type, world);
    }

    // Convenience constructor used when creating an arrow fired by a living entity
    public RadishArrowEntity(Level world, LivingEntity shooter) {
        this(ModEntities.RADISH_ARROW.get(), world);
        // set shooter/owner and position similar to vanilla arrow creation
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
    }

    @Override
    protected ItemStack getPickupItem() {
        // What item is dropped/picked up when the arrow is on the ground
        return new ItemStack(OrbitalRadishMod.RADISH_ARROW.get());
    }
}
