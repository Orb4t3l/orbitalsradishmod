package com.orbital.orbitalradish;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.*;

@EventBusSubscriber(modid = OrbitalRadishMod.MODID)
public class VillagerPickUpHandler {

    private static final double SEARCH_RADIUS = 1.5D;
    private static final int RETRY_SECONDS = 8;
    private static final int SCAN_INTERVAL_TICKS = 20;
    private static final int ATTEMPTS = Math.max(1, RETRY_SECONDS);

    private static boolean isPickupDelayElapsed(net.minecraft.world.entity.item.ItemEntity itemEntity) {
        try {
            java.lang.reflect.Method m = itemEntity.getClass().getMethod("getPickUpDelay");
            Object val = m.invoke(itemEntity);
            if (val instanceof Integer) return ((Integer) val) <= 0;
        } catch (NoSuchMethodException ignored) {}
        catch (Exception ignored) {}

        try {
            java.lang.reflect.Method m2 = itemEntity.getClass().getMethod("getPickupDelay");
            Object val2 = m2.invoke(itemEntity);
            if (val2 instanceof Integer) return ((Integer) val2) <= 0;
        } catch (NoSuchMethodException ignored) {}
        catch (Exception ignored) {}

        return true;
    }

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

    @SubscribeEvent
    public static void onItemEntitySpawn(EntityJoinLevelEvent event) {
        Level lvl = event.getLevel();
        if (lvl.isClientSide()) return;
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;

        if (!(lvl instanceof ServerLevel serverLevel)) return;

        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;

        if (!stack.is(ModItems.RADISH.get())) return;

        boolean transferred = tryTransferToNearbyFarmer(itemEntity, serverLevel);
        if (transferred) return;

        retryMap.put(itemEntity.getUUID(), new RetryEntry(serverLevel, ATTEMPTS));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
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
            if (stack.isEmpty() || !stack.is(ModItems.RADISH.get())) {
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
    }

    private static void consolidateRadishStacks(Villager v) {
        final int invSize = v.getInventory().getContainerSize();
        int total = 0;

        for (int i = 0; i < invSize; i++) {
            ItemStack s = v.getInventory().getItem(i);
            if (!s.isEmpty() && s.is(ModItems.RADISH.get())) {
                total += s.getCount();
                v.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }

        if (total <= 0) return;

        final int maxStackSize = new ItemStack(ModItems.RADISH.get()).getMaxStackSize();

        int idx = 0;
        while (total > 0 && idx < invSize) {
            ItemStack cur = v.getInventory().getItem(idx);
            if (cur.isEmpty()) {
                int put = Math.min(total, maxStackSize);
                v.getInventory().setItem(idx, new ItemStack(ModItems.RADISH.get(), put));
                total -= put;
            }
            idx++;
        }

        idx = 0;
        while (total > 0 && idx < invSize) {
            ItemStack cur = v.getInventory().getItem(idx);
            if (!cur.isEmpty() && cur.is(ModItems.RADISH.get())) {
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

        for (int i = 0; i < Math.min(8, invSize); i++) {
            ItemStack s = v.getInventory().getItem(i);
            if (!s.isEmpty() && s.is(ModItems.RADISH.get())) {
                System.out.println("[Radish] Villager " + v.getUUID() + " slot " + i + " = " + s.getCount());
            }
        }
    }

    private static boolean tryTransferToNearbyFarmer(ItemEntity itemEntity, Level level) {
        if (itemEntity.isRemoved()) return true;
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return true;

        if (!isPickupDelayElapsed(itemEntity)) return false;

        var box = itemEntity.getBoundingBox().inflate(SEARCH_RADIUS);
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, box);

        for (Villager v : villagers) {
            if (v.getVillagerData().getProfession() != VillagerProfession.FARMER) continue;

            ItemStack remainder = v.getInventory().addItem(stack.copy());
            consolidateRadishStacks(v);
            if (remainder.isEmpty()) {
                itemEntity.setItem(ItemStack.EMPTY);
                itemEntity.discard();

                System.out.println("[RadishPickupHandler] Farmer at " + v.blockPosition()
                        + " picked up " + stack.getCount() + " radish(es).");
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
}