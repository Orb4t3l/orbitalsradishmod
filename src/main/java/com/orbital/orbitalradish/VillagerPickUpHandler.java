package com.orbital.orbitalradish;

import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

/**
 * Robust pickup handler for Forge 1.20.1.
 * - Attempts immediate transfer when an ItemEntity spawns.
 * - Retries periodically for leftover radish ItemEntities on the same ServerLevel.
 * - Explicitly removes item entities and notifies nearby players to avoid client ghosting.
 */
@Mod.EventBusSubscriber(modid = OrbitalRadishMod.MODID)
public class VillagerPickUpHandler {

    private static final double SEARCH_RADIUS = 1.5D;     // smaller radius prevents many farmers fighting for one stack
    private static final int RETRY_SECONDS = 8;           // total seconds to try retries
    private static final int SCAN_INTERVAL_TICKS = 20;    // once per second
    private static final int ATTEMPTS = Math.max(1, RETRY_SECONDS); // attempts count
    // reflection helper: returns true if the ItemEntity's pickup delay is 0 or less
    private static boolean isPickupDelayElapsed(net.minecraft.world.entity.item.ItemEntity itemEntity) {
        try {
            // try Mojang-style name first
            java.lang.reflect.Method m = itemEntity.getClass().getMethod("getPickUpDelay");
            Object val = m.invoke(itemEntity);
            if (val instanceof Integer) return ((Integer) val) <= 0;
        } catch (NoSuchMethodException ignored) {}
        catch (Exception ignored) {}

        try {
            // try alternative mapping name
            java.lang.reflect.Method m2 = itemEntity.getClass().getMethod("getPickupDelay");
            Object val2 = m2.invoke(itemEntity);
            if (val2 instanceof Integer) return ((Integer) val2) <= 0;
        } catch (NoSuchMethodException ignored) {}
        catch (Exception ignored) {}

        // if we couldn't find a getter by reflection, be conservative and assume ready
        return true;
    }


    // Map of ItemEntity UUID -> RetryEntry (keeps ServerLevel and attempts left)
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
        // only server-side
        Level lvl = event.getLevel();
        if (lvl.isClientSide()) return;
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;

        if (!(lvl instanceof ServerLevel serverLevel)) return;

        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;

        // Fast check: ensure this is the radish item (adjust if your registry class differs)
        if (!stack.is(ModItems.RADISH.get())) return;

        // Try immediate transfer
        boolean transferred = tryTransferToNearbyFarmer(itemEntity, serverLevel);
        if (transferred) return;

        // schedule retries: store serverLevel and attempts
        retryMap.put(itemEntity.getUUID(), new RetryEntry(serverLevel, ATTEMPTS));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        tickCounter++;
        if ((tickCounter % SCAN_INTERVAL_TICKS) != 0) return;
        if (retryMap.isEmpty()) return;

        // copy keys to avoid concurrent modification
        List<UUID> keys = new ArrayList<>(retryMap.keySet());
        for (UUID id : keys) {
            RetryEntry entry = retryMap.get(id);
            if (entry == null) {
                retryMap.remove(id);
                continue;
            }

            ServerLevel level = entry.level;
            // find entity by UUID on this ServerLevel
            Entity entity = level.getEntity(id);
            if (!(entity instanceof ItemEntity itemEntity)) {
                // entity not found or not an ItemEntity anymore -> stop tracking
                retryMap.remove(id);
                continue;
            }

            ItemStack stack = itemEntity.getItem();
            if (stack.isEmpty() || !stack.is(ModItems.RADISH.get())) {
                retryMap.remove(id);
                continue;
            }

            // Attempt transfer
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

    // After adding radishes to a villager's inventory, compact them so one slot can reach the 12-item threshold.
// This avoids the "I see radishes but villager never becomes willing" problem.
    private static void consolidateRadishStacks(Villager v) {
        final int invSize = v.getInventory().getContainerSize();
        int total = 0;

        // Count and clear out all radish stacks
        for (int i = 0; i < invSize; i++) {
            ItemStack s = v.getInventory().getItem(i);
            if (!s.isEmpty() && s.is(ModItems.RADISH.get())) {
                total += s.getCount();
                v.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }

        if (total <= 0) return;

        // Put as many full stacks as needed, starting from first slot (or the first slot that accepts items)
        int idx = 0;
        while (total > 0 && idx < invSize) {
            // find next available slot (empty or already radish in case of some weird ordering)
            ItemStack cur = v.getInventory().getItem(idx);
            if (cur.isEmpty()) {
                int put = Math.min(total, ModItems.RADISH.get().getMaxStackSize()); // typically 64
                v.getInventory().setItem(idx, new ItemStack(ModItems.RADISH.get(), put));
                total -= put;
            }
            idx++;
        }

        // If there are still leftover items (shouldn't normally happen), stuff them into any remaining slots
        idx = 0;
        while (total > 0 && idx < invSize) {
            ItemStack cur = v.getInventory().getItem(idx);
            if (!cur.isEmpty() && cur.is(ModItems.RADISH.get())) {
                int canAdd = ModItems.RADISH.get().getMaxStackSize() - cur.getCount();
                if (canAdd > 0) {
                    int add = Math.min(canAdd, total);
                    cur.grow(add);
                    v.getInventory().setItem(idx, cur);
                    total -= add;
                }
            }
            idx++;
        }

        // Debug: log the first few stacks so you can verify a slot reached >=12
        for (int i = 0; i < Math.min(8, invSize); i++) {
            ItemStack s = v.getInventory().getItem(i);
            if (!s.isEmpty() && s.is(ModItems.RADISH.get())) {
                System.out.println("[Radish] Villager " + v.getUUID() + " slot " + i + " = " + s.getCount());
            }
        }
    }


    /**
     * Try to transfer the entire ItemEntity stack into a nearby farmer's inventory.
     * If the farmer accepts the full stack (remainder empty), the ItemEntity is removed
     * (server-side) and clients are notified. If partially accepted, the ItemEntity stack
     * is updated to the remainder and method returns false.
     */
    private static boolean tryTransferToNearbyFarmer(ItemEntity itemEntity, Level level) {
        if (itemEntity.isRemoved()) return true;
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return true;

        // VANILLA-FRIENDLY: don't force pickup immediately — respect vanilla pickup delay
        // VANILLA-FRIENDLY: don't attempt transfer until vanilla pickup delay expires
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


            // partial acceptance: update the ItemEntity with remainder
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