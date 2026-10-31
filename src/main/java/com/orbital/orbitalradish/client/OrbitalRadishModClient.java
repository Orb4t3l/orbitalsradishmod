package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.ModBlocks;
import com.orbital.orbitalradish.ModEntities;
import com.orbital.orbitalradish.client.RadishArrowRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class OrbitalRadishModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RADISH_CROP, RenderType.cutoutMipped());

        EntityRendererRegistry.register(ModEntities.RADISH_ARROW, RadishArrowRenderer::new);
    }
}
