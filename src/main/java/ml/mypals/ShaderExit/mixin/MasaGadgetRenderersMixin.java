package ml.mypals.ShaderExit.mixin;

//? if <=26.2 {
/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import ml.mypals.ShaderExit.compat.Bypass;
import ml.mypals.ShaderExit.compat.SupportedMod;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.Coerce;
*///?}
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

/** MasaGadget uses MagicLib events rather than MaLiLib's world-render callbacks. */
@Pseudo
@Mixin(targets = {
	"com.plusls.MasaGadget.impl.feature.entityInfo.EntityInfoRenderer",
	"com.plusls.MasaGadget.impl.feature.entityTrace.EntityTraceRenderer"
})
public abstract class MasaGadgetRenderersMixin {
	//? if <=26.2 {
	/*@WrapMethod(method = "postRenderLevel(Lnet/minecraft/client/multiplayer/ClientLevel;Ltop/hendrixshen/magiclib/api/render/context/LevelRenderContext;)V", require = 0)
	private void shaderExit$bypassWorldOverlays(ClientLevel level, @Coerce Object context, Operation<Void> original) {
		Bypass.render(SupportedMod.MASAGADGET, () -> original.call(level, context));
	}

	// Older MasaGadget releases implement MagicLib's three-argument callback instead.
	@WrapMethod(method = "postRenderLevel(Lnet/minecraft/world/level/Level;Ltop/hendrixshen/magiclib/api/render/context/RenderContext;F)V", require = 0)
	private void shaderExit$bypassLegacyWorldOverlays(Level level, @Coerce Object context, float partialTick, Operation<Void> original) {
		Bypass.render(SupportedMod.MASAGADGET, () -> original.call(level, context, partialTick));
	}
	*///?}
}
