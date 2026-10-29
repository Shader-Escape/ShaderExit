package ml.mypals.ShaderExit.compat;

import ml.mypals.exit.api.Exit;
import ml.mypals.exit.api.PortalOverrides;

public final class Bypass {
	private Bypass() {}

	public static void render(SupportedMod mod, Runnable original) {
		if (mod.enabled()) Exit.portal(original);
		else original.run();
	}

	public static void render(SupportedMod mod, PortalOverrides overrides, Runnable original) {
		if (mod.enabled()) Exit.portal(overrides, original);
		else original.run();
	}
}
