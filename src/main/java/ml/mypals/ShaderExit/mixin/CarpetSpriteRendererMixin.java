package ml.mypals.ShaderExit.mixin;

import ml.mypals.ShaderExit.compat.SupportedMod;
import ml.mypals.exit.api.Exit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
//? if >=26.2 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import ml.mypals.ShaderExit.compat.CarpetCompat;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//?}

/** Item and block shapes submit directly, separately from Carpet's gizmo primitives. */
@Pseudo
@Mixin(targets = "carpet.script.utils.ShapesRenderer$RenderedSprite")
public abstract class CarpetSpriteRendererMixin {
	//? if >=26.2 {
	// Deferred submissions already apply the camera view. Carpet's old inverse
	// camera rotation rotates the relative position before it reaches that view.
	@WrapOperation(method = "renderLines", at = @At(value = "INVOKE",
		//? if >=26.3 {
		target = "Lcom/mojang/blaze3d/vertex/PoseStack;rotate(Lorg/joml/Quaternionfc;)V", ordinal = 0), require = 0)
		//?} else {
		/*target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 0), require = 0)
		*///?}
	private void shaderExit$keepSpriteInWorldSpace(PoseStack pose, Quaternionfc rotation, Operation<Void> original) {
		if (!CarpetCompat.NEEDS_DEFERRED_RENDER_FIX || !SupportedMod.CARPET.enabled()) {
			original.call(pose, rotation);
		}
	}

	@ModifyArg(method = "renderLines", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/block/BlockModelRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"), index = 1)
	private SubmitNodeCollector shaderExit$collectBlock(SubmitNodeCollector collector) {
		return SupportedMod.CARPET.enabled() ? Exit.batch(collector) : collector;
	}

	@ModifyArg(method = "renderLines", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;submit(Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"), index = 2)
	private SubmitNodeCollector shaderExit$collectBlockEntity(SubmitNodeCollector collector) {
		return SupportedMod.CARPET.enabled() ? Exit.batch(collector) : collector;
	}

	@ModifyArg(method = "renderLines", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"), index = 1)
	private SubmitNodeCollector shaderExit$collectItem(SubmitNodeCollector collector) {
		return SupportedMod.CARPET.enabled() ? Exit.batch(collector) : collector;
	}
	//?}
}
