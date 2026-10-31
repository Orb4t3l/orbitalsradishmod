package com.orbital.orbitalradish.item;

import com.orbital.orbitalradish.entity.RadishArrowEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class RadishStickItem extends BowItem {

    public RadishStickItem(Properties props) {
        super(props);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() == Items.STICK;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
        int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack);
        int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack);

        ItemStack found = ItemStack.EMPTY;
        InteractionHand foundHand = null;

        ItemStack main = player.getMainHandItem();
        if (main.getItem() == Items.STICK) {
            found = main;
            foundHand = InteractionHand.MAIN_HAND;
        } else {
            ItemStack off = player.getOffhandItem();
            if (off.getItem() == Items.STICK) {
                found = off;
                foundHand = InteractionHand.OFF_HAND;
            } else {
                NonNullList<ItemStack> items = player.getInventory().items;
                for (int i = 0; i < items.size(); i++) {
                    ItemStack s = items.get(i);
                    if (s.getItem() == Items.STICK) {
                        found = s;
                        break;
                    }
                }
            }
        }

        boolean hasAmmo = !found.isEmpty() || player.getAbilities().instabuild;
        if (!hasAmmo) {
            return;
        }

        boolean shouldConsume = !player.getAbilities().instabuild && infinityLevel == 0;

        if (shouldConsume && !found.isEmpty()) {
            if (foundHand != null) {
                player.getItemInHand(foundHand).shrink(1);
            } else {
                found.shrink(1);
            }
        }

        ItemStack renderStack = found.isEmpty() ? ItemStack.EMPTY : new ItemStack(Items.STICK);
        RadishArrowEntity arrow = new RadishArrowEntity(level, player, renderStack);

        double baseDamage = 1.0D;
        if (powerLevel > 0) {
            baseDamage += 0.5D * powerLevel + 0.5D;
        }
        arrow.setBaseDamage(baseDamage);

        if (punchLevel > 0) {
            arrow.setKnockback(punchLevel);
        }

        if (flameLevel > 0) {
            arrow.setSecondsOnFire(100);
        }

        arrow.setPos(player.getX(), player.getEyeY() - 0.10000000149011612D, player.getZ());
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 1.0F);

        if (infinityLevel > 0) {
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        level.addFreshEntity(arrow);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!player.getAbilities().instabuild) {
            ItemStack mainNow = player.getMainHandItem();
            if (!mainNow.isEmpty()) {
                player.getCooldowns().addCooldown(mainNow.getItem(), 5);
            }
        }
    }
}
