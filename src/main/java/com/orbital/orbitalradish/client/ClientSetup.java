package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;

/**
 * Client-only setup (register cutout/cutout-mipped render layers for transparent blocks).
 * Put this in your client package and ensure the file path matches the package.
 */
@Mod.busSubscriber(modid = OrbitalRadishMod.MODID, bus = Mod.busSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {

    private ClientSetup() {}

    @SuppressWarnings("deprecation") // ItemBlockRenderTypes.setRenderLayer is deprecated in 1.21+, but fine for Forge 1.20.1
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(
                OrbitalRadishMod.RADISH_CROP.get(),
                RenderType.cutoutMipped()
        );
    }

}
