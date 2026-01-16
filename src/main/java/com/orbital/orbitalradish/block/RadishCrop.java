package com.orbital.orbitalradish.block;

import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.ItemLike;

/**
 * Simple CropBlock implementation — CropBlock automatically uses the block loot table
 * at data/<modid>/loot_tables/blocks/<blockname>.json when broken.
 */
public class RadishCrop extends CropBlock {

    public RadishCrop(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * Which item is used to plant this crop (and what seeds are returned).
     * You registered the placeable item as OrbitalRadishMod.RADISH — return that.
     */
    @Override
    protected ItemLike getBaseSeedId() {
        return OrbitalRadishMod.RADISH.get();
    }
}
