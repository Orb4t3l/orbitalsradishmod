package com.orbital.orbitalradish;

import com.orbital.orbitalradish.item.RadishArrowItem;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OrbitalRadishMod.MODID);

    // The item that visually represents the projectile (used by the arrow renderer & pickup)
    public static final RegistryObject<Item> RADISH_ARROW =
            ITEMS.register("radish_arrow",
                    () -> new RadishArrowItem(new Item.Properties().stacksTo(64)));

    // A simple tool that spawns the radish arrow when used (optional)
    public static final RegistryObject<Item> RADISH_STICK =
            ITEMS.register("radish_stick",
                    () -> new RadishStickItem(new Item.Properties().durability(250)));

    private ModItems() {}
}
