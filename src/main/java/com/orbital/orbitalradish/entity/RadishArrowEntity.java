package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadishArrowEntity extends AbstractArrow implements ItemSupplier {

    private ItemStack itemStack = ItemStack.EMPTY;

    /**
     * Entity factory / deserialization constructor.
     * Many builds require a (EntityType, Level) ctor for the entity factory — delegate to the main ctor.
     */
    public RadishArrowEntity(EntityType<? extends RadishArrowEntity> type, Level level) {
        // This delegates to the constructor below which calls super(type, level, ItemStack)
        this(type, level, ItemStack.EMPTY);
    }

    /**
     * Primary constructor used when the vanilla/forge constructor signature requires an ItemStack.
     * Matches AbstractArrow(EntityType, Level, ItemStack).
     */
    public RadishArrowEntity(EntityType<? extends RadishArrowEntity> type, Level level, ItemStack stack) {
        // Pass the ItemStack you want the arrow to carry/use to the super constructor.
        super(type, level, stack == null ? ItemStack.EMPTY : stack.copy());
        this.itemStack = (stack == null || stack.isEmpty()) ? ItemStack.EMPTY : stack.copy();
    }

    /**
     * Convenience constructor: spawn from code with a shooter and optional itemstack.
     * Note: we call the (EntityType, Level, ItemStack) ctor and then set the owner.
     */
    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack stack) {
        this(ModEntities.RADISH_ARROW.get(), level, stack);
        if (shooter != null) {
            // set the shooter/owner on the arrow after construction
            this.setOwner(shooter);
        }
    }

    /**
     * Convenience ctor: spawn with shooter and no custom stack.
     */
    public RadishArrowEntity(Level level, LivingEntity shooter) {
        this(level, shooter, ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }

    @Override
    protected ItemStack getPickupItem() {
        // return the stack given to players when picking the arrow up; change if you want a radish item
        return ItemStack.EMPTY;
    }

    // Optional: if you want the arrow to carry custom NBT/item data for rendering or behavior,
    // ensure you copy the stack and handle serialization (saveAdditional/load) as needed.
}
