package com.orbital.orbitalradish;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = OrbitalRadishMod.MODID)
public class VillagerPickupDebugger {

    // Correct way to reference minecraft:villager_food in 1.20.1
    private static final TagKey<Item> VILLAGER_FOOD =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "villager_food"));

    @SubscribeEvent
    public static void onItemSpawn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;

        ItemStack stack = itemEntity.getItem();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());

        // Only log things whose id contains "radish" — change if needed
        if (id == null || !id.getPath().contains("radish")) return;

        boolean isVillagerFood = stack.is(VILLAGER_FOOD);
        boolean isPlantable = stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS);

        System.out.println("====== RADISH DEBUG ======");
        System.out.println("Item: " + id);
        System.out.println("Count: " + stack.getCount());
        System.out.println("Is villager food: " + isVillagerFood);
        System.out.println("Is plantable seeds: " + isPlantable);

        List<Villager> villagers = event.getLevel()
                .getEntitiesOfClass(Villager.class, itemEntity.getBoundingBox().inflate(5));

        System.out.println("Nearby villagers: " + villagers.size());
        for (Villager v : villagers) {
            System.out.println(
                    "Villager @ " + v.blockPosition() +
                            " profession=" + v.getVillagerData().getProfession()
            );
        }

        System.out.println("==========================");
    }
}
