package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {

    public static final Block RADISH_CROP = register("radish_crop",
            new RadishCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static final Block RADISH_BLOCK = register("radish_block",
            new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final Block DOUBLE_COMPRESSED_RADISH_BLOCK = register("double_compressed_radish_block",
            new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final Block TRIPLE_COMPRESSED_RADISH_BLOCK = register("triple_compressed_radish_block",
            new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final Block RADISH_BRICKS = register("radish_bricks",
            new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.STONE)));

    public static final Block RADISH_STAIRS = register("radish_stairs",
            new StairBlock(RADISH_BRICKS.defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(2.0f, 6.0f).sound(SoundType.STONE)));

    public static final Block RADISH_SLAB = register("radish_slab",
            new SlabBlock(BlockBehaviour.Properties.of().strength(2.0f, 6.0f).sound(SoundType.STONE)));

    public static final Block RADISH_WALLS = register("radish_walls",
            new WallBlock(BlockBehaviour.Properties.copy(Blocks.BRICK_WALL)));

    private static Block register(String name, Block block) {
        ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(),
                new ResourceLocation(OrbitalRadishMod.MODID, name));
        return net.minecraft.core.Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static void init() {
    }

    private ModBlocks() {
    }
}
