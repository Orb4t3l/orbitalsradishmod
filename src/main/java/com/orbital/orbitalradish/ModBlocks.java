package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {

    private static boolean initialized = false;

    public static Block RADISH_CROP;
    public static Block RADISH_BLOCK;
    public static Block DOUBLE_COMPRESSED_RADISH_BLOCK;
    public static Block TRIPLE_COMPRESSED_RADISH_BLOCK;
    public static Block RADISH_BRICKS;
    public static Block RADISH_STAIRS;
    public static Block RADISH_SLAB;
    public static Block RADISH_WALLS;

    private static Block register(String name, Block block) {
        ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(),
                Identifier.fromNamespaceAndPath(OrbitalRadishMod.MODID, name));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    private static Block register(String name, java.util.function.Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties props) {
        ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(),
                Identifier.fromNamespaceAndPath(OrbitalRadishMod.MODID, name));
        // Set the registry key in properties before construction (required in 1.21+)
        Block block = factory.apply(props.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static void init() {
        if (initialized) return;
        initialized = true;

        RADISH_CROP = register("radish_crop", RadishCrop::new,
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.CROP));

        RADISH_BLOCK = register("radish_block", Block::new,
                BlockBehaviour.Properties.of()
                        .strength(0.5f)
                        .sound(SoundType.GRASS)
                        .mapColor(MapColor.PLANT));

        DOUBLE_COMPRESSED_RADISH_BLOCK = register("double_compressed_radish_block", Block::new,
                BlockBehaviour.Properties.of()
                        .strength(1.0f)
                        .sound(SoundType.GRASS)
                        .mapColor(MapColor.PLANT));

        TRIPLE_COMPRESSED_RADISH_BLOCK = register("triple_compressed_radish_block", Block::new,
                BlockBehaviour.Properties.of()
                        .strength(1.5f)
                        .sound(SoundType.GRASS)
                        .mapColor(MapColor.PLANT));

        RADISH_BRICKS = register("radish_bricks", Block::new,
                BlockBehaviour.Properties.of()
                        .strength(2.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .mapColor(MapColor.STONE));

        RADISH_STAIRS = register("radish_stairs", props -> new StairBlock(RADISH_BRICKS.defaultBlockState(), props),
                BlockBehaviour.Properties.of()
                        .strength(2.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops());

        RADISH_SLAB = register("radish_slab", SlabBlock::new,
                BlockBehaviour.Properties.of()
                        .strength(2.0f, 6.0f)
                        .sound(SoundType.STONE));

        RADISH_WALLS = register("radish_walls", WallBlock::new,
                BlockBehaviour.Properties.of()
                        .strength(2.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops());

        // Client-side render layer setup
        setupClient();
    }

    private static void setupClient() {
        // This method is called from common code but only loads client classes on the client side
        try {
            Class<?> clientSetup = Class.forName("com.orbital.orbitalradish.client.ModBlocksClient");
            clientSetup.getMethod("setup").invoke(null);
        } catch (Exception e) {
            // Not on client side, ignore
        }
    }

    private ModBlocks() {
    }
}
