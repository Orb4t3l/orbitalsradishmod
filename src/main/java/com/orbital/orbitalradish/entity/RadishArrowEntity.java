package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.ModEntities;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class RadishArrowEntity extends AbstractArrow {

    private static final ResourceLocation TAKE_AIM = ResourceLocation.fromNamespaceAndPath("minecraft", "adventure/shoot_arrow");

    private ItemStack renderStack = ItemStack.EMPTY;

    public RadishArrowEntity(EntityType<? extends RadishArrowEntity> type, Level level) {
        super(type, level);
    }

    public RadishArrowEntity(Level level, LivingEntity shooter, ItemStack renderStack) {
        super(ModEntities.RADISH_ARROW, level);
        this.setOwner(shooter);

        double offsetDistance = 2.0;
        double offsetX = -Math.sin(Math.toRadians(shooter.getYRot())) * offsetDistance;
        double offsetZ = Math.cos(Math.toRadians(shooter.getYRot())) * offsetDistance;

        this.setPos(
                shooter.getX() + offsetX,
                shooter.getEyeY() - 0.1,
                shooter.getZ() + offsetZ
        );

        this.renderStack = renderStack.copy();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (this.getOwner() instanceof ServerPlayer player) {
            AdvancementHolder adv = player.server.getAdvancements().get(TAKE_AIM);
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
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.STICK);
    }

    public ItemStack getRenderStack() {
        return renderStack;
    }
}
