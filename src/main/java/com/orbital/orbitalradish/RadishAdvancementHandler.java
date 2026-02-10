package com.orbital.orbitalradish;

import com.orbital.orbitalradish.ModItems;
import com.orbital.orbitalradish.entity.RadishArrowEntity;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.InteractionResult;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = OrbitalRadishMod.MODID)
public class RadishAdvancementHandler {

    private static final ResourceLocation SEEDY =
            ResourceLocation.fromNamespaceAndPath("minecraft", "husbandry/plant_seed");

    private static final ResourceLocation TAKE_AIM =
            ResourceLocation.fromNamespaceAndPath("minecraft", "adventure/shoot_arrow");

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        if (stack.getItem() != ModItems.RADISH.get()) return;

        // Must be farmland
        if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof FarmBlock)) return;

        // In NeoForge, just check if the event was canceled or if we should proceed
        if (event.isCanceled()) return;

        // get manager and the advancement holder (entry)
        ServerAdvancementManager advManager = player.server.getAdvancements();
        AdvancementHolder advHolder = advManager.get(SEEDY);
        if (advHolder == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advHolder);
        if (progress.isDone()) return;

        for (String criteria : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advHolder, criteria);
        }
    }

    @SubscribeEvent
    public static void onRadishArrowHit(LivingDamageEvent.Pre event) {
        // Use getSource() to get the DamageSource
        if (!(event.getSource().getDirectEntity() instanceof RadishArrowEntity arrow))
            return;

        if (!(arrow.getOwner() instanceof ServerPlayer player))
            return;

        ServerAdvancementManager advManager = player.server.getAdvancements();
        AdvancementHolder advHolder = advManager.get(TAKE_AIM);
        if (advHolder == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advHolder);
        if (progress.isDone()) return;

        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advHolder, criterion);
        }
    }
}