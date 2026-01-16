package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.ModEntities;
import com.orbital.orbitalradish.entity.RadishArrowEntity;
import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OrbitalRadishMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // cast-lambda to satisfy generics
        event.registerEntityRenderer(ModEntities.RADISH_ARROW.get(), (EntityRendererProvider<RadishArrowEntity>) RadishArrowRenderer::new);
    }
}
