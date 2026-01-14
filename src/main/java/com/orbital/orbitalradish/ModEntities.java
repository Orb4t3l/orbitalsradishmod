package com.orbital.orbitalradish;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OrbitalRadishMod.MODID);

    public static final RegistryObject<EntityType<RadishArrowEntity>> RADISH_ARROW =
            ENTITIES.register("radish_arrow",
                    () -> EntityType.Builder.of(
                                    (EntityType<RadishArrowEntity> type, Level level) ->
                                            new RadishArrowEntity(type, level),
                                    MobCategory.MISC
                            )
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(8)
                            .build("radish_arrow")
            );

    private ModEntities() {}
}
