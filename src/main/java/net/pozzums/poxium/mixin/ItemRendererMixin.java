package net.pozzums.poxium.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.pozzums.poxium.Poxium;
import net.pozzums.poxium.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Mixin to use detailed 3D models for custom items when rendered in-world or in-hand.
 * GUI inventory shows the simple 2D icon, while 3D rendering uses the detailed model.
 */
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    
    /**
     * Replaces the item model with detailed versions for cleaver and greatsword.
     * This provides better visual appearance when items are held or dropped.
     * 
     * @param value The original BakedModel
     * @param stack The ItemStack being rendered
     * @param renderMode The rendering context (GUI, hand, ground, etc.)
     * @return The model to use (original or detailed version)
     */
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useCustomItemModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, 
                                         boolean leftHanded, MatrixStack matrices, 
                                         VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // Don't replace model in GUI - keep the simple 2D icon for inventory
        if (renderMode == ModelTransformationMode.GUI) {
            return value;
        }
        
        // Use detailed cleaver model when held/dropped
        if (stack.isOf(ModItems.POXIUM_SWORD)) {
            return ((ItemRendererAccessor) this).mccourse$getModels()
                    .getModelManager()
                    .getModel(new ModelIdentifier(Poxium.MOD_ID, "poxiumsword_detail", "inventory"));
        }

        return value;
    }
}
