package com.orbital.orbitalradish;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class VillagerPickUpHandler {

    private static final double SEARCH_RADIUS = 1.5D;
    private static final int RETRY_SECONDS = 8;
    private static final int SCAN_INTERVAL_TICKS = 20;
    private static final int ATTEMPTS = Math.max(1, RETRY_SECONDS);

    private static final Map<UUID, RetryEntry> retryMap = new HashMap<>();
    private static long tickCounter = 0L;

    private static final class RetryEntry {
        final ServerLevel level;
        int attemptsLeft;

        RetryEntry(ServerLevel level, int attemptsLeft) {
            this.level = level;
            this.attemptsLeft = attemptsLeft;
        }
    }

    public static void init() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (!(entity instanceof ItemEntity itemEntity)) return;

            ItemStack stack = itemEntity.getItem();
            if (stack.isEmpty() || !stack.is(ModItems.RADISH)) return;

            boolean transferred = tryTransferToNearbyFarmer(itemEntity, level);
            if (transferred) return;

            retryMap.put(itemEntity.getUUID(), new RetryEntry(level, ATTEMPTS));
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            if ((tickCounter % SCAN_INTERVAL_TICKS) != 0) return;
            if (retryMap.isEmpty()) return;

            List<UUID> keys = new ArrayList<>(retryMap.keySet());
            for (UUID id : keys) {
                RetryEntry entry = retryMap.get(id);
                if (entry == null) {
                    retryMap.remove(id);
                    continue;
                }

                ServerLevel level = entry.level;
                Entity entity = level.getEntity(id);
                if (!(entity instanceof ItemEntity itemEntity)) {
                    retryMap.remove(id);
                    continue;
                }

                ItemStack stack = itemEntity.getItem();
                if (stack.isEmpty() || !stack.is(ModItems.RADISH)) {
                    retryMap.remove(id);
                    continue;
                }

                boolean transferred = tryTransferToNearbyFarmer(itemEntity, level);
                if (transferred) {
                    retryMap.remove(id);
                } else {
                    entry.attemptsLeft--;
                    if (entry.attemptsLeft <= 0) {
                        retryMap.remove(id);
                    } else {
                        retryMap.put(id, entry);
                    }
                }
            }
        });
    }

    private static void consolidateRadishStacks(Villager v) {
        final int invSize = v.getInventory().getContainerSize();
        int total = 0;

        for (int i = 0; i < invSize; i++) {
            ItemStack s = v.getInventory().getItem(i);
            if (!s.isEmpty() && s.is(ModItems.RADISH)) {
                total += s.getCount();
                v.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }

        if (total <= 0) return;

        final int maxStackSize = new ItemStack(ModItems.RADISH).getMaxStackSize();

        int idx = 0;
        while (total > 0 && idx < invSize) {
            ItemStack cur = v.getInventory().getItem(idx);
            if (cur.isEmpty()) {
                int put = Math.min(total, maxStackSize);
                v.getInventory().setItem(idx, new ItemStack(ModItems.RADISH, put));
                total -= put;
            }
            idx++;
        }

        idx = 0;
        while (total > 0 && idx < invSize) {
            ItemStack cur = v.getInventory().getItem(idx);
            if (!cur.isEmpty() && cur.is(ModItems.RADISH)) {
                int canAdd = maxStackSize - cur.getCount();
                if (canAdd > 0) {
                    int add = Math.min(canAdd, total);
                    cur.grow(add);
                    v.getInventory().setItem(idx, cur);
                    total -= add;
                }
            }
            idx++;
        }
    }

    private static boolean tryTransferToNearbyFarmer(ItemEntity itemEntity, Level level) {
        if (itemEntity.isRemoved()) return true;
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return true;

        var box = itemEntity.getBoundingBox().inflate(SEARCH_RADIUS);
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, box);

        for (Villager v : villagers) {
            if (v.getVillagerData().profession() != VillagerProfession.FARMER) continue;

            ItemStack remainder = v.getInventory().addItem(stack.copy());
            consolidateRadishStacks(v);
            if (remainder.isEmpty()) {
                itemEntity.setItem(ItemStack.EMPTY);
                itemEntity.discard();
                return true;
            }

            int accepted = stack.getCount() - remainder.getCount();
            if (accepted > 0) {
                ItemStack newStack = stack.copy();
                newStack.shrink(accepted);
                itemEntity.setItem(newStack);
            }
        }
        return false;
    }

    private VillagerPickUpHandler() {
    }
}
