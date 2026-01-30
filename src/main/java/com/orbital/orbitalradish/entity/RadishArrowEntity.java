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
        // Call the other constructor with the registered entity type
        this(ModEntities.RADISH_ARROW.get(), level);
        // Set the owner/shooter
        this.setOwner(shooter);
        // Store the render stack
        this.renderStack = renderStack.copy();
    }

    // FIXED: Return a vanilla STICK instead of radish_arrow when picked up
    // This makes sense since the RadishStick uses sticks as ammo
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.STICK);  // Return vanilla stick, not radish arrow
    }

    // Optional: If you need the render stack elsewhere
    public ItemStack getRenderStack() {
        return renderStack;
    }
}