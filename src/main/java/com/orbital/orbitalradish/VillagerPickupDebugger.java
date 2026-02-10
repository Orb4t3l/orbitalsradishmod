//package com.orbital.orbitalradish;
//
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.npc.Villager;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.Mod;
//
//import java.util.List;
//
//@Mod.busSubscriber(modid = OrbitalRadishMod.MODID)
//public class VillagerPickupDebugger {
//
//    // Correct way to reference minecraft:villager_food in 1.20.1
//    private static final TagKey<Item> VILLAGER_FOOD =
//            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "villager_food"));
//
//    // prints villager contents for debugging — call this when you observe a villager nearby
//    private static void dumpVillagerInventory(Villager v) {
//        System.out.println("Villager inventory for " + v + " at " + v.blockPosition());
//        for (int i = 0; i < v.getInventory().getContainerSize(); i++) {
//            ItemStack s = v.getInventory().getItem(i);
//            if (!s.isEmpty()) {
//                System.out.println(" slot " + i + ": " + s.getCount() + "x " + s.getItem().toString());
//            }
//        }
//    }
//
//
//    @SubscribeEvent
//    public static void onItemSpawn(EntityJoinLevelEvent event) {
//        if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;
//
//        ItemStack stack = itemEntity.getItem();
//        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
//
//        // Only log things whose id contains "radish" — change if needed
//        if (id == null || !id.getPath().contains("radish")) return;
//
//        boolean isVillagerFood = stack.is(VILLAGER_FOOD);
//        boolean isPlantable = stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS);
//
//        System.out.println("====== RADISH DEBUG ======");
//        System.out.println("Item: " + id);
//        System.out.println("Count: " + stack.getCount());
//        System.out.println("Is villager food: " + isVillagerFood);
//        System.out.println("Is plantable seeds: " + isPlantable);
//
//        List<Villager> villagers = event.getLevel()
//                .getEntitiesOfClass(Villager.class, itemEntity.getBoundingBox().inflate(5));
//
//        System.out.println("Nearby villagers: " + villagers.size());
//        for (Villager v : villagers) {
//            System.out.println(
//                    "Villager @ " + v.blockPosition() +
//                            " profession=" + v.getVillagerData().getProfession()
//            );
//        }
//
//        System.out.println("==========================");
//    }
//}
//
