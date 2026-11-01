package ml.mypals.ShaderExit.gui;

import com.mojang.blaze3d.platform.NativeImage;
import ml.mypals.ShaderExit.ShaderExit;
import ml.mypals.ShaderExit.compat.SupportedMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}

import java.io.InputStream;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.Map;

/** Screen-owned textures; read the mod's declared icon rather than assuming its assets path. */
final class ModIcons implements AutoCloseable {
	//? if >=1.21.11 {
	private final Map<SupportedMod, Identifier> icons = new EnumMap<>(SupportedMod.class);
//?} else {
	/*private final Map<SupportedMod, ResourceLocation> icons = new EnumMap<>(SupportedMod.class);
	*///?}

	ModIcons() {
		for (SupportedMod mod : SupportedMod.values()) {
			FabricLoader.getInstance().getModContainer(mod.id()).ifPresent(container ->
				container.getMetadata().getIconPath(64).flatMap(container::findPath).ifPresent(path -> {
					try (InputStream stream = Files.newInputStream(path)) {
						NativeImage image = NativeImage.read(stream);
						//? if >=1.21.11 {
						var id = Identifier.fromNamespaceAndPath(ShaderExit.MOD_ID, "mod_icons/" + mod.id());
						//?} else {
						/*var id = ResourceLocation.fromNamespaceAndPath(ShaderExit.MOD_ID, "mod_icons/" + mod.id());
						*///?}
						//? if >=1.21.5 {
						DynamicTexture texture = new DynamicTexture(() -> "ShaderExit icon: " + mod.id(), image);
						//?} else {
						/*DynamicTexture texture = new DynamicTexture(image);
						*///?}
						Minecraft.getInstance().getTextureManager().register(id, texture);
						this.icons.put(mod, id);
					} catch (Exception e) {
						ShaderExit.LOGGER.warn("Could not load {} icon; using its initials", mod.id(), e);
					}
				}));
		}
	}

	//? if >=1.21.11 {
	Identifier get(SupportedMod mod) {
	//?} else {
	/*ResourceLocation get(SupportedMod mod) {
	*///?}
		return this.icons.get(mod);
	}

	@Override
	public void close() {
		this.icons.values().forEach(Minecraft.getInstance().getTextureManager()::release);
		this.icons.clear();
	}
}
