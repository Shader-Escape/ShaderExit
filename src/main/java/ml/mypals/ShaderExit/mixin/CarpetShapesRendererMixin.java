package ml.mypals.ShaderExit.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ml.mypals.ShaderExit.compat.Bypass;
import ml.mypals.ShaderExit.compat.SupportedMod;
import ml.mypals.exit.api.Exit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/** Carpet also draws the shapes received from TIS Addition, through this same renderer. */
@Pseudo
@Mixin(targets = "carpet.script.utils.ShapesRenderer")
public abstract class CarpetShapesRendererMixin {
	//? if >=26.2 {
	@WrapMethod(method = "render")
	private void shaderExit$renderShapes(net.minecraft.client.renderer.SubmitNodeStorage storage,
		net.minecraft.client.renderer.state.level.LevelRenderState state, org.joml.Matrix4fc modelView,
		float partialTick, Operation<Void> original) {
		Bypass.render(SupportedMod.CARPET, () -> original.call(storage, state, modelView, partialTick));
	}

	@WrapOperation(method = "render", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives;submit(Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Z)V"))
	private void shaderExit$collectShapes(net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives primitives,
		net.minecraft.client.renderer.SubmitNodeCollector collector,
		net.minecraft.client.renderer.state.level.CameraRenderState camera,
		boolean onTop, Operation<Void> original) {
		original.call(primitives, SupportedMod.CARPET.enabled() ? Exit.batch(collector) : collector, camera, onTop);
	}
	//?} else if >=1.21.11 {
	/*@WrapMethod(method = "render")
	private void shaderExit$renderShapes(net.minecraft.client.renderer.RenderBuffers buffers,
		//? if >=26.1.2 {
		net.minecraft.client.renderer.state.level.LevelRenderState state, org.joml.Matrix4fc modelView,
		//?} else {
		net.minecraft.client.renderer.state.LevelRenderState state, org.joml.Matrix4f modelView,
		//?}
		float partialTick, Operation<Void> original) {
		Bypass.render(SupportedMod.CARPET, () -> original.call(buffers, state, modelView, partialTick));
	}
	*///?} else if >=1.21.10 {
	/*@WrapMethod(method = "render")
	private void shaderExit$renderShapes(org.joml.Matrix4f modelView,
		net.minecraft.client.renderer.state.LevelRenderState state, float partialTick, Operation<Void> original) {
		Bypass.render(SupportedMod.CARPET, () -> original.call(modelView, state, partialTick));
	}
	*///?} else {
	/*@WrapMethod(method = "render")
	private void shaderExit$renderShapes(org.joml.Matrix4f modelView,
		net.minecraft.client.Camera camera, float partialTick, Operation<Void> original) {
		Bypass.render(SupportedMod.CARPET, () -> original.call(modelView, camera, partialTick));
	}
	*///?}
}
