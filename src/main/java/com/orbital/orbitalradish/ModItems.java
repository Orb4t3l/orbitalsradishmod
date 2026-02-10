package com.orbital.orbitalradish;

import com.orbital.orbitalradish.item.RadishArrowItem;
import com.orbital.orbitalradish.item.RadishItem;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, OrbitalRadishMod.MODID);

    public static final DeferredHolder<Item, RadishItem> RADISH = ITEMS.register("radish",
            () -> new RadishItem(new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(2)
                            .saturationModifier(0.3f)
                            .build()
                    )
            )
    );

    public static final DeferredHolder<Item, RadishArrowItem> RADISH_ARROW =
            ITEMS.register("radish_arrow", () -> new RadishArrowItem(new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<Item, RadishStickItem> RADISH_STICK =
            ITEMS.register("radish_stick", () -> new RadishStickItem(new Item.Properties().durability(250)));

    public static final DeferredHolder<Item, Item> COOKED_RADISH =
            ITEMS.register("cooked_radish", () -> new Item(new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).build()
            )));

    public static final DeferredHolder<Item, Item> RADISH_LEAF =
            ITEMS.register("radish_leaf", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> RADISH_BLOCK_ITEM =
            ITEMS.register("radish_block", () -> new BlockItem(OrbitalRadishMod.RADISH_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM =
            ITEMS.register("double_compressed_radish_block", () -> new BlockItem(OrbitalRadishMod.DOUBLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM =
            ITEMS.register("triple_compressed_radish_block", () -> new BlockItem(OrbitalRadishMod.TRIPLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> RADISH_BRICKS_ITEM =
            ITEMS.register("radish_bricks", () -> new BlockItem(OrbitalRadishMod.RADISH_BRICKS.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> RADISH_SLAB_ITEM =
            ITEMS.register("radish_slab", () -> new BlockItem(OrbitalRadishMod.RADISH_SLAB.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> RADISH_WALLS_ITEM =
            ITEMS.register("radish_walls", () -> new BlockItem(OrbitalRadishMod.RADISH_WALLS.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> RADISH_STAIRS_ITEM =
            ITEMS.register("radish_stairs", () -> new BlockItem(OrbitalRadishMod.RADISH_STAIRS.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> RADISH_STEW = ITEMS.register(
            "radish_stew",
            () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .craftRemainder(Items.BOWL)
                            .food(new FoodProperties.Builder()
                                    .nutrition(6)
                                    .saturationModifier(0.4f)
                                    .build()
                            )
            )
    );

    private ModItems() {}
}