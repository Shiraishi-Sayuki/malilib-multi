package fi.dy.masa.malilib.util;

import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * 装備/道具の分類ヘルパー - 1.21のmalilibにあったユーティリティの1.20.1移植版。
 * セマンティクスはtweakerooのソート/移動ヒューリスティック向けの近似実装。
 */
public class EquipmentUtils
{
    public static boolean isSword(@Nullable ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof SwordItem;
    }

    public static boolean isPickAxe(@Nullable ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof PickaxeItem;
    }

    public static boolean isAxe(@Nullable ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof AxeItem;
    }

    public static boolean isShovel(@Nullable ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof ShovelItem;
    }

    public static boolean isHoe(@Nullable ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof HoeItem;
    }

    public static boolean isAnyWeapon(@Nullable ItemStack stack)
    {
        if (stack == null) { return false; }
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem ||
               item instanceof TridentItem || item instanceof BowItem || item instanceof CrossbowItem;
    }

    public static boolean isRangedWeapon(@Nullable ItemStack stack)
    {
        if (stack == null) { return false; }
        Item item = stack.getItem();
        return item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem;
    }

    public static boolean isAnyTool(@Nullable ItemStack stack)
    {
        if (stack == null) { return false; }
        Item item = stack.getItem();
        return item instanceof ToolItem || item instanceof ShearsItem;
    }

    public static boolean isRegularTool(@Nullable ItemStack stack)
    {
        return isAxe(stack) || isShovel(stack) || isHoe(stack);
    }

    public static boolean isMiscTool(@Nullable ItemStack stack)
    {
        return isAnyTool(stack) &&
               isSword(stack) == false && isPickAxe(stack) == false &&
               isAxe(stack) == false && isShovel(stack) == false && isHoe(stack) == false;
    }

    public static boolean isCorrectTool(@Nullable ItemStack stack, @NotNull BlockState state)
    {
        return stack != null && stack.isSuitableFor(state);
    }

    public static float getMiningSpeed(@Nullable ItemStack stack, @NotNull BlockState state)
    {
        return stack != null ? stack.getMiningSpeedMultiplier(state) : 1.0f;
    }

    public static int getEnchantmentLevel(@Nullable ItemStack stack, @NotNull Enchantment enchantment)
    {
        return stack != null ? net.minecraft.enchantment.EnchantmentHelper.getLevel(enchantment, stack) : 0;
    }

    public static boolean hasSilkTouch(@Nullable ItemStack stack)
    {
        return getEnchantmentLevel(stack, Enchantments.SILK_TOUCH) > 0;
    }

    /** 候補がpreviousと同等以上のエンチャントレベルなら1、そうでなければ0 */
    public static int hasSameOrBetterEnchantment(@Nullable ItemStack candidate, @Nullable ItemStack previous, @NotNull Enchantment enchantment)
    {
        return getEnchantmentLevel(candidate, enchantment) >= getEnchantmentLevel(previous, enchantment) ? 1 : 0;
    }

    /** MAINHANDの攻撃力と攻撃速度を返す */
    public static Pair<Double, Double> getDamageAndSpeedAttributes(@Nullable ItemStack stack)
    {
        double damage = 1.0;
        double speed = 4.0;

        if (stack != null)
        {
            for (var entry : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).entries())
            {
                EntityAttributeModifier modifier = entry.getValue();

                if (entry.getKey() == EntityAttributes.GENERIC_ATTACK_DAMAGE)
                {
                    damage += modifier.getValue();
                }
                else if (entry.getKey() == EntityAttributes.GENERIC_ATTACK_SPEED)
                {
                    speed += modifier.getValue();
                }
            }
        }

        return new Pair<>(damage, speed);
    }
}
