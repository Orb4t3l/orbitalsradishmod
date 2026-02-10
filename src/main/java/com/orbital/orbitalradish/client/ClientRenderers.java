package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.ModEntities;
import com.orbital.orbitalradish.entity.RadishArrowEntity;
import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = OrbitalRadishMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.RADISH_ARROW.get(),
                (EntityRendererProvider.Context ctx) -> new RadishArrowRenderer(ctx)
        );
    }
}