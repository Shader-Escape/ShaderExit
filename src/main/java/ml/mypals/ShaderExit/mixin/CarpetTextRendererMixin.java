package ml.mypals.ShaderExit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
//? if >=26.2 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import ml.mypals.ShaderExit.compat.CarpetCompat;
import ml.mypals.ShaderExit.compat.SupportedMod;
import ml.mypals.exit.api.Exit;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.injection.At;
//?}

@Pseudo
@Mixin(targets = "carpet.script.utils.ShapesRenderer$RenderedText")
public abstract class CarpetTextRendererMixin {
	//? if >=26.2 {
	// These releases prepare their label but never submit it. Restrict the workaround
	// to the affected builds so a later Carpet fix cannot draw the same text twice.
	@WrapOperation(method = "renderLines", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/gui/Font;prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;"), require = 0)
	private Font.PreparedText shaderExit$submitLabel(Font font, FormattedCharSequence text,
		float x, float y, int color, boolean shadow, boolean shadowOnly, int background,
		Operation<Font.PreparedText> original, @Local(argsOnly = true) PoseStack pose,
		@Local(argsOnly = true) SubmitNodeStorage storage) {
		Font.PreparedText prepared = original.call(font, text, x, y, color, shadow, shadowOnly, background);
		if (CarpetCompat.NEEDS_DEFERRED_RENDER_FIX && SupportedMod.CARPET.enabled()) {
			Exit.batch(storage).submitText(pose, x, y, text, shadow, Font.DisplayMode.NORMAL,
				0xF000F0, color, background, 0);
		}
		return prepared;
	}
	//?}
}
