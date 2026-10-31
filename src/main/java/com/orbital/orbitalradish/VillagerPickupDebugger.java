package com.orbital.orbitalradish;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class VillagerPickupDebugger {

    private static final TagKey<Item> VILLAGER_FOOD =
            TagKey.create(Registries.ITEM, new ResourceLocation("minecraft", "villager_food"));

    public static void init() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (!(entity instanceof ItemEntity itemEntity)) return;

            ItemStack stack = itemEntity.getItem();
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());

            if (id == null || !id.getPath().contains("radish")) return;

            boolean isVillagerFood = stack.is(VILLAGER_FOOD);
            boolean isPlantable = stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS);

            System.out.println("====== RADISH DEBUG ======");
            System.out.println("Item: " + id);
            System.out.println("Count: " + stack.getCount());
            System.out.println("Is villager food: " + isVillagerFood);
            System.out.println("Is plantable seeds: " + isPlantable);

            List<Villager> villagers = level.getEntitiesOfClass(Villager.class, itemEntity.getBoundingBox().inflate(5));

            System.out.println("Nearby villagers: " + villagers.size());
            for (Villager v : villagers) {
                System.out.println("Villager @ " + v.blockPosition() + " profession=" + v.getVillagerData().getProfession());
            }

            System.out.println("==========================");
        });
    }

    private VillagerPickupDebugger() {
    }
}
