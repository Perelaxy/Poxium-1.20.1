package net.pozzums.poxium.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.pozzums.poxium.Poxium;
import net.pozzums.poxium.effect.ModEffects;

public class ModPotions {
    // Poxium Blight Potion (30 seconds, level 1)
    public static final Potion POXIUM_BLIGHT_POTION = registerPotion("poxium_blight",
        new Potion(new StatusEffectInstance(ModEffects.POXIUM_BLIGHT, 600, 0)));
    
    // Long Poxium Blight Potion (90 seconds, level 1)
    public static final Potion LONG_POXIUM_BLIGHT_POTION = registerPotion("long_poxium_blight",
        new Potion(new StatusEffectInstance(ModEffects.POXIUM_BLIGHT, 1800, 0)));
    
    // Strong Poxium Blight Potion (30 seconds, level 2)
    public static final Potion STRONG_POXIUM_BLIGHT_POTION = registerPotion("strong_poxium_blight",
        new Potion(new StatusEffectInstance(ModEffects.POXIUM_BLIGHT, 600, 1)));

    private static Potion registerPotion(String name, Potion potion) {
        return Registry.register(Registries.POTION, new Identifier(Poxium.MOD_ID, name), potion);
    }

    public static void register() {
        Poxium.LOGGER.info("Registering potions for " + Poxium.MOD_ID);
    }
}
