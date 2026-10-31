package com.orbital.orbitalradish;

import com.orbital.orbitalradish.item.RadishArrowItem;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;

public final class ModItems {

    public static final Item RADISH = register("radish",
            new ItemNameBlockItem(ModBlocks.RADISH_CROP, new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build())));

    public static final Item RADISH_ARROW = register("radish_arrow",
            new RadishArrowItem(new Item.Properties().stacksTo(64)));

    public static final Item RADISH_STICK = register("radish_stick",
            new RadishStickItem(new Item.Properties().durability(99999999)));

    public static final Item COOKED_RADISH = register("cooked_radish",
            new Item(new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(6).saturationMod(0.4f).build())));

    public static final Item RADISH_LEAF = register("radish_leaf", new Item(new Item.Properties()));

    public static final Item RADISH_BLOCK_ITEM = register("radish_block",
            new BlockItem(ModBlocks.RADISH_BLOCK, new Item.Properties()));

    public static final Item DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM = register("double_compressed_radish_block",
            new BlockItem(ModBlocks.DOUBLE_COMPRESSED_RADISH_BLOCK, new Item.Properties()));

    public static final Item TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM = register("triple_compressed_radish_block",
            new BlockItem(ModBlocks.TRIPLE_COMPRESSED_RADISH_BLOCK, new Item.Properties()));

    public static final Item RADISH_BRICKS_ITEM = register("radish_bricks",
            new BlockItem(ModBlocks.RADISH_BRICKS, new Item.Properties()));

    public static final Item RADISH_SLAB_ITEM = register("radish_slab",
            new BlockItem(ModBlocks.RADISH_SLAB, new Item.Properties()));

    public static final Item RADISH_WALLS_ITEM = register("radish_walls",
            new BlockItem(ModBlocks.RADISH_WALLS, new Item.Properties()));

    public static final Item RADISH_STAIRS_ITEM = register("radish_stairs",
            new BlockItem(ModBlocks.RADISH_STAIRS, new Item.Properties()));

    public static final Item RADISH_STEW = register("radish_stew",
            new BowlFoodItem(new Item.Properties()
                    .stacksTo(1)
                    .craftRemainder(Items.BOWL)
                    .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4f).build())));

    private static Item register(String name, Item item) {
        ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(),
                new ResourceLocation(OrbitalRadishMod.MODID, name));
        return net.minecraft.core.Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void init() {
    }

    private ModItems() {
    }
}
