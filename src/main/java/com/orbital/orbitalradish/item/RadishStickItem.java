package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RadishStickItem extends BowItem {

    public RadishStickItem(Properties props) {
        super(props);
    }

    @Override
    public java.util.function.Predicate<ItemStack> getAllSupportedProjectiles() {
        return s -> s.is(Items.STICK);
    }

    private ItemStack findAmmo(Player player) {
        ItemStack main = player.getMainHandItem();
        if (main.is(Items.STICK)) return main;

        ItemStack off = player.getOffhandItem();
        if (off.is(Items.STICK)) return off;

        NonNullList<ItemStack> inv = player.getInventory().items;
        for (ItemStack s : inv) {
            if (s.is(Items.STICK)) return s;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        boolean hasAmmo = !findAmmo(player).isEmpty() || player.getAbilities().instabuild;
        if (!hasAmmo) return InteractionResult.FAIL;
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide) return false;
        if (!(entity instanceof Player player)) return false;

        float charge = BowItem.getPowerForTime(getUseDuration(stack, player) - timeLeft);
        if (charge < 0.1F) return false;

        ItemStack ammo = findAmmo(player);
        boolean creative = player.getAbilities().instabuild;
        if (ammo.isEmpty() && !creative) return false;

        if (!creative && !ammo.isEmpty()) {
            ammo.shrink(1);
        }

        RadishArrowEntity arrow = new RadishArrowEntity(level, player, new ItemStack(Items.STICK));

        arrow.setBaseDamage(2.0D * charge);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, charge * 3.0F, 1.0F);

        if (charge >= 1.0F) arrow.setCritArrow(true);
        arrow.pickup = creative ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.DISALLOWED;

        level.addFreshEntity(arrow);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!creative) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
        }

        return true;
    }
}