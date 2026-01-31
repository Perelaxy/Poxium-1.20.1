package net.pozzums.poxium.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class PoxiumToolMaterial implements ToolMaterial {
    public static final PoxiumToolMaterial INSTANCE = new PoxiumToolMaterial();

    @Override
    public int getDurability() {
        return 500; // Moderate durability
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 5.0f;
    }

    @Override
    public float getAttackDamage() {
        return 2.0f; // Base 3 damage (2 + 1 from sword base)
    }

    @Override
    public int getMiningLevel() {
        return 2; // Iron level
    }

    @Override
    public int getEnchantability() {
        return 14;
    }

    @Override
    public Ingredient getRepairIngredient() {
        // You can set this to use mycelium or another item for repairs
        return Ingredient.EMPTY;
    }
}
