package com.orbital.orbitalradish;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adds radish temptation and feeding/breeding support for pigs.
 * - When a pig spawns we add a TemptGoal (radish) at priority 3.
 * - Right-click a pig with a radish -> pig enters love mode and we nudge a nearby in-love pig to meet it.
 */
@Mod.busSubscriber(modid = OrbitalRadishMod.MODID)
public final class RadishPigHandler {
    private static final int TEMPT_GOAL_PRIORITY = 3;
    private static final double MATE_SEARCH_RADIUS = 8.0D;

    // track which entities we've already added a tempt goal to (avoid duplicates across reloads)
    private static final Set<UUID> HAS_TEMPT_GOAL = ConcurrentHashMap.newKeySet();

    private RadishPigHandler() {}

    // Add TemptGoal when a pig joins the world
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        // server only
        Level lvl = event.getLevel();
        if (lvl.isClientSide()) return;

        Entity e = event.getEntity();
        if (!(e instanceof Pig)) return;
        Pig pig = (Pig) e;

        UUID id = pig.getUUID();
        if (HAS_TEMPT_GOAL.contains(id)) return;

        // pig implements PathfinderMob (Pig extends Animal -> PathfinderMob), cast to PathfinderMob for TemptGoal
        if (!(pig instanceof PathfinderMob)) return;
        PathfinderMob pm = (PathfinderMob) pig;

        try {
            Ingredient radishIngredient = Ingredient.of(ModItems.RADISH.get());
            pm.goalSelector.addGoal(TEMPT_GOAL_PRIORITY, new TemptGoal(pm, 1.25D, radishIngredient, false));
            HAS_TEMPT_GOAL.add(id);
        } catch (Throwable t) {
            System.out.println("[RadishPigHandler] Failed to add TemptGoal: " + t);
        }
    }

    // Player right-click feed handler
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Pig pig)) return;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (!stack.is(ModItems.RADISH.get())) return;

        Level level = player.level();
        if (level.isClientSide) return;

// 🐖 BABY PIG: grow it
        if (pig.isBaby()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Grow the pig (vanilla age logic)
            pig.ageUp((int)((-pig.getAge()) * 0.01F), true);

            // Spawn green happy particles for baby pigs
            if (!pig.level().isClientSide() && pig.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.HAPPY_VILLAGER,
                        pig.getX(),
                        pig.getY() + 0.5,
                        pig.getZ(),
                        5,          // count
                        0.3, 0.3, 0.3, // spread
                        0.0         // speed
                );
            }

            event.setCanceled(true);
            return;
        }

// 🚫 Cancel interaction if the pig is already in love
        if (pig.isInLove()) {
            event.setCanceled(true);
            return;
        }

// 🐖 ADULT PIG: breeding (vanilla cooldown enforced)
        if (!pig.isBaby() && pig.canFallInLove() && pig.getAge() == 0) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Set pig in love mode
            pig.setInLove(player);

            // Spawn heart particles (vanilla)
            pig.level().broadcastEntityEvent(pig, (byte)18);

            event.setCanceled(true);
        }

    }


    // Try to find a nearby pig that is already in love and nudge both to move to each other
    private static void nudgeNearbyLovedPig(Pig pig) {
        Level lvl = pig.level();
        if (lvl == null) return;

        for (Pig other : lvl.getEntitiesOfClass(Pig.class, pig.getBoundingBox().inflate(MATE_SEARCH_RADIUS),
                p -> p != pig && !p.isBaby())) {
            // use isInLove() - will normally exist on Animal; if not, fallback to reflection could be added
            boolean otherInLove;
            try {
                otherInLove = other.isInLove();
            } catch (Throwable t) {
                otherInLove = false;
            }

            if (otherInLove) {
                // nudge navigation for both pigs
                try {
                    if (pig instanceof Mob) ((Mob) pig).getNavigation().moveTo(other, 1.0D);
                    if (other instanceof Mob) ((Mob) other).getNavigation().moveTo(pig, 1.0D);
                } catch (Throwable ignored) {}
                return;
            }
        }
    }
}
