package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;

public class RadishArrowRenderer extends ArrowRenderer<RadishArrowEntity, ArrowRenderState> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("orbitalradish", "textures/entity/projectiles/radish_arrow.png");

    public RadishArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(ArrowRenderState state) {
        return TEXTURE;
    }
}