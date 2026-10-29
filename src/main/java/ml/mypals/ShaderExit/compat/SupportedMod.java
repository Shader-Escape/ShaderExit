package ml.mypals.ShaderExit.compat;

import ml.mypals.ShaderExit.ShaderExit;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

/** Each choice belongs to a mod, even when several mods share MaLiLib's dispatcher. */
public enum SupportedMod {
	MINIHUD("minihud", "MiniHUD", "MH", 0xFF68C6A3, "fi.dy.masa.minihud."),
	TWEAKEROO("tweakeroo", "Tweakeroo", "TW", 0xFFE4AD61, "fi.dy.masa.tweakeroo."),
	LITEMATICA("litematica", "Litematica", "LM", 0xFF79B9EA, "fi.dy.masa.litematica."),
	TWEAKERMORE("tweakermore", "TweakerMore", "TM", 0xFFBD9BE8, "me.fallenbreath.tweakermore."),
	WORLDEDIT_CUI("worldeditcui", "WorldEdit CUI", "WE", 0xFFEBBC5D, "org.enginehub.worldeditcui."),
	CARPET("carpet", "Carpet", "CP", 0xFFD77870, "carpet."),
	MALILIB("malilib", "MaLiLib", "ML", 0xFF9CC68A, "fi.dy.masa.malilib."),
	MINECRAFT("minecraft", "Minecraft", "MC", 0xFF8BBC76, "net.minecraft.");

	private final String id;
	private final String name;
	private final String initials;
	private final int color;
	private final String rendererPackage;

	SupportedMod(String id, String name, String initials, int color, String rendererPackage) {
		this.id = id;
		this.name = name;
		this.initials = initials;
		this.color = color;
		this.rendererPackage = rendererPackage;
	}

	public String id() { return this.id; }
	public String initials() { return this.initials; }
	public int color() { return this.color; }
	public Component displayName() { return Component.literal(this.name); }
	public Component description() { return Component.translatable("shader_exit.mod." + this.id); }
	public boolean installed() { return FabricLoader.getInstance().isModLoaded(this.id); }
	public boolean enabled() { return ShaderExit.CONFIG.isEnabled(this.id); }

	public static SupportedMod forRenderer(Object renderer) {
		return forRendererClass(renderer.getClass().getName());
	}

	public static SupportedMod forRendererClass(String className) {
		for (SupportedMod mod : values()) {
			if (className.startsWith(mod.rendererPackage)) return mod;
		}
		// Unrecognised MaLiLib clients retain the old bypass behavior under MaLiLib's own choice.
		return MALILIB;
	}
}
