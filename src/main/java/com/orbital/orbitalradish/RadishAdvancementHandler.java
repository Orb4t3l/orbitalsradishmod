package com.orbital.orbitalradish;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FarmBlock;

public final class RadishAdvancementHandler {

    private static final Identifier SEEDY = Identifier.fromNamespaceAndPath("minecraft", "husbandry/plant_seed");

    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClientSide()) return InteractionResult.PASS;
            if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.PASS;

            ItemStack stack = player.getItemInHand(hand);
            if (stack.isEmpty() || stack.getItem() != ModItems.RADISH) return InteractionResult.PASS;

            if (!(world.getBlockState(hitResult.getBlockPos()).getBlock() instanceof FarmBlock)) {
                return InteractionResult.PASS;
            }

            AdvancementHolder adv = serverPlayer.level().getServer().getAdvancements().get(SEEDY);
            if (adv != null) {
                AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(adv);
                if (!progress.isDone()) {
                    for (String criterion : progress.getRemainingCriteria()) {
                        serverPlayer.getAdvancements().award(adv, criterion);
                    }
                }
            }

            return InteractionResult.PASS;
        });
    }

    private RadishAdvancementHandler() {
    }
}
