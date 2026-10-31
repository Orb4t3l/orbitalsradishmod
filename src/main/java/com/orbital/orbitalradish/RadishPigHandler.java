package com.orbital.orbitalradish;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RadishPigHandler {

    private static final Set<UUID> HAS_TEMPT_GOAL = ConcurrentHashMap.newKeySet();

    public static void init() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (!(entity instanceof Pig pig)) return;

            UUID id = pig.getUUID();
            if (HAS_TEMPT_GOAL.contains(id)) return;
            if (!(pig instanceof PathfinderMob pm)) return;

            Ingredient radishIngredient = Ingredient.of(ModItems.RADISH);
            pm.goalSelector.addGoal(3, new TemptGoal(pm, 1.25D, radishIngredient, false));
            HAS_TEMPT_GOAL.add(id);
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClientSide()) return InteractionResult.PASS;
            if (!(entity instanceof Pig pig)) return InteractionResult.PASS;

            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(ModItems.RADISH)) return InteractionResult.PASS;

            Level level = player.level();

            if (pig.isBaby()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                pig.ageUp((int) ((-pig.getAge()) * 0.01F), true);

                if (pig.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            pig.getX(), pig.getY() + 0.5, pig.getZ(),
                            5, 0.3, 0.3, 0.3, 0.0);
                }

                return InteractionResult.CONSUME;
            }

            if (pig.isInLove()) {
                return InteractionResult.CONSUME;
            }

            if (pig.canFallInLove() && pig.getAge() == 0) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                pig.setInLove(player);
                pig.level().broadcastEntityEvent(pig, (byte) 18);

                return InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        });
    }

    private RadishPigHandler() {
    }
}
