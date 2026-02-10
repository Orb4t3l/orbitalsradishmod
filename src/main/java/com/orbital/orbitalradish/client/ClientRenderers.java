package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.ModEntities;
import com.orbital.orbitalradish.entity.RadishArrowEntity;
import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.client.event.EntityRenderersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod.busSubscriber(modid = OrbitalRadishMod.MODID, bus = Mod.busSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // cast-lambda to satisfy generics
        event.registerEntityRenderer(ModEntities.RADISH_ARROW.get(), (EntityRendererProvider<RadishArrowEntity>) RadishArrowRenderer::new);
    }
}
