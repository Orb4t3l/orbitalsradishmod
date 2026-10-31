package com.orbital.orbitalradish;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {

    public static final EntityType<RadishArrowEntity> RADISH_ARROW = register("radish_arrow",
            EntityType.Builder.<RadishArrowEntity>of(RadishArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("radish_arrow"));

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(String name, EntityType<T> type) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(),
                new ResourceLocation(OrbitalRadishMod.MODID, name));
        return net.minecraft.core.Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type);
    }

    public static void init() {
    }

    private ModEntities() {
    }
}
