package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class RadishStickItem extends BowItem {

    public RadishStickItem(Properties props) {
        super(props);  // SET ENCHANTABILITY HERE!
    }

    // REMOVE THESE - THEY DON'T WORK IN 1.21:
    // @Override
    // public int getEnchantmentValue() {
    //     return 15;
    // }

    // @Override
    // public boolean isEnchantable(ItemStack stack) {
    //     return true;
    // }

    /* ---------------- ENCHANTING ---------------- */



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
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    /* ---------------- FIRE ---------------- */

    // Helper method to get enchantment level in 1.21
    private int getEnchantLevel(ItemStack stack, Level level, ResourceKey<Enchantment> enchantmentKey) {
        Holder<Enchantment> holder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(enchantmentKey);
        return stack.getEnchantments().getLevel(holder);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        System.out.println("DEBUG: releaseUsing called!");
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;
        System.out.println("DEBUG: Server side, is player");

        int power = getEnchantLevel(stack, level, Enchantments.POWER);
        int punch = getEnchantLevel(stack, level, Enchantments.PUNCH);
        int flame = getEnchantLevel(stack, level, Enchantments.FLAME);
        int infinity = getEnchantLevel(stack, level, Enchantments.INFINITY);

        float charge = getPowerForTime(getUseDuration(stack, player) - timeLeft);
        System.out.println("DEBUG: Charge = " + charge);
        if (charge < 0.1F) return;
        System.out.println("DEBUG: Charge check passed!");

        ItemStack ammo = findAmmo(player);
        boolean creative = player.getAbilities().instabuild;
        System.out.println("DEBUG: Has ammo = " + !ammo.isEmpty() + ", creative = " + creative);
        if (ammo.isEmpty() && !creative) return;

        if (!creative && infinity == 0 && !ammo.isEmpty()) {
            ammo.shrink(1);
        }

        System.out.println("DEBUG: Creating arrow entity...");
        RadishArrowEntity arrow = new RadishArrowEntity(
                level,
                player,
                new ItemStack(Items.STICK)
        );

        System.out.println("DEBUG: Arrow created, setting properties...");
        // ... rest of code

        System.out.println("DEBUG: Adding arrow to world...");
        level.addFreshEntity(arrow);
        System.out.println("DEBUG: Arrow added!");

        // ... rest of code
    }

    public static float getPowerForTime(int drawTime) {
        float f = (float) drawTime / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }
}