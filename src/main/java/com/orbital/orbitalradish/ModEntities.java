package com.orbital.orbitalradish;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {

    private static boolean initialized = false;

    public static EntityType<RadishArrowEntity> RADISH_ARROW;

    private static EntityType<RadishArrowEntity> register(String name) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(),
                Identifier.fromNamespaceAndPath(OrbitalRadishMod.MODID, name));

        EntityType<RadishArrowEntity> type = EntityType.Builder
                .<RadishArrowEntity>of(RadishArrowEntity::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .clientTrackingRange(4)
                .updateInterval(20)
                .build(key);

        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type);
    }

    public static void init() {
        if (initialized) return;
        initialized = true;

        RADISH_ARROW = register("radish_arrow");
    }

    private ModEntities() {
    }
}
