package ml.mypals.ShaderExit.compat;

import net.fabricmc.loader.api.FabricLoader;

/** Known Carpet builds whose deferred text/sprite rendering is incomplete. */
public final class CarpetCompat {
	public static final boolean NEEDS_DEFERRED_RENDER_FIX = FabricLoader.getInstance()
		.getModContainer("carpet").map(mod -> mod.getMetadata().getVersion().getFriendlyString())
		.map(version -> version.equals("26.2+v260616") || version.equals("26.3+v260915"))
		.orElse(false);

	private CarpetCompat() {}
}
