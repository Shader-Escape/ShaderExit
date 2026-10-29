package ml.mypals.ShaderExit.mixin;

//? if =26.2 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ml.mypals.ShaderExit.compat.Bypass;
import ml.mypals.ShaderExit.compat.SupportedMod;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "top.hendrixshen.magiclib.impl.render.text.ImmediateTextDrawer")
public abstract class MagicLibImmediateTextDrawerMixin {
	//? if =26.2 {
	/*// A shared text batch is submitted after MasaGadget's portal has closed. Give its text
	// a standalone batch so it is drawn inside the portal, without collecting other mods' text.
	@Coerce
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE",
		target = "Ltop/hendrixshen/magiclib/impl/render/text/TextRenderBatch;getActiveBatch()Ltop/hendrixshen/magiclib/impl/render/text/TextRenderBatch;"), require = 0)
	private Object shaderExit$separateMasaGadgetText(Operation<Object> original) {
		if (Bypass.currentModOr(null) == SupportedMod.MASAGADGET && SupportedMod.MASAGADGET.enabled()) return null;
		return original.call();
	}
	*///?}
}
