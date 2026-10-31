package com.orbital.orbitalradish;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FarmBlock;

public final class RadishAdvancementHandler {

    private static final ResourceLocation SEEDY = new ResourceLocation("minecraft", "husbandry/plant_seed");
    private static final ResourceLocation TAKE_AIM = new ResourceLocation("minecraft", "adventure/shoot_arrow");

    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClientSide()) return InteractionResult.PASS;
            if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

            ItemStack stack = player.getItemInHand(hand);
            if (stack.isEmpty() || stack.getItem() != ModItems.RADISH) return InteractionResult.PASS;

            if (!(world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof FarmBlock)) {
                return InteractionResult.PASS;
            }

            awardIfPresent(serverPlayer, SEEDY);
            return InteractionResult.PASS;
        });

        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseAmount, finalAmount, blocked) -> {
            if (!(source.getDirectEntity() instanceof RadishArrowEntity arrow)) return;
            if (!(arrow.getOwner() instanceof ServerPlayer player)) return;

            awardIfPresent(player, TAKE_AIM);
        });
    }

    private static void awardIfPresent(ServerPlayer player, ResourceLocation id) {
        Advancement adv = player.server.getAdvancements().getAdvancement(id);
        if (adv == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
        if (progress.isDone()) return;

        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(adv, criterion);
        }
    }

    private RadishAdvancementHandler() {
    }
}
