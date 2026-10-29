package ml.mypals.ShaderExit.gui;

import ml.mypals.ShaderExit.ShaderExit;
import ml.mypals.ShaderExit.compat.SupportedMod;
//? if >=26.1.2 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import java.util.ArrayList;
import java.util.List;
//? if >=1.21.3 && <1.21.8 {
/*import net.minecraft.client.renderer.RenderType;
*///?}

/** A compact mod list. Pagination keeps every toggle accessible even at large GUI scales. */
public final class BypassScreen extends Screen {
	private final Screen parent;
	private final List<List<FormattedCharSequence>> descriptions = new ArrayList<>();
	private ModIcons icons;
	private int rowHeight;
	private int page;
	private int pageSize;
	private int listWidth;
	private int left;
	private boolean saveFailed;

	public BypassScreen(Screen parent) {
		super(Component.translatable("shader_exit.screen.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		if (this.icons == null) this.icons = new ModIcons();
		this.listWidth = Math.min(520, this.width - 24);
		this.left = (this.width - this.listWidth) / 2;
		SupportedMod[] mods = SupportedMod.values();
		this.descriptions.clear();
		this.rowHeight = 60;
		for (SupportedMod mod : mods) {
			List<FormattedCharSequence> lines = this.font.split(mod.description(), Math.max(1, this.listWidth - 64));
			this.descriptions.add(lines);
			this.rowHeight = Math.max(this.rowHeight, 38 + lines.size() * this.font.lineHeight);
		}
		this.pageSize = Math.max(1, (this.height - 116) / this.rowHeight);
		this.page = Math.min(this.page, this.lastPage());
		for (int row = 0; row < this.pageSize; row++) {
			int index = this.page * this.pageSize + row;
			if (index >= mods.length) break;
			SupportedMod mod = mods[index];
			Button toggle = Button.builder(this.toggleLabel(mod), button -> {
				this.saveFailed = !ShaderExit.CONFIG.setEnabled(mod.id(), !mod.enabled());
				button.setMessage(this.toggleLabel(mod));
			}).bounds(this.left + this.listWidth - 108, 56 + row * this.rowHeight + 6, 100, 20)
				.tooltip(Tooltip.create(Component.empty().append(mod.displayName()).append("\n")
					.append(mod.description()).append("\n").append(Component.translatable("shader_exit.screen.toggle_hint"))))
				.createNarration(supplier -> Component.empty().append(mod.displayName()).append(": ")
					.append(this.toggleLabel(mod)).append(". ").append(mod.description()))
				.build();
			toggle.active = mod.installed();
			this.addRenderableWidget(toggle);
		}
		int bottom = this.height - 28;
		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
			.bounds(this.width / 2 - 50, bottom, 100, 20).build());
		if (this.lastPage() > 0) {
			Button previous = this.addRenderableWidget(Button.builder(Component.literal("<"), button -> this.changePage(-1))
				.bounds(this.left, bottom, 28, 20).tooltip(Tooltip.create(Component.translatable("shader_exit.screen.previous"))).build());
			previous.active = this.page > 0;
			Button next = this.addRenderableWidget(Button.builder(Component.literal(">"), button -> this.changePage(1))
				.bounds(this.left + this.listWidth - 28, bottom, 28, 20).tooltip(Tooltip.create(Component.translatable("shader_exit.screen.next"))).build());
			next.active = this.page < this.lastPage();
		}
	}

	private Component toggleLabel(SupportedMod mod) {
		if (!mod.installed()) return Component.translatable("shader_exit.screen.not_installed");
		return Component.translatable(mod.enabled() ? "shader_exit.screen.enabled" : "shader_exit.screen.disabled");
	}

	private int lastPage() {
		return (SupportedMod.values().length - 1) / this.pageSize;
	}

	private void changePage(int direction) {
		int next = Math.clamp(this.page + direction, 0, this.lastPage());
		if (next != this.page) {
			this.page = next;
			this.rebuildWidgets();
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
		if (vertical != 0 && mouseY >= 54 && mouseY < this.height - 58) {
			this.changePage(vertical > 0 ? -1 : 1);
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, horizontal, vertical);
	}

	@Override
	//? if >=26.1.2 {
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
	//?} else {
	/*public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
	*///?}
		//? if >=26.1.2 {
		super.extractBackground(graphics, mouseX, mouseY, delta);
		//?} else {
		/*super.renderBackground(graphics, mouseX, mouseY, delta);
		*///?}
		graphics.fill(this.left - 4, 8, this.left + this.listWidth + 4, this.height - 36, 0xC0101823);
		this.centered(graphics, this.title, this.width / 2, 17, 0xFFFFFFFF);
		this.centered(graphics, Component.translatable("shader_exit.screen.subtitle"), this.width / 2, 34, 0xFFB8C9DB);
		SupportedMod[] mods = SupportedMod.values();
		for (int row = 0; row < this.pageSize; row++) {
			int index = this.page * this.pageSize + row;
			if (index >= mods.length) break;
			SupportedMod mod = mods[index];
			int y = 56 + row * this.rowHeight;
			boolean installed = mod.installed();
			graphics.fill(this.left, y, this.left + this.listWidth, y + this.rowHeight - 4, installed ? 0xD0273646 : 0x90202932);
			graphics.fill(this.left, y, this.left + 2, y + this.rowHeight - 4, installed ? mod.color() : 0xFF434B55);
			int iconY = y + (this.rowHeight - 36) / 2;
			var icon = this.icons.get(mod);
			if (icon != null) {
				//? if >=1.21.8 {
				graphics.blit(icon, this.left + 8, iconY, this.left + 40, iconY + 32, 0, 1, 0, 1);
				//?} else if >=1.21.3 {
				/*graphics.blit(RenderType::guiTextured, icon, this.left + 8, iconY, 0, 0, 32, 32, 32, 32);
				*///?} else {
				/*graphics.blit(icon, this.left + 8, iconY, 0, 0, 32, 32, 32, 32);
				*///?}
			} else {
				graphics.fill(this.left + 8, iconY, this.left + 40, iconY + 32, installed ? mod.color() : 0xFF343D47);
				this.centered(graphics, Component.literal(mod.initials()), this.left + 24, iconY + 12, installed ? 0xFF15202B : 0xFF67727E);
			}
			this.text(graphics, mod.displayName(), this.left + 48, y + 7, installed ? 0xFFF2F6FC : 0xFF7C8795);
			Component status = Component.translatable(installed ? "shader_exit.screen.installed" : "shader_exit.screen.not_installed");
			this.text(graphics, status, this.left + 48, y + 20, installed ? 0xFF8DD9AD : 0xFF626D79);
			int descriptionY = y + 34;
			for (FormattedCharSequence line : this.descriptions.get(index)) {
				this.text(graphics, line, this.left + 48, descriptionY, installed ? 0xFFB8C9DB : 0xFF929DAA);
				descriptionY += this.font.lineHeight;
			}
		}
		Component footer = this.saveFailed ? Component.translatable("shader_exit.screen.save_failed")
			: this.lastPage() > 0 ? Component.translatable("shader_exit.screen.page", this.page + 1, this.lastPage() + 1)
			: Component.translatable("shader_exit.screen.saved_hint");
		this.centered(graphics, footer, this.width / 2, this.height - 49, this.saveFailed ? 0xFFFF8B8B : 0xFF9AABBD);
	}

	//? if >=26.1.2 {
	private void text(GuiGraphicsExtractor graphics, Component text, int x, int y, int color) {
		graphics.text(this.font, text, x, y, color);
	}
	private void text(GuiGraphicsExtractor graphics, FormattedCharSequence text, int x, int y, int color) {
		graphics.text(this.font, text, x, y, color);
	}
	private void centered(GuiGraphicsExtractor graphics, Component text, int x, int y, int color) {
		graphics.centeredText(this.font, text, x, y, color);
	}
	//?} else {
	/*private void text(GuiGraphics graphics, Component text, int x, int y, int color) {
		graphics.drawString(this.font, text, x, y, color);
	}
	private void text(GuiGraphics graphics, FormattedCharSequence text, int x, int y, int color) {
		graphics.drawString(this.font, text, x, y, color);
	}
	private void centered(GuiGraphics graphics, Component text, int x, int y, int color) {
		graphics.drawCenteredString(this.font, text, x, y, color);
	}
	*///?}

	@Override
	public void onClose() {
		//? if >=26.1.2 {
		this.minecraft.setScreenAndShow(this.parent);
		//?} else {
		/*this.minecraft.setScreen(this.parent);
		*///?}
	}

	@Override
	public void removed() {
		if (this.icons != null) {
			this.icons.close();
			this.icons = null;
		}
		super.removed();
	}
}
