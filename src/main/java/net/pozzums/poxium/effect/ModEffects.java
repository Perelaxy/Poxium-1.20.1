package net.pozzums.poxium.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.pozzums.poxium.Poxium;

import java.util.UUID;

public class ModEffects {
    // UUID for the max health modifier
    public static final UUID POXIUM_BLIGHT_MAX_HEALTH_MODIFIER_ID = 
        UUID.fromString("a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d");
    
    // Register the Poxium Blight effect
    public static final StatusEffect POXIUM_BLIGHT = registerEffect("poxium_blight", new PoxiumBlightEffect());

    private static StatusEffect registerEffect(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Poxium.MOD_ID, name), effect);
    }

    public static void register() {
        Poxium.LOGGER.info("Registering effects for " + Poxium.MOD_ID);
    }
}
