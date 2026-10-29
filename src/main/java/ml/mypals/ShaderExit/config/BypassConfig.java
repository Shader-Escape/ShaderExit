package ml.mypals.ShaderExit.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/** Per-mod choices. Missing entries default to the original, enabled behavior. */
public final class BypassConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger("shader_exit");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final Path path;
	private Map<String, Boolean> enabled = Map.of();

	public BypassConfig(Path path) {
		this.path = path;
	}

	public void load() {
		if (!Files.exists(this.path)) return;
		try (Reader reader = Files.newBufferedReader(this.path)) {
			JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
			Map<String, Boolean> loaded = new HashMap<>();
			JsonObject mods = root.getAsJsonObject("bypass");
			if (mods != null) {
				for (Map.Entry<String, JsonElement> entry : mods.entrySet()) {
					JsonElement value = entry.getValue();
					if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isBoolean()) {
						loaded.put(entry.getKey(), value.getAsBoolean());
					}
				}
			}
			this.enabled = Map.copyOf(loaded);
		} catch (IOException | RuntimeException e) {
			LOGGER.error("Could not read {}; keeping current bypass settings", this.path, e);
		}
	}

	public boolean isEnabled(String modId) {
		return this.enabled.getOrDefault(modId, true);
	}

	/** Publish a change only after it has been safely saved. */
	public boolean setEnabled(String modId, boolean value) {
		Map<String, Boolean> updated = new HashMap<>(this.enabled);
		updated.put(modId, value);
		JsonObject mods = new JsonObject();
		updated.entrySet().stream().sorted(Map.Entry.comparingByKey())
			.forEach(entry -> mods.addProperty(entry.getKey(), entry.getValue()));
		JsonObject root = new JsonObject();
		root.add("bypass", mods);
		Path temporary = null;
		try {
			Files.createDirectories(this.path.toAbsolutePath().getParent());
			temporary = Files.createTempFile(this.path.toAbsolutePath().getParent(), "shader_exit-", ".tmp");
			Files.writeString(temporary, GSON.toJson(root) + "\n");
			try {
				Files.move(temporary, this.path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
			} catch (AtomicMoveNotSupportedException e) {
				Files.move(temporary, this.path, StandardCopyOption.REPLACE_EXISTING);
			}
			this.enabled = Map.copyOf(updated);
			return true;
		} catch (IOException | RuntimeException e) {
			LOGGER.error("Could not save {}", this.path, e);
			return false;
		} finally {
			if (temporary != null) {
				try {
					Files.deleteIfExists(temporary);
				} catch (IOException e) {
					LOGGER.warn("Could not remove temporary config {}", temporary, e);
				}
			}
		}
	}
}
