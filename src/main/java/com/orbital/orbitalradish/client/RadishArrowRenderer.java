package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RadishArrowRenderer extends ArrowRenderer<RadishArrowEntity> {

    public RadishArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(RadishArrowEntity entity) {
        return new ResourceLocation("orbitalradish", "textures/entity/projectiles/radish_arrow.png");
    }
}
