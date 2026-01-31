package net.pozzums.poxium.mixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pozzums.poxium.effect.ModEffects;
import net.pozzums.poxium.item.PoxiumArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class PoxiumArmorDeathMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("PoxiumArmorDeathMixin");
    
    static {
        LOGGER.info("========================================");
        LOGGER.info("POXIUM ARMOR DEATH MIXIN CLASS LOADED!");
        LOGGER.info("========================================");
    }
    
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPoxiumArmorDeath(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        LOGGER.info("=== DEATH EVENT TRIGGERED ===");
        LOGGER.info("Entity: {}", entity.getClass().getSimpleName());
        LOGGER.info("Is Player: {}", entity instanceof PlayerEntity);
        LOGGER.info("Is Client: {}", entity.getWorld().isClient());
        
        // Only proceed if this is a player on the server side
        if (!(entity instanceof PlayerEntity)) {
            LOGGER.info("Not a player - skipping");
            return;
        }
        
        if (entity.getWorld().isClient()) {
            LOGGER.info("Client side - skipping");
            return;
        }
        
        PlayerEntity player = (PlayerEntity) entity;
        LOGGER.info("Player name: {}", player.getName().getString());
        
        // Check if wearing Poxium armor
        boolean wearingPoxiumArmor = false;
        int poxiumPieces = 0;
        
        for (ItemStack armorPiece : player.getArmorItems()) {
            LOGGER.info("Armor slot - Item: {} | Class: {}", 
                armorPiece.getItem().toString(), 
                armorPiece.getItem().getClass().getSimpleName());
            
            if (armorPiece.getItem() instanceof PoxiumArmorItem) {
                wearingPoxiumArmor = true;
                poxiumPieces++;
                LOGGER.info("^^^ THIS IS POXIUM ARMOR! ^^^");
            }
        }
        
        LOGGER.info("Total Poxium armor pieces: {}", poxiumPieces);
        
        if (!wearingPoxiumArmor) {
            LOGGER.info("Player not wearing Poxium armor - no mycelium spread");
            return;
        }
        
        LOGGER.info("=== POXIUM ARMOR DETECTED - TRIGGERING MYCELIUM SPREAD ===");
        
        ServerWorld world = (ServerWorld) entity.getWorld();
        BlockPos deathPos = player.getBlockPos();
        
        LOGGER.info("Death position: {}", deathPos);
        LOGGER.info("Block at death: {}", world.getBlockState(deathPos).getBlock());
        
        // Spread mycelium
        int blocksConverted = spreadMycelium(world, deathPos);
        LOGGER.info("=== MYCELIUM SPREAD COMPLETE - {} BLOCKS CONVERTED ===", blocksConverted);
        
        // Apply Poxium Blight to the killer
        if (damageSource.getAttacker() instanceof LivingEntity killer) {
            LOGGER.info("Applying Poxium Blight to killer: {}", killer.getName().getString());
            applyPoxiumBlightToKiller(killer);
        } else {
            LOGGER.info("No killer entity - skipping Poxium Blight application");
        }
    }
    
    private int spreadMycelium(ServerWorld world, BlockPos center) {
        LOGGER.info("--- Starting mycelium spread ---");
        
        // Find ground position
        BlockPos groundPos = null;
        for (int y = 0; y <= 5; y++) {
            BlockPos checkPos = center.down(y);
            BlockState state = world.getBlockState(checkPos);
            
            LOGGER.info("Checking Y-{}: {} at {}", y, state.getBlock(), checkPos);
            
            if (canConvertToMycelium(state)) {
                groundPos = checkPos;
                LOGGER.info("Found convertible ground at {}", groundPos);
                break;
            }
        }
        
        if (groundPos == null) {
            LOGGER.warn("NO CONVERTIBLE GROUND FOUND!");
            return 0;
        }
        
        // Simple spreading algorithm
        List<BlockPos> toConvert = new ArrayList<>();
        int radius = 4;
        int maxBlocks = 40;
        
        // Collect all convertible blocks in radius
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos pos = groundPos.add(x, y, z);
                    
                    // Check if within radius
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance <= radius) {
                        BlockState state = world.getBlockState(pos);
                        if (canConvertToMycelium(state)) {
                            toConvert.add(pos);
                            
                            if (toConvert.size() >= maxBlocks) {
                                break;
                            }
                        }
                    }
                }
                if (toConvert.size() >= maxBlocks) {
                    break;
                }
            }
            if (toConvert.size() >= maxBlocks) {
                break;
            }
        }
        
        LOGGER.info("Found {} convertible blocks", toConvert.size());
        
        // Convert all blocks
        int converted = 0;
        for (BlockPos pos : toConvert) {
            try {
                world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState(), 3);
                converted++;
                LOGGER.info("Converted {} to mycelium", pos);
            } catch (Exception e) {
                LOGGER.error("Failed to convert block at {}: {}", pos, e.getMessage());
            }
        }
        
        return converted;
    }
    
    private boolean canConvertToMycelium(BlockState state) {
        boolean canConvert = state.isOf(Blocks.GRASS_BLOCK) ||
               state.isOf(Blocks.DIRT) ||
               state.isOf(Blocks.SAND) ||
               state.isOf(Blocks.RED_SAND) ||
               state.isOf(Blocks.GRAVEL);
        
        return canConvert;
    }
    
    private void applyPoxiumBlightToKiller(LivingEntity killer) {
        // Get current Poxium Blight level on the killer
        StatusEffectInstance currentEffect = killer.getStatusEffect(ModEffects.POXIUM_BLIGHT);
        int currentAmplifier = currentEffect != null ? currentEffect.getAmplifier() : -1;
        
        // Add 5 levels (amplifier goes from current to current+5)
        int newAmplifier = Math.min(currentAmplifier + 5, 14); // Cap at level 15 (amplifier 14)
        int duration = 600; // 30 seconds
        
        StatusEffectInstance newEffect = new StatusEffectInstance(
            ModEffects.POXIUM_BLIGHT,
            duration,
            newAmplifier,
            false,
            true,
            true
        );
        
        killer.addStatusEffect(newEffect);
        LOGGER.info("Applied Poxium Blight level {} to killer", newAmplifier + 1);
    }
}
