package com.orbital.orbitalradish.block;

import com.orbital.orbitalradish.ModItems;
import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.ItemLike;

/**
 * Simple CropBlock implementation — CropBlock automatically uses the block loot table
 * at data/<modid>/loot_tables/blocks/<blockname>.json when broken.
 */
public class RadishCrop extends CropBlock {

    public RadishCrop(Properties props) {
        super(props);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.RADISH.get();
    }
}



