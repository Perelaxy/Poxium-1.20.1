package net.pozzums.poxium.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

public class PoxiumBlightEffect extends StatusEffect {
    public PoxiumBlightEffect() {
        super(StatusEffectCategory.HARMFUL, 0x5A4D3E); // Dark brownish color for the effect
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // This is called every tick while the effect is active
        // Update max health if level is 10 or higher
        if (amplifier >= 9) { // Level 10 or higher (amplifier is 0-indexed)
            updateMaxHealth(entity, amplifier);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // Return true to allow applyUpdateEffect to be called every tick
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        
        // Apply max health reduction if level is 10 or higher
        if (amplifier >= 9) { // amplifier is 0-based, so level 10 = amplifier 9
            updateMaxHealth(entity, amplifier);
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        
        // Remove the max health modifier
        if (entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH) != null) {
            entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                .removeModifier(ModEffects.POXIUM_BLIGHT_MAX_HEALTH_MODIFIER_ID);
        }
        
        // When the effect runs out, increase its level by 1 (up to level 15)
        // This creates the escalating threat mechanic
        if (amplifier < 14) { // Max level 15 (amplifier 14)
            int newAmplifier = amplifier + 1;
            int duration = 600; // 30 seconds (600 ticks)
            
            StatusEffectInstance newEffect = new StatusEffectInstance(
                ModEffects.POXIUM_BLIGHT,
                duration,
                newAmplifier,
                false, // ambient
                true,  // show particles
                true   // show icon
            );
            
            entity.addStatusEffect(newEffect);
        }
    }

    private void updateMaxHealth(LivingEntity entity, int amplifier) {
        if (entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH) == null) {
            return;
        }
        
        // Remove old modifier if it exists
        entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
            .removeModifier(ModEffects.POXIUM_BLIGHT_MAX_HEALTH_MODIFIER_ID);
        
        // Calculate health reduction
        // Level 10 (amplifier 9) = -0.5 hearts = -1.0 health
        // Level 11 (amplifier 10) = -1.0 hearts = -2.0 health
        // Level 12 (amplifier 11) = -1.5 hearts = -3.0 health
        // etc. until minimum of 5 hearts (10.0 health)
        int level = amplifier + 1;
        double healthReduction = 0.0;
        
        if (level >= 10) {
            // Each level above 9 removes 0.5 hearts (1.0 health)
            healthReduction = -((level - 9) * 1.0);
            
            // Cap the reduction so player always has at least 5 hearts (10.0 health)
            double currentMaxHealth = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue();
            double minimumHealth = 10.0; // 5 hearts
            
            if (currentMaxHealth + healthReduction < minimumHealth) {
                healthReduction = minimumHealth - currentMaxHealth;
            }
        }
        
        if (healthReduction < 0) {
            EntityAttributeModifier modifier = new EntityAttributeModifier(
                ModEffects.POXIUM_BLIGHT_MAX_HEALTH_MODIFIER_ID,
                "Poxium Blight Max Health Reduction",
                healthReduction,
                EntityAttributeModifier.Operation.ADDITION
            );
            
            entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(modifier);
        }
    }
}
