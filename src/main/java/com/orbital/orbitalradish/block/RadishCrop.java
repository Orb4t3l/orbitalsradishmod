package com.orbital.orbitalradish.block;

import com.orbital.orbitalradish.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

public class RadishCrop extends CropBlock {

    public RadishCrop(Properties props) {
        super(props);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.RADISH;
    }
}
