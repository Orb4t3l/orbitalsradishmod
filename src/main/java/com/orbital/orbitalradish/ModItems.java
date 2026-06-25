package com.orbital.orbitalradish;

import com.orbital.orbitalradish.item.RadishArrowItem;
import com.orbital.orbitalradish.item.RadishItem;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OrbitalRadishMod.MODID);

    public static final RegistryObject<Item> RADISH = ITEMS.register("radish",
            () -> new RadishItem(
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(2)
                                    .saturationModifier(0.3f)
                                    .build()
                            )
            )
    );

    public static final RegistryObject<Item> COOKED_RADISH = ITEMS.register("cooked_radish",
            () -> new Item(
                    new Item.Properties()
                            .food(new FoodProperties.Builder()
                                    .nutrition(6)
                                    .saturationModifier(0.4f)
                                    .build()
                            )
            )
    );

    public static final RegistryObject<Item> RADISH_LEAF = ITEMS.register("radish_leaf",
            () -> new Item(new Item.Properties())
    );

    public static final RegistryObject<Item> RADISH_STEW = ITEMS.register("radish_stew",
            () -> new Item(
                    new Item.Properties()
                            .stacksTo(1)
                            .craftRemainder(Items.BOWL)
                            .food(new FoodProperties.Builder()
                                    .nutrition(8)
                                    .saturationModifier(0.8f)
                                    .build()
                            )
            )
    );


    public static final RegistryObject<Item> RADISH_ARROW = ITEMS.register("radish_arrow",
            () -> new RadishArrowItem(new Item.Properties().stacksTo(64))
    );

    public static final RegistryObject<Item> RADISH_STICK = ITEMS.register("radish_stick",
            () -> new RadishStickItem(new Item.Properties().durability(250))
    );


    public static final RegistryObject<Item> RADISH_BLOCK_ITEM = ITEMS.register("radish_block",
            () -> new BlockItem(ModBlocks.RADISH_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM = ITEMS.register("double_compressed_radish_block",
            () -> new BlockItem(ModBlocks.DOUBLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM = ITEMS.register("triple_compressed_radish_block",
            () -> new BlockItem(ModBlocks.TRIPLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> RADISH_BRICKS_ITEM = ITEMS.register("radish_bricks",
            () -> new BlockItem(ModBlocks.RADISH_BRICKS.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> RADISH_SLAB_ITEM = ITEMS.register("radish_slab",
            () -> new BlockItem(ModBlocks.RADISH_SLAB.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> RADISH_WALLS_ITEM = ITEMS.register("radish_walls",
            () -> new BlockItem(ModBlocks.RADISH_WALLS.get(), new Item.Properties())
    );

    public static final RegistryObject<Item> RADISH_STAIRS_ITEM = ITEMS.register("radish_stairs",
            () -> new BlockItem(ModBlocks.RADISH_STAIRS.get(), new Item.Properties())
    );

    private ModItems() {}
}