package com.orbital.orbitalradish;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

public final class VillagerTradesEventHandler {

    public static void init() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, factories ->
                factories.add((level, trader, random) -> new MerchantOffer(
                        new ItemCost(ModItems.RADISH, 19),
                        new ItemStack(Items.EMERALD, 1),
                        12,
                        2,
                        0.05F
                )));
    }

    private VillagerTradesEventHandler() {
    }
}