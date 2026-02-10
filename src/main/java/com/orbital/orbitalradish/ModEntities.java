package com.orbital.orbitalradish;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, OrbitalRadishMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<RadishArrowEntity>> RADISH_ARROW =
            ENTITIES.register("radish_arrow",
                    () -> EntityType.Builder
                            .<RadishArrowEntity>of(RadishArrowEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("radish_arrow")
            );

    private ModEntities() {}
}