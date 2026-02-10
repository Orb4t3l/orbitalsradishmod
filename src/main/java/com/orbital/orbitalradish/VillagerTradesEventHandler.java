package com.orbital.orbitalradish;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.bus.api.SubscribeEvent;
import net.neoforged.neoforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = OrbitalRadishMod.MODID)
public class VillagerTradesEventHandler {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.FARMER) {

            // Level 1 farmer trades (novice)
            List<VillagerTrades.ItemListing> level1Trades =
                    event.getTrades().get(1);

            // FIXED: In 1.20.6, MerchantOffer uses ItemCost instead of ItemStack for inputs
            level1Trades.add((trader, random) ->
                    new net.minecraft.world.item.trading.MerchantOffer(
                            new ItemCost(ModItems.RADISH.get(), 19),  // Input cost
                            new ItemStack(Items.EMERALD, 1),          // Output
                            12,    // max uses
                            2,     // villager XP
                            0.05F  // price multiplier
                    )
            );
        }
    }
}