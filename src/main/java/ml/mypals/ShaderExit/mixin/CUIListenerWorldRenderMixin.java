package ml.mypals.ShaderExit.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import ml.mypals.exit.api.Exit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "org.enginehub.worldeditcui.event.listeners.CUIListenerWorldRender")
public abstract class CUIListenerWorldRenderMixin {
	//? if >=26.3 {
	@WrapMethod(method = "onRender")
	private void shaderExit$bypassShaderPack(
		float partialTicks,
		net.minecraft.client.renderer.SubmitNodeCollector collector,
		com.mojang.blaze3d.vertex.PoseStack poseStack,
		Operation<Void> original
	) {
		original.call(partialTicks, Exit.batch(collector), poseStack);
	}
	//?} else {
	/*@WrapMethod(method = "onRender")
	private void shaderExit$bypassShaderPack(float partialTicks, Operation<Void> original) {
		Exit.portal(() -> original.call(partialTicks));
	}
	*///?}
}
