package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import com.orbital.orbitalradish.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = OrbitalRadishMod.MODID)
public class RadishAdvancementHandler {

    private static final ResourceLocation SEEDY =

            new ResourceLocation("minecraft", "husbandry/plant_seed");

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        if (stack.getItem() != ModItems.RADISH.get()) return;

        // Must be farmland
        if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof FarmBlock)) return;

        // Let vanilla place first
        if (event.getUseItem() == net.minecraftforge.eventbus.api.Event.Result.DENY) return;

        Advancement adv = player.server.getAdvancements().getAdvancement(SEEDY);
        if (adv == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
        if (progress.isDone()) return;

        for (String criteria : progress.getRemainingCriteria()) {
            player.getAdvancements().award(adv, criteria);
        }
    }
    @SubscribeEvent
    public static void onRadishArrowHit(LivingHurtEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof RadishArrowEntity arrow))
            return;

        if (!(arrow.getOwner() instanceof ServerPlayer player))
            return;

        ResourceLocation TAKE_AIM =
                new ResourceLocation("minecraft", "adventure/shoot_arrow");

        Advancement adv = player.server.getAdvancements().getAdvancement(TAKE_AIM);
        if (adv == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
        if (progress.isDone()) return;

        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(adv, criterion);
        }
    }
}
