package net.pozzums.poxium.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.pozzums.poxium.Poxium;

public class ModItems {
    // Register the Poxium Sword
    // Base damage: 3-4 (using attackDamage of 3, which adds to base 1 = 4 total damage)
    // Attack speed: 1.8 (using -2.2, which gives 4.0 - 2.2 = 1.8)
    public static final Item POXIUM_SWORD = registerItem("poxium_sword",
        new PoxiumSwordItem(
            PoxiumToolMaterial.INSTANCE,
            3, // Attack damage modifier (adds to base 1, total 4 damage)
            -2.2f, // Attack speed modifier (base 4.0 - 2.2 = 1.8 attacks per second)
            new FabricItemSettings()
        )
    );

    // Register Poxium Armor
    public static final Item POXIUM_HELMET = registerItem("poxium_helmet",
        new PoxiumArmorItem(PoxiumArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new FabricItemSettings())
    );
    
    public static final Item POXIUM_CHESTPLATE = registerItem("poxium_chestplate",
        new PoxiumArmorItem(PoxiumArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new FabricItemSettings())
    );
    
    public static final Item POXIUM_LEGGINGS = registerItem("poxium_leggings",
        new PoxiumArmorItem(PoxiumArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new FabricItemSettings())
    );
    
    public static final Item POXIUM_BOOTS = registerItem("poxium_boots",
        new PoxiumArmorItem(PoxiumArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new FabricItemSettings())
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Poxium.MOD_ID, name), item);
    }

    public static void register() {
        Poxium.LOGGER.info("Registering items for " + Poxium.MOD_ID);
        
        // Add to combat item group
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(POXIUM_SWORD);
            entries.add(POXIUM_HELMET);
            entries.add(POXIUM_CHESTPLATE);
            entries.add(POXIUM_LEGGINGS);
            entries.add(POXIUM_BOOTS);
        });
    }
}
