package net.pozzums.poxium.mixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.pozzums.poxium.effect.ModEffects;
import net.pozzums.poxium.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class PoxiumSwordDamageMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("PoxiumSwordDamageMixin");
    
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyPoxiumSwordDamage(float amount, DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        // Check if the damage source is from the Poxium Sword
        if (source.getAttacker() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandStack();
            
            if (weapon.getItem() == ModItems.POXIUM_SWORD) {
                // Get current Poxium Blight level on the target
                StatusEffectInstance currentEffect = entity.getStatusEffect(ModEffects.POXIUM_BLIGHT);
                
                float originalDamage = amount;
                
                if (currentEffect != null) {
                    int currentLevel = currentEffect.getAmplifier() + 1; // Convert amplifier to level (1-indexed)
                    
                    // Add damage bonus based on level (max bonus at level 4)
                    // Level 1 = +1 damage, Level 2 = +2 damage, Level 3 = +3 damage, Level 4+ = +4 damage
                    int damageBonus = Math.min(currentLevel, 4);
                    amount += damageBonus;
                    
                    LOGGER.info("Poxium Sword hit - Target has Poxium Blight level {} - Damage: {} + {} bonus = {} total", 
                        currentLevel, originalDamage, damageBonus, amount);
                } else {
                    LOGGER.info("Poxium Sword hit - Target has no Poxium Blight - Base damage: {}", originalDamage);
                }
            }
        }
        
        return amount;
    }
}
