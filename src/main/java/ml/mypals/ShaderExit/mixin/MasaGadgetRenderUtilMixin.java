package ml.mypals.ShaderExit.mixin;

//? if >=1.21.11 && <=26.2 {
/*import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import fi.dy.masa.malilib.render.RenderContext;
import ml.mypals.ShaderExit.compat.Bypass;
import ml.mypals.ShaderExit.compat.SupportedMod;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "com.plusls.MasaGadget.util.RenderUtil")
public abstract class MasaGadgetRenderUtilMixin {
	//? if >=1.21.11 && <=26.2 {
	/*// MasaGadget 4.0.403 writes line vertices on these versions but never submits or closes
	// their context. Complete that draw inside the portal; already submitted contexts are left alone.
	@Inject(method = "drawLine", at = @At("RETURN"))
	private static void shaderExit$submitTracerLine(CallbackInfo ci, @Local RenderContext context,
		@Local BufferBuilder builder) throws Exception {
		if (Bypass.currentModOr(null) != SupportedMod.MASAGADGET || !SupportedMod.MASAGADGET.enabled()
			|| !context.isStarted() || context.isUploaded()) return;
		try (MeshData mesh = builder.build()) {
			if (mesh != null) context.draw(mesh, false, true);
		} finally {
			context.close();
		}
	}
	*///?}
}
