package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only setup (register cutout/cutout-mipped render layers for transparent blocks).
 */
@EventBusSubscriber(modid = OrbitalRadishMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {

    private ClientSetup() {}

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(
                    OrbitalRadishMod.RADISH_CROP.get(),
                    RenderType.cutoutMipped()
            );
        });
    }
}