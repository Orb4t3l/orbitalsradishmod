package com.orbital.orbitalradish;

import com.orbital.orbitalradish.item.RadishArrowItem;
import com.orbital.orbitalradish.item.RadishItem;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class ModItems {

    private static boolean initialized = false;

    public static Item RADISH;
    public static Item RADISH_ARROW;
    public static Item RADISH_STICK;
    public static Item COOKED_RADISH;
    public static Item RADISH_LEAF;
    public static Item RADISH_BLOCK_ITEM;
    public static Item DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM;
    public static Item TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM;
    public static Item RADISH_BRICKS_ITEM;
    public static Item RADISH_SLAB_ITEM;
    public static Item RADISH_WALLS_ITEM;
    public static Item RADISH_STAIRS_ITEM;
    public static Item RADISH_STEW;

    private static Item register(String name, Item item) {
        ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(OrbitalRadishMod.MODID, name));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static Item register(String name, java.util.function.Function<Item.Properties, Item> factory, Item.Properties props) {
        ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(),
                Identifier.fromNamespaceAndPath(OrbitalRadishMod.MODID, name));
        // Set the registry key in properties before construction (required in 1.21+)
        Item item = factory.apply(props.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void init() {
        if (initialized) return;
        initialized = true;

        RADISH = register("radish", RadishItem::new,
                new Item.Properties().food(
                        new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).build()));

        RADISH_ARROW = register("radish_arrow", RadishArrowItem::new,
                new Item.Properties().stacksTo(64));

        RADISH_STICK = register("radish_stick", RadishStickItem::new,
                new Item.Properties().durability(99999999).enchantable(1));

        COOKED_RADISH = register("cooked_radish", Item::new,
                new Item.Properties().food(
                        new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).build()));

        RADISH_LEAF = register("radish_leaf", Item::new,
                new Item.Properties());

        RADISH_BLOCK_ITEM = register("radish_block", props -> new BlockItem(ModBlocks.RADISH_BLOCK, props),
                new Item.Properties());
        DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM = register("double_compressed_radish_block", props -> new BlockItem(ModBlocks.DOUBLE_COMPRESSED_RADISH_BLOCK, props),
                new Item.Properties());
        TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM = register("triple_compressed_radish_block", props -> new BlockItem(ModBlocks.TRIPLE_COMPRESSED_RADISH_BLOCK, props),
                new Item.Properties());
        RADISH_BRICKS_ITEM = register("radish_bricks", props -> new BlockItem(ModBlocks.RADISH_BRICKS, props),
                new Item.Properties());
        RADISH_SLAB_ITEM = register("radish_slab", props -> new BlockItem(ModBlocks.RADISH_SLAB, props),
                new Item.Properties());
        RADISH_WALLS_ITEM = register("radish_walls", props -> new BlockItem(ModBlocks.RADISH_WALLS, props),
                new Item.Properties());
        RADISH_STAIRS_ITEM = register("radish_stairs", props -> new BlockItem(ModBlocks.RADISH_STAIRS, props),
                new Item.Properties());

        RADISH_STEW = register("radish_stew", Item::new,
                new Item.Properties()
                        .stacksTo(1)
                        .craftRemainder(Items.BOWL)
                        .food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).build()));
    }

    private ModItems() {
    }
}
