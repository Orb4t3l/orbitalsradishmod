package com.orbital.orbitalradish;

import com.orbital.orbitalradish.item.RadishArrowItem;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OrbitalRadishMod.MODID);

    public static final RegistryObject<Item> RADISH = ITEMS.register("radish",
            () -> new ItemNameBlockItem(
                    OrbitalRadishMod.RADISH_CROP.get(),
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(2)
                                    .saturationMod(0.3f)
                                    .build()
                            )
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


    private ModItems() {}
}
