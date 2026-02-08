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

@Mod(OrbitalRadishMod.MODID)
public class OrbitalRadishMod {

    public static final String MODID = "orbitalradish";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Block> RADISH_CROP = BLOCKS.register("radish_crop",
            () -> new RadishCrop(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)));

    public static final RegistryObject<Item> COOKED_RADISH = ITEMS.register("cooked_radish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.4f).build())));

    public static final RegistryObject<Item> RADISH_ARROW = ITEMS.register("radish_arrow",
            () -> new com.orbital.orbitalradish.item.RadishArrowItem(new Item.Properties()));

    public static final RegistryObject<Item> RADISH_LEAF = ITEMS.register("radish_leaf",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RADISH_STEW = ITEMS.register(
            "radish_stew",
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

    public static final RegistryObject<Block> RADISH_BLOCK = BLOCKS.register("radish_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(0.5f)
                    .sound(SoundType.GRASS)
                    .mapColor(MapColor.PLANT)));

    public static final RegistryObject<Item> RADISH_BLOCK_ITEM = ITEMS.register("radish_block",
            () -> new BlockItem(RADISH_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Block> DOUBLE_COMPRESSED_RADISH_BLOCK = BLOCKS.register("double_compressed_radish_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(1.0f)
                    .sound(SoundType.GRASS)
                    .mapColor(MapColor.PLANT)));

    public static final RegistryObject<Item> DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM = ITEMS.register("double_compressed_radish_block",
            () -> new BlockItem(DOUBLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Block> TRIPLE_COMPRESSED_RADISH_BLOCK = BLOCKS.register("triple_compressed_radish_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(1.5f)
                    .sound(SoundType.GRASS)
                    .mapColor(MapColor.PLANT)));

    public static final RegistryObject<Item> TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM = ITEMS.register("triple_compressed_radish_block",
            () -> new BlockItem(TRIPLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Block> RADISH_BRICKS = BLOCKS.register("radish_bricks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.STONE)));

    public static final RegistryObject<Item> RADISH_BRICKS_ITEM = ITEMS.register("radish_bricks",
            () -> new BlockItem(RADISH_BRICKS.get(), new Item.Properties()));

    public static final RegistryObject<Block> RADISH_STAIRS = BLOCKS.register(
            "radish_stairs",
            () -> new StairBlock(
                    RADISH_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
            )
    );

    public static final RegistryObject<Item> RADISH_STAIRS_ITEM = ITEMS.register("radish_stairs",
            () -> new BlockItem(RADISH_STAIRS.get(), new Item.Properties()));

    public static final RegistryObject<Block> RADISH_SLAB = BLOCKS.register("radish_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
            ));

    public static final RegistryObject<Item> RADISH_SLAB_ITEM = ITEMS.register("radish_slab",
            () -> new BlockItem(RADISH_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<Block> RADISH_WALLS = BLOCKS.register(
            "radish_walls",
            () -> new WallBlock(
                    BlockBehaviour.Properties.of()
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
            )
    );

    public static final RegistryObject<Item> RADISH_WALLS_ITEM = ITEMS.register("radish_walls",
            () -> new BlockItem(RADISH_WALLS.get(), new Item.Properties()));

    public static final RegistryObject<Item> RADISH_STICK = ITEMS.register("radish_stick",
            () -> new RadishStickItem(new Item.Properties().durability(99999999)));

    public OrbitalRadishMod(IEventBus modEventBus) {
        LOGGER.info("[DEBUG] Mod constructor started");

        modEventBus.addListener(this::commonSetup);
        LOGGER.info("[DEBUG] Added commonSetup listener");

        BLOCKS.register(modEventBus);
        LOGGER.info("[DEBUG] Registered BLOCKS");

        ModItems.ITEMS.register(modEventBus);
        LOGGER.info("[DEBUG] Registered ITEMS");

        ModEntities.ENTITIES.register(modEventBus);
        LOGGER.info("[DEBUG] Registered ENTITIES");

        CREATIVE_MODE_TABS.register(modEventBus);
        LOGGER.info("[DEBUG] Registered CREATIVE_MODE_TABS");

        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("[DEBUG] Registered mod to Forge event bus");

        modEventBus.addListener(this::addCreative);
        LOGGER.info("[DEBUG] Added addCreative listener");

        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        LOGGER.info("[DEBUG] Registered config");

        LOGGER.info("[DEBUG] Mod constructor finished");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[DEBUG] commonSetup started");
        LOGGER.info("[DEBUG] commonSetup finished");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        LOGGER.info("[DEBUG] addCreative called for tab: {}", event.getTabKey().location());

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            LOGGER.info("[DEBUG] Adding building blocks");
            event.accept(RADISH_BLOCK_ITEM.get());
            event.accept(DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM.get());
            event.accept(TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM.get());
            event.accept(RADISH_BRICKS_ITEM.get());
            event.accept(RADISH_SLAB_ITEM.get());
            event.accept(RADISH_STAIRS_ITEM.get());
            event.accept(RADISH_WALLS_ITEM.get());
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            LOGGER.info("[DEBUG] Adding combat items");
            event.accept(RADISH_STICK.get());
        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            LOGGER.info("[DEBUG] Adding food items");
            event.accept(ModItems.RADISH.get());
            event.accept(COOKED_RADISH.get());
            event.accept(RADISH_LEAF.get());
            event.accept(RADISH_STEW.get());
        }

        LOGGER.info("[DEBUG] addCreative finished for tab: {}", event.getTabKey().location());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[DEBUG] onServerStarting called");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("[DEBUG] onClientSetup called, player name >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}