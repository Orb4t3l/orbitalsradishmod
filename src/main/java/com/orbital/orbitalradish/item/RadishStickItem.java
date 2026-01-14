package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.OrbitalRadishMod;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class RadishStickItem extends BowItem {

    public RadishStickItem(Properties props) {
        super(props);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack ->
                stack.getItem() instanceof net.minecraft.world.item.ArrowItem
                        || stack.is(OrbitalRadishMod.RADISH_ARROW.get());
    }
}
