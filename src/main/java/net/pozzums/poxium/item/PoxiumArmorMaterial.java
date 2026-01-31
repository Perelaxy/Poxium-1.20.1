package net.pozzums.poxium.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class PoxiumArmorMaterial implements ArmorMaterial {
    public static final PoxiumArmorMaterial INSTANCE = new PoxiumArmorMaterial();
    
    // Armor protection values for each piece (helmet, chestplate, leggings, boots)
    // For reference: Diamond is [3, 8, 6, 3], Netherite is [3, 8, 6, 3] with knockback resistance
    // Poxium is slightly weaker: [2, 7, 5, 2]
    private static final int[] BASE_DURABILITY = {13, 15, 16, 11};
    private static final int[] PROTECTION_VALUES = {2, 7, 5, 2};

    @Override
    public int getDurability(ArmorItem.Type type) {
        // Total durability = base durability * durability multiplier
        // Diamond multiplier is 33, we'll use 28 for slightly less durability
        return BASE_DURABILITY[type.getEquipmentSlot().getEntitySlotId()] * 28;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return PROTECTION_VALUES[type.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 15; // Higher than diamond (10), same as gold
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE; // Reuse netherite sound
    }

    @Override
    public Ingredient getRepairIngredient() {
        // Can be repaired with mycelium
        return Ingredient.EMPTY; // Will set this up later if needed
    }

    @Override
    public String getName() {
        return "poxium";
    }

    @Override
    public float getToughness() {
        return 2.0F; // Between diamond (2.0) and netherite (3.0)
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F; // No knockback resistance (unlike netherite which has 0.1 per piece)
    }
}
