package ml.mypals.ShaderExit.compat;

import ml.mypals.exit.api.Exit;
import ml.mypals.exit.api.PortalOverrides;

public final class Bypass {
	private static final ThreadLocal<SupportedMod> CURRENT_MOD = new ThreadLocal<>();

	private Bypass() {}

	public static void render(SupportedMod mod, Runnable original) {
		render(mod, PortalOverrides.DEFAULT, original);
	}

	public static void render(SupportedMod mod, PortalOverrides overrides, Runnable original) {
		SupportedMod previous = CURRENT_MOD.get();
		CURRENT_MOD.set(mod);
		try {
			if (mod.enabled()) Exit.portal(overrides, original);
			else original.run();
		} finally {
			if (previous == null) CURRENT_MOD.remove();
			else CURRENT_MOD.set(previous);
		}
	}

	/** Shared vanilla helpers keep the calling mod's choice, even when its bypass is off. */
	public static SupportedMod currentModOr(SupportedMod fallback) {
		SupportedMod mod = CURRENT_MOD.get();
		return mod == null ? fallback : mod;
	}
}
