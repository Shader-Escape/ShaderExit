package ml.mypals.ShaderExit;

import net.fabricmc.api.ClientModInitializer;
import ml.mypals.ShaderExit.config.BypassConfig;
import ml.mypals.ShaderExit.gui.BypassScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShaderExit implements ClientModInitializer {
	public static final String MOD_ID = "shader_exit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final BypassConfig CONFIG = new BypassConfig(FabricLoader.getInstance().getConfigDir().resolve("shader_exit.json"));
	private static boolean openConfig;

	@Override
	public void onInitializeClient() {
		CONFIG.load();
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
			dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("shaderexit").executes(context -> {
				openConfig = true;
				return 1;
			})));
		// Opening in the next tick lets the chat screen finish handling its command first.
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openConfig) {
				openConfig = false;
				//? if >=26.1.2 {
				client.setScreenAndShow(new BypassScreen(null));
				//?} else {
				/*client.setScreen(new BypassScreen(null));
				*///?}
			}
		});
	}

}
