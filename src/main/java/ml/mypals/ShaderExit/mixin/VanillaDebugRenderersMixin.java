package ml.mypals.ShaderExit.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ml.mypals.exit.api.Exit;
import ml.mypals.ShaderExit.compat.Bypass;
import ml.mypals.ShaderExit.compat.SupportedMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


//? if >=26.2 {
@Mixin(targets = "net/minecraft/client/renderer/LevelRenderer")
//?} else if >=1.21.11 {
/*@Mixin(targets = "net/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives")
*///?} else {
/*@Mixin(targets = "net/minecraft/client/renderer/debug/DebugRenderer")
*///?}
public abstract class VanillaDebugRenderersMixin {
	//? if >=26.2 {
	@WrapOperation(method = "submitFeatures", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/renderer/gizmos/DrawableGizmoPrimitives;submit(Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Z)V"))
	private void shaderExit$bypassDebugGizmos(
		net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives primitives,
		net.minecraft.client.renderer.SubmitNodeCollector collector,
		net.minecraft.client.renderer.state.level.CameraRenderState cameraState,
		boolean onTop,
		Operation<Void> original
	) {
		original.call(primitives, Bypass.currentModOr(SupportedMod.MINECRAFT).enabled() ? Exit.batch(collector) : collector, cameraState, onTop);
	}
	//?} else if >=1.21.11 {
	/*@WrapMethod(method = "render")
	private void shaderExit$bypassDebugGizmos(
		com.mojang.blaze3d.vertex.PoseStack poseStack,
		net.minecraft.client.renderer.MultiBufferSource bufferSource,
		//? if >=26.1.2 {
		net.minecraft.client.renderer.state.level.CameraRenderState cameraState,
		org.joml.Matrix4fc modelView,
		//?} else {
		net.minecraft.client.renderer.state.CameraRenderState cameraState,
		org.joml.Matrix4f modelView,
		//?}
		Operation<Void> original
	) {
		Bypass.render(Bypass.currentModOr(SupportedMod.MINECRAFT), () -> original.call(poseStack, bufferSource, cameraState, modelView));
	}
	*///?} else if >=1.21.10 {
	/*@WrapMethod(method = "render")
	private void shaderExit$bypassDebugGizmos(
		com.mojang.blaze3d.vertex.PoseStack poseStack,
		net.minecraft.client.renderer.culling.Frustum frustum,
		net.minecraft.client.renderer.MultiBufferSource.BufferSource bufferSource,
		double camX,
		double camY,
		double camZ,
		boolean clearBuffers,
		Operation<Void> original
	) {
		Bypass.render(Bypass.currentModOr(SupportedMod.MINECRAFT), () -> original.call(poseStack, frustum, bufferSource, camX, camY, camZ, clearBuffers));
	}
	*///?} else if >=1.21.3 {
	/*@WrapMethod(method = "render")
	private void shaderExit$bypassDebugGizmos(
		com.mojang.blaze3d.vertex.PoseStack poseStack,
		net.minecraft.client.renderer.culling.Frustum frustum,
		net.minecraft.client.renderer.MultiBufferSource.BufferSource bufferSource,
		double camX,
		double camY,
		double camZ,
		Operation<Void> original
	) {
		Bypass.render(Bypass.currentModOr(SupportedMod.MINECRAFT), () -> original.call(poseStack, frustum, bufferSource, camX, camY, camZ));
	}
	*///?} else {
	/*@WrapMethod(method = "render")
	private void shaderExit$bypassDebugGizmos(
		com.mojang.blaze3d.vertex.PoseStack poseStack,
		net.minecraft.client.renderer.MultiBufferSource.BufferSource bufferSource,
		double camX,
		double camY,
		double camZ,
		Operation<Void> original
	) {
		Bypass.render(Bypass.currentModOr(SupportedMod.MINECRAFT), () -> original.call(poseStack, bufferSource, camX, camY, camZ));
	}
	*///?}
}
