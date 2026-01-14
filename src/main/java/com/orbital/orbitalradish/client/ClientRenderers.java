package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.ModEntities;
import com.orbital.orbitalradish.OrbitalRadishMod;
import com.orbital.orbitalradish.entity.RadishArrowEntity; // ✅ MISSING IMPORT
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = OrbitalRadishMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.RADISH_ARROW.get(),
                new EntityRendererProvider<RadishArrowEntity>() {
                    @Override
                    public RadishArrowRenderer create(EntityRendererProvider.Context context) {
                        return new RadishArrowRenderer(context);
                    }
                }
        );
    }
}
