package com.orbital.orbitalradish;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitalRadishMod implements ModInitializer {

    public static final String MODID = "orbitalradish";
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        Config.load();

        ModBlocks.init();
        ModItems.init();
        ModEntities.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.accept(ModItems.RADISH_BLOCK_ITEM);
            entries.accept(ModItems.DOUBLE_COMPRESSED_RADISH_BLOCK_ITEM);
            entries.accept(ModItems.TRIPLE_COMPRESSED_RADISH_BLOCK_ITEM);
            entries.accept(ModItems.RADISH_BRICKS_ITEM);
            entries.accept(ModItems.RADISH_SLAB_ITEM);
            entries.accept(ModItems.RADISH_STAIRS_ITEM);
            entries.accept(ModItems.RADISH_WALLS_ITEM);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.accept(ModItems.RADISH_STICK);
            entries.accept(ModItems.RADISH_ARROW);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.accept(ModItems.RADISH);
            entries.accept(ModItems.COOKED_RADISH);
            entries.accept(ModItems.RADISH_LEAF);
            entries.accept(ModItems.RADISH_STEW);
        });

        RadishAdvancementHandler.init();
        RadishPigHandler.init();
        VillagerPickUpHandler.init();
        VillagerReplanter.init();
        VillagerTradesEventHandler.init();
        VillagerPickupDebugger.init();

        LOGGER.info("OrbitalRadish: common setup complete");
    }
}
