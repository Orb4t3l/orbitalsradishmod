package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class RadishStickItem extends BowItem {

    public RadishStickItem(Properties props) {
        super(props);
    }

    /* ---------------- ENCHANTING ---------------- */

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    // DO NOT override isBookEnchantable in 1.20.6
    // DO NOT manually read stored enchantments
    // BowItem already supports Power/Punch/Flame/Infinity correctly

    /* ---------------- AMMO ---------------- */

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

    /* ---------------- USE ---------------- */

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        boolean hasAmmo = !findAmmo(player).isEmpty() || player.getAbilities().instabuild;
        if (!hasAmmo) return InteractionResultHolder.fail(held);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(held);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    /* ---------------- FIRE ---------------- */

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        int power = stack.getEnchantmentLevel(Enchantments.POWER);
        int punch = stack.getEnchantmentLevel(Enchantments.PUNCH);
        int flame = stack.getEnchantmentLevel(Enchantments.FLAME);
        int infinity = stack.getEnchantmentLevel(Enchantments.INFINITY);

        float charge = getPowerForTime(getUseDuration(stack) - timeLeft);
        if (charge < 0.1F) return;

        ItemStack ammo = findAmmo(player);
        boolean creative = player.getAbilities().instabuild;
        if (ammo.isEmpty() && !creative) return;

        if (!creative && infinity == 0 && !ammo.isEmpty()) {
            ammo.shrink(1);
        }

        RadishArrowEntity arrow = new RadishArrowEntity(
                level,
                player,
                new ItemStack(Items.STICK)
        );

        double damage = 2.0D + (power > 0 ? power * 0.5D + 0.5D : 0.0D);
        arrow.setBaseDamage(damage * charge);

        if (punch > 0) arrow.setKnockback(punch);
        if (flame > 0) arrow.setRemainingFireTicks(100);
        if (charge >= 1.0F) arrow.setCritArrow(true);
        if (infinity > 0) arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(),
                0.0F, charge * 3.0F, 1.0F);

        level.addFreshEntity(arrow);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + charge * 0.5F);

        if (!creative && infinity == 0) {
            EquipmentSlot slot = player.getUsedItemHand() == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND;
            stack.hurtAndBreak(1, player, slot);
        }
    }

    public static float getPowerForTime(int drawTime) {
        float f = (float) drawTime / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }
}
