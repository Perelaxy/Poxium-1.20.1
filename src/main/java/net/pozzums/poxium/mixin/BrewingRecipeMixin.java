package net.pozzums.poxium.mixin;

import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.pozzums.poxium.potion.ModPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeMixin {
    
    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private static void registerPoxiumBrewingRecipes(CallbackInfo ci) {
        // Register brewing recipe: Awkward Potion + Mycelium = Poxium Blight Potion
        BrewingRecipeRegistry.registerPotionRecipe(
            Potions.AWKWARD, 
            Items.MYCELIUM, 
            ModPotions.POXIUM_BLIGHT_POTION
        );
        
        // Register brewing recipe for long version
        BrewingRecipeRegistry.registerPotionRecipe(
            ModPotions.POXIUM_BLIGHT_POTION, 
            Items.REDSTONE, 
            ModPotions.LONG_POXIUM_BLIGHT_POTION
        );
        
        // Register brewing recipe for strong version
        BrewingRecipeRegistry.registerPotionRecipe(
            ModPotions.POXIUM_BLIGHT_POTION, 
            Items.GLOWSTONE_DUST, 
            ModPotions.STRONG_POXIUM_BLIGHT_POTION
        );
    }
}
