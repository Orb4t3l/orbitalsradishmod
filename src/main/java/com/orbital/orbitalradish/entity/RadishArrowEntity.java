package com.orbital.orbitalradish.entity;

import com.orbital.orbitalradish.ModEntities;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class RadishArrowEntity extends AbstractArrow {

    private static final Identifier TAKE_AIM = Identifier.fromNamespaceAndPath("minecraft", "adventure/shoot_arrow");

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
            AdvancementHolder adv = player.level().getServer().getAdvancements().get(TAKE_AIM);
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

    public void setInitialRotationFromMovement() {
        Vec3 motion = this.getDeltaMovement();
        double horizontal = motion.horizontalDistance();
        this.setYRot((float) (Mth.atan2(motion.x, motion.z) * (180D / Math.PI)));
        this.setXRot((float) (Mth.atan2(motion.y, horizontal) * (180D / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.STICK);
    }

    public ItemStack getRenderStack() {
        return renderStack;
    }
}
