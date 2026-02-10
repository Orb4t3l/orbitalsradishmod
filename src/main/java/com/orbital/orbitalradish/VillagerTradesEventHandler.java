package com.orbital.orbitalradish;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.List;

@Mod.busSubscriber(modid = OrbitalRadishMod.MODID)
public class VillagerTradesEventHandler {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.FARMER) {

            // Level 1 farmer trades (novice)
            List<VillagerTrades.ItemListing> level1Trades =
                    event.getTrades().get(1);

            level1Trades.add((trader, random) ->
                    new net.minecraft.world.item.trading.MerchantOffer(
                            new ItemStack(ModItems.RADISH.get(), 19),
                            new ItemStack(Items.EMERALD, 1),
                            12,    // max uses
                            2,     // villager XP
                            0.05F  // price multiplier
                    )
            );
        }
    }
}
