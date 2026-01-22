package com.orbital.orbitalradish;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OrbitalRadishMod.MODID)
public class VillagerPickupFix {

    @SubscribeEvent
    public static void onItemSpawn(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;

        ItemStack stack = itemEntity.getItem();
        if (!stack.is(ModItems.RADISH.get())) return;

        var level = itemEntity.level();

        for (Villager villager : level.getEntitiesOfClass(
                Villager.class,
                itemEntity.getBoundingBox().inflate(2.5)
        )) {
            if (villager.getVillagerData().getProfession() == VillagerProfession.FARMER) {
                villager.getInventory().addItem(stack.copy());
                itemEntity.discard();
                return;
            }
        }
    }
}
