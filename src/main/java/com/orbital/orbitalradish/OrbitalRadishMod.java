package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;
import com.orbital.orbitalradish.item.RadishStickItem;
import net.neoforged.fml.ModLoadingContext;
import net.minecraft.core.registries.BuiltInRegistries;




@Mod(OrbitalRadishMod.MODID)
public class OrbitalRadishMod {

    public static final String MODID = "orbitalradish";

    private static final Logger LOGGER = LogUtils.getLogger();

    // IMPORTANT: Use BuiltInRegistries.BLOCK (singular) not BLOCKS!
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    // IMPORTANT: In 1.20.2, use .copy() NOT .ofFullCopy()!
    public static final DeferredHolder<Block, RadishCrop> RADISH_CROP = BLOCKS.register("radish_crop",
            () -> new RadishCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));


    public static final DeferredHolder<Item, Item> COOKED_RADISH = ITEMS.register("cooked_radish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4f).build())));

    public static final DeferredHolder<Item, com.orbital.orbitalradish.item.RadishArrowItem> RADISH_ARROW = ITEMS.register("radish_arrow",
            () -> new com.orbital.orbitalradish.item.RadishArrowItem(new Item.Properties()));

    public static final DeferredHolder<Item, Item> RADISH_LEAF = ITEMS.register("radish_leaf",
            () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, BowlFoodItem> RADISH_STEW = ITEMS.register(
            "radish_stew",
            () -> new BowlFoodItem(
                    new Item.Properties()
                            .stacksTo(1)
                            .craftRemainder(Items.BOWL)
                            .food(new FoodProperties.Builder()
                                    .nutrition(8)
                                    .saturationMod(0.8f)
                                    .build()
                            )
            )
    );



    public static final DeferredHolder<Block, Block> RADISH_BLOCK = BLOCKS.register("radish_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final DeferredHolder<Item, BlockItem> RADISH_BLOCK_ITEM = ITEMS.register("radish_block",
            () -> new BlockItem(RADISH_BLOCK.get(), new Item.Properties()));


    public static final DeferredHolder<Block, Block> DOUBLE_COMPRESSED_RADISH_BLOCK = BLOCKS.register("double_compressed_radish_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final DeferredHolder<Item, BlockItem> DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM = ITEMS.register("double_compressed_radish_block",
            () -> new BlockItem(DOUBLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Block, Block> TRIPLE_COMPRESSED_RADISH_BLOCK = BLOCKS.register("triple_compressed_radish_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final DeferredHolder<Item, BlockItem> TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM = ITEMS.register("triple_compressed_radish_block",
            () -> new BlockItem(TRIPLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final DeferredHolder<Block, Block> RADISH_BRICKS = BLOCKS.register("radish_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.STONE)));

    public static final DeferredHolder<Item, BlockItem> RADISH_BRICKS_ITEM = ITEMS.register("radish_bricks",
            () -> new BlockItem(RADISH_BRICKS.get(), new Item.Properties()));

    public static final DeferredHolder<Block, StairBlock> RADISH_STAIRS = BLOCKS.register("radish_stairs",
            () -> new StairBlock(
                    RADISH_BRICKS.get().defaultBlockState(),  // Direct BlockState for 1.20.2+
                    BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)
            ));




    public static final DeferredHolder<Item, BlockItem> RADISH_STAIRS_ITEM = ITEMS.register("radish_stairs",
            () -> new BlockItem(RADISH_STAIRS.get(), new Item.Properties()));

    public static final DeferredHolder<Block, SlabBlock> RADISH_SLAB = BLOCKS.register("radish_slab",
            () -> new SlabBlock(
                    BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)
            ));


    public static final DeferredHolder<Item, BlockItem> RADISH_SLAB_ITEM = ITEMS.register("radish_slab",
            () -> new BlockItem(RADISH_SLAB.get(), new Item.Properties()));

    public static final DeferredHolder<Block, WallBlock> RADISH_WALLS = BLOCKS.register(
            "radish_walls",
            () -> new WallBlock(
                    BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)
            )
    );


    public static final DeferredHolder<Item, BlockItem> RADISH_WALLS_ITEM = ITEMS.register("radish_walls",
            () -> new BlockItem(RADISH_WALLS.get(), new Item.Properties()));

    public static final DeferredHolder<Item, RadishStickItem> RADISH_STICK = ITEMS.register("radish_stick",
            () -> new RadishStickItem(new Item.Properties().durability(99999999)));

    public OrbitalRadishMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // register listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        // register registries on the mod event bus
        BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // register for general NeoForge events
        NeoForge.EVENT_BUS.register(this);

        // register config via ModLoadingContext
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("OrbitalRadish: common setup");

        // run as queued work so it executes safely after registries are ready
        event.enqueueWork(() -> {
            // add fresh radishes to the composter at a chance
            ComposterBlock.COMPOSTABLES.put(ModItems.RADISH.get(), 0.4F);

            ComposterBlock.COMPOSTABLES.put(ModItems.RADISH_LEAF.get(), 0.25F);

            // cooked radish should compost more reliably (optional)
            ComposterBlock.COMPOSTABLES.put(COOKED_RADISH.get(), 0.7F);
        });
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(RADISH_BLOCK_ITEM.get());
            event.accept(DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM.get());
            event.accept(TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM.get());
            event.accept(RADISH_BRICKS_ITEM.get());
            event.accept(RADISH_SLAB_ITEM.get());
            event.accept(RADISH_STAIRS_ITEM.get());
            event.accept(RADISH_WALLS_ITEM.get());

        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(RADISH_STICK.get());

        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.RADISH.get());
            event.accept(COOKED_RADISH.get());
            event.accept(RADISH_LEAF.get());
            event.accept(RADISH_STEW.get());
        }

    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("OrbitalRadish: server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("OrbitalRadish: client setup, player name >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}