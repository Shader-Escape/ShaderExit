package ml.mypals.ShaderExit.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import ml.mypals.ShaderExit.compat.Bypass;
import ml.mypals.ShaderExit.compat.SupportedMod;
import ml.mypals.exit.api.PortalOverrides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;


@Pseudo
@Mixin(targets = "me.fallenbreath.tweakermore.event.TweakerMoreRenderEvents")
public abstract class TweakerMoreRenderEventsMixin {
	@WrapMethod(method = "dispatchRenderWorldPostEvent")
	private static void shaderExit$bypassShaderPack(
		net.minecraft.client.Minecraft mc,
		org.joml.Matrix4fStack matrixStack,
		Operation<Void> original
	) {
		Bypass.render(SupportedMod.TWEAKERMORE,
			PortalOverrides.DEFAULT.withRenderingLevel(PortalOverrides.Switch.OFF),
			() -> original.call(mc, matrixStack)
		);
	}
}
