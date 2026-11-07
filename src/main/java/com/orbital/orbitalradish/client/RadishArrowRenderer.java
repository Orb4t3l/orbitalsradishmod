package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class RadishArrowRenderer extends ArrowRenderer<RadishArrowEntity> {

    public RadishArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTextureLocation(RadishArrowEntity entity) {
        return Identifier.fromNamespaceAndPath("orbitalradish", "textures/entity/projectiles/radish_arrow.png");
    }
}
