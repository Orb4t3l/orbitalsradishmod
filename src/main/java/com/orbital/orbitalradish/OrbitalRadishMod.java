package com.orbital.orbitalradish;

import com.orbital.orbitalradish.block.RadishCrop;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
import net.minecraft.world.level.block.ComposterBlock;


@Mod(OrbitalRadishMod.MODID)
public class OrbitalRadishMod {
    public static final String MODID = "orbitalradish";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public static final RegistryObject<Block> RADISH_CROP = BLOCKS.register("radish_crop",
            () -> new RadishCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    // Radish item: edible AND placeable (ItemNameBlockItem ties the item to placing the crop block)

    public static final RegistryObject<Item> COOKED_RADISH = ITEMS.register("cooked_radish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4f).build())));

    public static final RegistryObject<Item> RADISH_ARROW = ITEMS.register("radish_arrow",
            () -> new com.orbital.orbitalradish.item.RadishArrowItem(new Item.Properties()));

    public static final RegistryObject<Item> RADISH_LEAF = ITEMS.register("radish_leaf",
            () -> new Item(new Item.Properties()));



    public static final RegistryObject<Block> RADISH_BLOCK = BLOCKS.register("radish_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final RegistryObject<Item> RADISH_BLOCK_ITEM = ITEMS.register("radish_block",
            () -> new BlockItem(RADISH_BLOCK.get(), new Item.Properties()));


    public static final RegistryObject<Block> DOUBLE_COMPRESSED_RADISH_BLOCK = BLOCKS.register("double_compressed_radish_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT).mapColor(MapColor.PLANT)));

    public static final RegistryObject<Item> DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM = ITEMS.register("double_compressed_radish_block",
            () -> new BlockItem(DOUBLE_COMPRESSED_RADISH_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> RADISH_STICK = ITEMS.register("radish_stick",
            () -> new RadishStickItem(new Item.Properties().durability(99999999)));


    public OrbitalRadishMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        // register blocks first
        BLOCKS.register(modEventBus);

        // register the centralized items register (only one)
        ModItems.ITEMS.register(modEventBus);

        // other registries
        ModEntities.ENTITIES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        // use BuildCreativeModeTabContentsEvent to place items into tabs
        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }



    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("OrbitalRadish: common setup");

        // run as queued work so it executes safely after registries are ready
        event.enqueueWork(() -> {
            // add fresh radishes to the composter at a modest chance
            ComposterBlock.COMPOSTABLES.put(ModItems.RADISH.get(), 0.3F);

            // cooked radish should compost more reliably (optional)
            ComposterBlock.COMPOSTABLES.put(COOKED_RADISH.get(), 0.65F);

            // if you also want the radish item used as arrow visual to be compostable:
            // ComposterBlock.COMPOSTABLES.put(RADISH_ARROW.get(), 0.3F);
        });
    }

    // This is how you add items to vanilla tabs in 1.20.x (no .tab on Item.Properties)
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(RADISH_BLOCK_ITEM.get());
            event.accept(DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM.get());

        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(RADISH_STICK.get());

        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.RADISH.get());
            event.accept(COOKED_RADISH.get());
            event.accept(RADISH_LEAF.get());
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
