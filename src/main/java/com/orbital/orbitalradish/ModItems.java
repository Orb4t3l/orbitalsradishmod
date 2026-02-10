package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import com.orbital.orbitalradish.item.RadishArrowItem;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OrbitalRadishMod.MODID);

    public static final RegistryObject<Item> RADISH = ITEMS.register("radish",
            () -> new ItemNameBlockItem(
                    OrbitalRadishMod.RADISH_CROP.get(),
                    new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build())
            ));







    public static final RegistryObject<Item> RADISH_ARROW =
            ITEMS.register("radish_arrow", () -> new RadishArrowItem(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> RADISH_STICK =
            ITEMS.register("radish_stick", () -> new RadishStickItem(new Item.Properties().durability(250)));

    public static final RegistryObject<Item> COOKED_RADISH =
            ITEMS.register("cooked_radish", () -> new Item(new Item.Properties().food(
                    new FoodProperties.Builder().nutrition(6).saturationMod(0.4f).build()
            )));

    public static final RegistryObject<Item> RADISH_LEAF =
            ITEMS.register("radish_leaf", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RADISH_BLOCK_ITEM =
            ITEMS.register("radish_block", () -> new BlockItem(OrbitalRadishMod.RADISH_BLOCK.get(), new Item.Properties()));


    public static final RegistryObject<Item> DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM =
            ITEMS.register("double_compressed_radish_block", () -> new BlockItem(OrbitalRadishMod.DOUBLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM =
            ITEMS.register("triple_compressed_radish_block", () -> new BlockItem(OrbitalRadishMod.TRIPLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));



    public static final RegistryObject<Item> RADISH_BRICKS_ITEM =
            ITEMS.register("radish_bricks", () -> new BlockItem(OrbitalRadishMod.RADISH_BRICKS.get(), new Item.Properties()));

    public static final RegistryObject<Item> RADISH_SLAB_ITEM =
            ITEMS.register("radish_slab", () -> new BlockItem(OrbitalRadishMod.RADISH_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<Item> RADISH_WALLS_ITEM =
            ITEMS.register("radish_walls", () -> new BlockItem(OrbitalRadishMod.RADISH_WALLS.get(), new Item.Properties()));


    public static final RegistryObject<Item> RADISH_STAIRS_ITEM =
            ITEMS.register("radish_stairs", () -> new BlockItem(OrbitalRadishMod.RADISH_STAIRS.get(), new Item.Properties()));

    public static final RegistryObject<Item> RADISH_STEW = ITEMS.register(
            "radish_stew",
            () -> new BowlFoodItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .craftRemainder(Items.BOWL)
                            .food(new FoodProperties.Builder()
                                    .nutrition(6)
                                    .saturationMod(0.4f)
                                    .build()
                            )
            )
    );


    private ModItems() {}
}
