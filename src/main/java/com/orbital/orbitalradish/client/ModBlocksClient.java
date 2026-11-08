package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.ModBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public final class ModBlocksClient {

    public static void setup() {
        BlockRenderLayerMap.putBlock(ModBlocks.RADISH_CROP, ChunkSectionLayer.CUTOUT);
    }

    private ModBlocksClient() {
    }
}
