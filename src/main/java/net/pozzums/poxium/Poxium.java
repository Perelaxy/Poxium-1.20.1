package net.pozzums.poxium;

import net.fabricmc.api.ModInitializer;
import net.pozzums.poxium.effect.ModEffects;
import net.pozzums.poxium.item.ModItems;
import net.pozzums.poxium.potion.ModPotions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Poxium implements ModInitializer {
	public static final String MOD_ID = "poxium";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing Poxium Mod...");
		
		// Register effects first as they are referenced by items and potions
		ModEffects.register();
		
		// Register items
		ModItems.register();
		
		// Register potions
		ModPotions.register();
		
		LOGGER.info("Poxium Mod initialized successfully!");
	}
}