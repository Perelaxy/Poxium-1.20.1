package net.pozzums.poxium.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PoxiumArmorItem extends ArmorItem {
    public PoxiumArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            // Check if player is wearing full Poxium armor
            if (hasFullPoxiumArmor(player)) {
                // Apply Speed II effect (Poxium Spores)
                player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    40, // 2 seconds duration (refreshed constantly)
                    1,  // Level 2 (Speed II)
                    false, // Not ambient
                    false, // Don't show particles (or true for spore effect)
                    true   // Show icon
                ));
            }
        }
        
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private boolean hasFullPoxiumArmor(PlayerEntity player) {
        ItemStack helmet = player.getInventory().armor.get(3);
        ItemStack chestplate = player.getInventory().armor.get(2);
        ItemStack leggings = player.getInventory().armor.get(1);
        ItemStack boots = player.getInventory().armor.get(0);

        return !helmet.isEmpty() && helmet.getItem() instanceof PoxiumArmorItem &&
               !chestplate.isEmpty() && chestplate.getItem() instanceof PoxiumArmorItem &&
               !leggings.isEmpty() && leggings.getItem() instanceof PoxiumArmorItem &&
               !boots.isEmpty() && boots.getItem() instanceof PoxiumArmorItem;
    }
}
