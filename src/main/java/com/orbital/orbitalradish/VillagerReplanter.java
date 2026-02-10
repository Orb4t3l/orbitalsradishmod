package com.orbital.orbitalradish;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.server.ServerLifecycleHooks;

import java.util.*;

@Mod.busSubscriber(modid = OrbitalRadishMod.MODID)
public class VillagerReplanter {

    private static final int SCAN_INTERVAL_TICKS = 20;
    private static final int SEARCH_RADIUS_BLOCKS = 6;
    private static final int PLANT_SEARCH_RADIUS = 4;
    private static final int PLANT_COOLDOWN_SECONDS = 4;

    private static final Map<UUID, Integer> cooldownTicks = new HashMap<>();
    private static long tickCounter = 0L;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        tickCounter++;
        if ((tickCounter % SCAN_INTERVAL_TICKS) != 0) return;

        // decrement cooldowns
        var it = cooldownTicks.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            int left = e.getValue() - 1;
            if (left <= 0) it.remove();
            else e.setValue(left);
        }

        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            ServerLevel level = (ServerLevel) player.level();
            if (level == null) continue;

            var playerBox = player.getBoundingBox().inflate(SEARCH_RADIUS_BLOCKS);
            List<Villager> villagers = level.getEntitiesOfClass(Villager.class, playerBox, v ->
                    !v.isRemoved() && !v.isBaby() && v.getVillagerData().getProfession() == VillagerProfession.FARMER
            );

            for (Villager v : villagers) {
                UUID vid = v.getUUID();
                if (cooldownTicks.containsKey(vid)) continue;

                int slot = findRadishSlot(v);
                if (slot < 0) continue;

                Optional<BlockPos> maybeTarget = findFarmlandForPlanting(v, level);
                if (maybeTarget.isEmpty()) continue;
                BlockPos target = maybeTarget.get();

                level.setBlock(target, OrbitalRadishMod.RADISH_CROP.get().defaultBlockState(), 3);

                ItemStack stack = v.getInventory().getItem(slot);
                if (!stack.isEmpty()) {
                    stack = stack.copy();
                    stack.shrink(1);
                    v.getInventory().setItem(slot, stack.isEmpty() ? ItemStack.EMPTY : stack);
                }

                cooldownTicks.put(vid, PLANT_COOLDOWN_SECONDS);
                v.swing(InteractionHand.MAIN_HAND);
                level.playSound(null, target, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0f, 1.0f);

                System.out.println("[VillagerReplanter] Villager " + vid + " planted radish at " + target);
            }
        }
    }

    private static int findRadishSlot(Villager v) {
        for (int i = 0; i < v.getInventory().getContainerSize(); i++) {
            ItemStack s = v.getInventory().getItem(i);
            if (!s.isEmpty() && s.is(ModItems.RADISH.get())) return i;
        }
        return -1;
    }

    private static Optional<BlockPos> findFarmlandForPlanting(Villager v, Level level) {
        BlockPos origin = v.blockPosition();
        for (int dx = -PLANT_SEARCH_RADIUS; dx <= PLANT_SEARCH_RADIUS; dx++) {
            for (int dz = -PLANT_SEARCH_RADIUS; dz <= PLANT_SEARCH_RADIUS; dz++) {
                BlockPos farmlandPos = origin.offset(dx, 0, dz);
                if (level.getBlockState(farmlandPos).is(Blocks.FARMLAND) && level.isEmptyBlock(farmlandPos.above())) {
                    return Optional.of(farmlandPos.above());
                }
            }
        }
        return Optional.empty();
    }
}
