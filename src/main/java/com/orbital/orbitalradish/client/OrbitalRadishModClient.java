package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class OrbitalRadishModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModBlocksClient.setup();
        EntityRendererRegistry.register(ModEntities.RADISH_ARROW, RadishArrowRenderer::new);
    }
}
