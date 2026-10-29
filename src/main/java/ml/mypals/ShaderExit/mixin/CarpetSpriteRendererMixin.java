package ml.mypals.ShaderExit.mixin;

import ml.mypals.ShaderExit.compat.SupportedMod;
import ml.mypals.exit.api.Exit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
//? if >=26.2 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//?}

/** Item and block shapes submit directly, separately from Carpet's gizmo primitives. */
@Pseudo
@Mixin(targets = "carpet.script.utils.ShapesRenderer$RenderedSprite")
public abstract class CarpetSpriteRendererMixin {
	//? if >=26.2 {
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
