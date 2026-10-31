package com.orbital.orbitalradish;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RadishPigHandler {

    private static final Set<UUID> HAS_TEMPT_GOAL = ConcurrentHashMap.newKeySet();

    private static final Field GOAL_SELECTOR_FIELD;

    static {
        Field field;
        try {
            field = Mob.class.getDeclaredField("goalSelector");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            field = null;
        }
        GOAL_SELECTOR_FIELD = field;
    }

    private static GoalSelector getGoalSelector(Mob mob) {
        if (GOAL_SELECTOR_FIELD == null) return null;
        try {
            return (GoalSelector) GOAL_SELECTOR_FIELD.get(mob);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static void init() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (!(entity instanceof Pig pig)) return;

            UUID id = pig.getUUID();
            if (HAS_TEMPT_GOAL.contains(id)) return;

            PathfinderMob pm = pig;
            GoalSelector selector = getGoalSelector(pm);
            if (selector == null) return;

            Ingredient radishIngredient = Ingredient.of(ModItems.RADISH);
            selector.addGoal(3, new TemptGoal(pm, 1.25D, radishIngredient, false));
            HAS_TEMPT_GOAL.add(id);
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClientSide()) return InteractionResult.PASS;
            if (!(entity instanceof Pig pig)) return InteractionResult.PASS;

            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(ModItems.RADISH)) return InteractionResult.PASS;

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
