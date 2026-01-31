package net.pozzums.poxium.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.pozzums.poxium.effect.ModEffects;

public class PoxiumSwordItem extends SwordItem {
    private static final Logger LOGGER = LoggerFactory.getLogger("PoxiumSwordItem");
    
    public PoxiumSwordItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.getWorld().isClient()) {
            // Get current Poxium Blight effect on the target
            StatusEffectInstance currentEffect = target.getStatusEffect(ModEffects.POXIUM_BLIGHT);
            
            int newAmplifier;
            if (currentEffect == null) {
                // No effect yet - start at level 1 (amplifier 0)
                newAmplifier = 0;
                LOGGER.info("Target has no Poxium Blight - applying level 1 (amplifier 0)");
            } else {
                // Has effect - increase by 1 level
                int currentAmplifier = currentEffect.getAmplifier();
                newAmplifier = Math.min(currentAmplifier + 1, 14); // Cap at level 15 (amplifier 14)
                LOGGER.info("Target has Poxium Blight level {} (amplifier {}) - increasing to level {} (amplifier {})", 
                    currentAmplifier + 1, currentAmplifier, newAmplifier + 1, newAmplifier);
            }
            
            int duration = 600; // 30 seconds (600 ticks)
            
            StatusEffectInstance newEffect = new StatusEffectInstance(
                ModEffects.POXIUM_BLIGHT,
                duration,
                newAmplifier,
                false, // ambient
                true,  // show particles
                true   // show icon
            );
            
            target.addStatusEffect(newEffect);
            LOGGER.info("Applied Poxium Blight level {} to {}", newAmplifier + 1, target.getName().getString());
        }
        
        return super.postHit(stack, target, attacker);
    }
}
