package net.pozzums.poxium.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.pozzums.poxium.Poxium;
import net.pozzums.poxium.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    private static final TagKey<Block> POXIUM_CONVERTIBLE =
            TagKey.of(
                    RegistryKeys.BLOCK,
                    new Identifier(Poxium.MOD_ID, "mycelium_convertible")
            );

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void poxium$onPlayerDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ServerWorld world = player.getServerWorld();

        StatusEffectInstance blight =
                player.getStatusEffect(ModEffects.POXIUM_BLIGHT);
        if (blight == null) return;

        int level = blight.getAmplifier() + 1;
        int attempts = Math.min(20 + level * 5, 80);

        BlockPos origin = player.getBlockPos().down();

        // Run NEXT tick to avoid death cleanup issues
        world.getServer().execute(() ->
                spreadMycelium(world, origin, attempts)
        );
    }

    private static void spreadMycelium(ServerWorld world, BlockPos origin, int attempts) {
        Random random = world.getRandom();

        for (int i = 0; i < attempts; i++) {
            BlockPos pos = origin.add(
                    random.nextBetween(-4, 4),
                    random.nextBetween(-2, 2),
                    random.nextBetween(-4, 4)
            );

            BlockState state = world.getBlockState(pos);
            BlockState above = world.getBlockState(pos.up());

            if (!state.isIn(POXIUM_CONVERTIBLE)) continue;
            if (!above.isAir()) continue;

            world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState(), 3);
        }
    }
}
