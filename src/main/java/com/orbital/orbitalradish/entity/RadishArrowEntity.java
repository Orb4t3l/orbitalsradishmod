package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.ModEntities;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class RadishArrowEntity extends AbstractArrow implements ItemSupplier {

    private static final ResourceLocation TAKE_AIM = new ResourceLocation("minecraft", "adventure/shoot_arrow");

    private final ItemStack itemStack;

    public RadishArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.itemStack = ItemStack.EMPTY;
    }

    public RadishArrowEntity(Level level, LivingEntity shooter) {
        this(level, shooter, ItemStack.EMPTY);
    }

    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack stack) {
        super(ModEntities.RADISH_ARROW, shooter, level);

        this.itemStack = (stack == null) ? ItemStack.EMPTY : stack.copy();
        this.setOwner(shooter);

        if (shooter instanceof Player player) {
            ItemStack main = player.getMainHandItem();
            if (!main.isEmpty()) {
                player.getCooldowns().addCooldown(main.getItem(), 5);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (this.getOwner() instanceof ServerPlayer player) {
            Advancement adv = player.server.getAdvancements().getAdvancement(TAKE_AIM);
            if (adv != null) {
                AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
                if (!progress.isDone()) {
                    for (String criterion : progress.getRemainingCriteria()) {
                        player.getAdvancements().award(adv, criterion);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
