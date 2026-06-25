package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, OrbitalRadishMod.MODID);

    public static final RegistryObject<Block> RADISH_CROP = BLOCKS.register("radish_crop",
            () -> new RadishCrop(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
            )
    );



    public static final RegistryObject<Block> RADISH_BLOCK = BLOCKS.register("radish_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(0.5f)
                            .sound(SoundType.GRASS)
                            .mapColor(MapColor.PLANT)
            )
    );

    public static final RegistryObject<Block> DOUBLE_COMPRESSED_RADISH_BLOCK = BLOCKS.register("double_compressed_radish_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(1.0f)
                            .sound(SoundType.GRASS)
                            .mapColor(MapColor.PLANT)
            )
    );

    public static final RegistryObject<Block> TRIPLE_COMPRESSED_RADISH_BLOCK = BLOCKS.register("triple_compressed_radish_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(1.5f)
                            .sound(SoundType.GRASS)
                            .mapColor(MapColor.PLANT)
            )
    );



    public static final RegistryObject<Block> RADISH_BRICKS = BLOCKS.register("radish_bricks",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
                            .mapColor(MapColor.STONE)
            )
    );


    public static final RegistryObject<Block> RADISH_STAIRS = BLOCKS.register(
            "radish_stairs",
            () -> new StairBlock(
                    ModBlocks.RADISH_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
            )
    );


    public static final RegistryObject<Block> RADISH_SLAB = BLOCKS.register("radish_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
            )
    );

    public static final RegistryObject<Block> RADISH_WALLS = BLOCKS.register("radish_walls",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
            )
    );

    private ModBlocks() {}
}