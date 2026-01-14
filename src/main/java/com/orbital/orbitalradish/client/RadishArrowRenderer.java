package com.orbital.orbitalradish.client;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis; // << use Axis instead of Vector3f
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;

// BakedModel import most common for Forge 1.20.1:
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;

public class RadishArrowRenderer extends EntityRenderer<RadishArrowEntity> {

    public RadishArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(RadishArrowEntity entity, float yaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {

        ItemStack stack = entity.getItem();
        if (stack == null || stack.isEmpty()) {
            super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
            return;
        }

        poseStack.pushPose();

        float rotYaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float rotPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        // rotate to match flying arrow using Axis (no Vector3f required)
        poseStack.mulPose(Axis.YP.rotationDegrees(-rotYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotPitch));

        float s = 0.5f;
        poseStack.scale(s, s, s);

        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, model);

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RadishArrowEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
