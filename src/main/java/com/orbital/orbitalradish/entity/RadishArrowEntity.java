package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.ModItems;
import com.orbital.orbitalradish.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RadishArrowEntity extends AbstractArrow {

    private ItemStack renderStack = ItemStack.EMPTY;

    // Constructor for entity type registration - EntityType FIRST, then Level
    public RadishArrowEntity(EntityType<? extends RadishArrowEntity> type, Level level) {
        super(type, level);  // AbstractArrow expects (EntityType, Level)
    }

    // Constructor with shooter - used when spawning the arrow
    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack renderStack) {
        // FIXED: Call super with EntityType and Level instead of this()
        super(ModEntities.RADISH_ARROW.get(), level);
        // Set the owner/shooter
        this.setOwner(shooter);

        // FIXED: Spawn arrow in front of player based on their look direction
        double offsetDistance = 2.0; // 1 block in front
        double offsetX = -Math.sin(Math.toRadians(shooter.getYRot())) * offsetDistance;
        double offsetZ = Math.cos(Math.toRadians(shooter.getYRot())) * offsetDistance;

        this.setPos(
                shooter.getX() + offsetX,
                shooter.getEyeY() - 0.1,
                shooter.getZ() + offsetZ
        );

        // Store the render stack
        this.renderStack = renderStack.copy();
    }

    // Return a vanilla STICK instead of radish_arrow when picked up
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.STICK);
    }

    // Optional: If you need the render stack elsewhere
    public ItemStack getRenderStack() {
        return renderStack;
    }
}