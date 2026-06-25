package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import com.orbital.orbitalradish.item.RadishStickItem;

import static com.orbital.orbitalradish.ModBlocks.RADISH_BRICKS;

@Mod(OrbitalRadishMod.MODID)
public class OrbitalRadishMod {

    public static final String MODID = "orbitalradish";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public OrbitalRadishMod(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus); // move all blocks here
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }



    private void addCreative(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.RADISH_BLOCK_ITEM.get());
            event.accept(ModItems.DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM.get());
            event.accept(ModItems.TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM.get());
            event.accept(ModItems.RADISH_BRICKS_ITEM.get());
            event.accept(ModItems.RADISH_SLAB_ITEM.get());
            event.accept(ModItems.RADISH_STAIRS_ITEM.get());
            event.accept(ModItems.RADISH_WALLS_ITEM.get());
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.RADISH_STICK.get());
        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.RADISH.get());
            event.accept(ModItems.COOKED_RADISH.get());
            event.accept(ModItems.RADISH_LEAF.get());
            event.accept(ModItems.RADISH_STEW.get());
        }

    }
}