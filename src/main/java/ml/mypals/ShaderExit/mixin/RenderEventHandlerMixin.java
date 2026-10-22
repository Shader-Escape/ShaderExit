package ml.mypals.ShaderExit.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ml.mypals.exit.api.Exit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;


@Pseudo
@Mixin(targets = "fi.dy.masa.malilib.event.RenderEventHandler")
public abstract class RenderEventHandlerMixin {
	//? if >=26.3 {
	@WrapMethod(method = "runRenderWorldLast")
	private void shaderExit$bypassShaderPack(
		net.minecraft.client.Minecraft mc,
		net.minecraft.client.renderer.LevelRenderer vanilla,
		com.mojang.blaze3d.framegraph.FrameGraphBuilder frameGraphBuilder,
		net.minecraft.client.renderer.feature.FeatureRenderDispatcher.PreparedFrame preparedFrame,
		net.minecraft.client.renderer.LevelTargetBundle targets,
		net.minecraft.client.renderer.culling.Frustum cullFrustum,
		net.minecraft.client.renderer.state.level.CameraRenderState cameraState,
		net.minecraft.client.renderer.RenderBuffers buffers,
		boolean consistentDepthRequired,
		com.mojang.renderpearl.api.buffers.GpuBufferSlice terrainFog,
		org.joml.Vector4f fogColor,
		net.minecraft.util.profiling.ProfilerFiller profiler,
		Operation<Void> original
	) {
		Exit.portal(() -> original.call(
			mc, vanilla, frameGraphBuilder, preparedFrame, targets, cullFrustum, cameraState, buffers,
			consistentDepthRequired, terrainFog, fogColor, profiler
		));
	}
	//?} else if >=1.21.3 {
	/*@WrapOperation(method = "runRenderWorldLast", at = @At(value = "INVOKE",
		target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"))
	private void shaderExit$bypassShaderPack(
		com.mojang.blaze3d.framegraph.FramePass pass,
		Runnable task,
		Operation<Void> original
	) {
		original.call(pass, (Runnable) () -> Exit.portal(task));
	}
	*///?} else {
	/*@WrapMethod(method = "onRenderWorldLast")
	private void shaderExit$bypassShaderPack(
		org.joml.Matrix4f posMatrix,
		org.joml.Matrix4f projMatrix,
		net.minecraft.client.Minecraft mc,
		Operation<Void> original
	) {
		Exit.portal(() -> original.call(posMatrix, projMatrix, mc));
	}
	*///?}
}
