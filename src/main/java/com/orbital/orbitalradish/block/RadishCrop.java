package com.orbital.orbitalradish.block;

import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.ItemLike;

public class RadishCrop extends CropBlock {
    public RadishCrop(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        // Return the radish item so the radish itself plants (like potato/carrot)
        return OrbitalRadishMod.RADISH.get();
    }
}
