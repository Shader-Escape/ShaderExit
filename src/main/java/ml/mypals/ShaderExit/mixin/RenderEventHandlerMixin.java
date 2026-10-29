package ml.mypals.ShaderExit.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.malilib.interfaces.IRenderer;
import ml.mypals.ShaderExit.compat.Bypass;
import ml.mypals.ShaderExit.compat.SupportedMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/** Wrap the individual mod callback, including callbacks inside deferred framegraph tasks. */
@Pseudo
@Mixin(targets = "fi.dy.masa.malilib.event.RenderEventHandler")
public abstract class RenderEventHandlerMixin {
	//? if >=26.3 {
	@WrapOperation(method = "*", at = @At(value = "INVOKE",
		target = "Lfi/dy/masa/malilib/interfaces/IRenderer;onRenderWorldLast(Lcom/mojang/blaze3d/pipeline/RenderTarget;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/renderer/RenderBuffers;Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Lnet/minecraft/util/profiling/ProfilerFiller;)V"))
	private void shaderExit$bypassModRenderer(
		IRenderer renderer,
		com.mojang.blaze3d.pipeline.RenderTarget target,
		net.minecraft.client.renderer.state.level.CameraRenderState camera,
		net.minecraft.client.renderer.culling.Frustum frustum,
		net.minecraft.client.renderer.RenderBuffers buffers,
		com.mojang.renderpearl.api.buffers.GpuBufferSlice fog,
		org.joml.Vector4f fogColor,
		net.minecraft.util.profiling.ProfilerFiller profiler,
		Operation<Void> original
	) {
		Bypass.render(SupportedMod.forRenderer(renderer), () -> original.call(renderer, target, camera, frustum, buffers, fog, fogColor, profiler));
	}
	//? } else if >=26.1.2 {
	/*@WrapOperation(method = "*", at = @At(value = "INVOKE",
		target = "Lfi/dy/masa/malilib/interfaces/IRenderer;onRenderWorldLast(Lcom/mojang/blaze3d/pipeline/RenderTarget;Lorg/joml/Matrix4fc;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/renderer/RenderBuffers;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Lnet/minecraft/util/profiling/ProfilerFiller;)V"))
	private void shaderExit$bypassModRenderer(
		IRenderer renderer,
		com.mojang.blaze3d.pipeline.RenderTarget target,
		org.joml.Matrix4fc modelView,
		net.minecraft.client.renderer.state.level.CameraRenderState camera,
		net.minecraft.client.renderer.culling.Frustum frustum,
		net.minecraft.client.renderer.RenderBuffers buffers,
		com.mojang.blaze3d.buffers.GpuBufferSlice fog,
		org.joml.Vector4f fogColor,
		net.minecraft.util.profiling.ProfilerFiller profiler,
		Operation<Void> original
	) {
		Bypass.render(SupportedMod.forRenderer(renderer), () -> original.call(renderer, target, modelView, camera, frustum, buffers, fog, fogColor, profiler));
	}
	*///? } else if >=1.21.8 {
	/*@WrapOperation(method = "*", at = @At(value = "INVOKE",
		target = "Lfi/dy/masa/malilib/interfaces/IRenderer;onRenderWorldLastAdvanced(Lcom/mojang/blaze3d/pipeline/RenderTarget;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/RenderBuffers;Lnet/minecraft/util/profiling/ProfilerFiller;)V"))
	private void shaderExit$bypassModRenderer(
		IRenderer renderer,
		com.mojang.blaze3d.pipeline.RenderTarget target,
		org.joml.Matrix4f pose,
		org.joml.Matrix4f projection,
		net.minecraft.client.renderer.culling.Frustum frustum,
		net.minecraft.client.Camera camera,
		net.minecraft.client.renderer.RenderBuffers buffers,
		net.minecraft.util.profiling.ProfilerFiller profiler,
		Operation<Void> original
	) {
		Bypass.render(SupportedMod.forRenderer(renderer), () -> original.call(renderer, target, pose, projection, frustum, camera, buffers, profiler));
	}
	*///? } else if >=1.21.5 {
	/*@WrapOperation(method = "*", at = @At(value = "INVOKE",
		target = "Lfi/dy/masa/malilib/interfaces/IRenderer;onRenderWorldLastAdvanced(Lcom/mojang/blaze3d/pipeline/RenderTarget;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogParameters;Lnet/minecraft/client/renderer/RenderBuffers;Lnet/minecraft/util/profiling/ProfilerFiller;)V"))
	private void shaderExit$bypassModRenderer(
		IRenderer renderer,
		com.mojang.blaze3d.pipeline.RenderTarget target,
		org.joml.Matrix4f pose,
		org.joml.Matrix4f projection,
		net.minecraft.client.renderer.culling.Frustum frustum,
		net.minecraft.client.Camera camera,
		net.minecraft.client.renderer.FogParameters fog,
		net.minecraft.client.renderer.RenderBuffers buffers,
		net.minecraft.util.profiling.ProfilerFiller profiler,
		Operation<Void> original
	) {
		Bypass.render(SupportedMod.forRenderer(renderer), () -> original.call(renderer, target, pose, projection, frustum, camera, fog, buffers, profiler));
	}
	*///? } else if >=1.21.3 {
	/*@WrapOperation(method = "*", at = @At(value = "INVOKE",
		target = "Lfi/dy/masa/malilib/interfaces/IRenderer;onRenderWorldLastAdvanced(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogParameters;Lnet/minecraft/util/profiling/ProfilerFiller;)V"))
	private void shaderExit$bypassModRenderer(
		IRenderer renderer,
		org.joml.Matrix4f pose,
		org.joml.Matrix4f projection,
		net.minecraft.client.renderer.culling.Frustum frustum,
		net.minecraft.client.Camera camera,
		net.minecraft.client.renderer.FogParameters fog,
		net.minecraft.util.profiling.ProfilerFiller profiler,
		Operation<Void> original
	) {
		Bypass.render(SupportedMod.forRenderer(renderer), () -> original.call(renderer, pose, projection, frustum, camera, fog, profiler));
	}
	*///?}

	//? if <=1.21.11 {
	/*@WrapOperation(method = "*", at = @At(value = "INVOKE",
		target = "Lfi/dy/masa/malilib/interfaces/IRenderer;onRenderWorldLast(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"))
	private void shaderExit$bypassBasicModRenderer(IRenderer renderer, org.joml.Matrix4f pose,
		org.joml.Matrix4f projection, Operation<Void> original) {
		Bypass.render(SupportedMod.forRenderer(renderer), () -> original.call(renderer, pose, projection));
	}
	*///?}
}
