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
//? if >=1.21.10 {
import net.minecraft.client.input.MouseButtonEvent;
//?}
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
//? if >=1.21.3 && <1.21.8 {
/*import net.minecraft.client.renderer.RenderType;
*///?}

public final class BypassScreen extends Screen {
	private static final int LIST_ICON_SIZE = 16;
	private static final int DETAIL_ICON_SIZE = 32;
	//? if >=1.21.10 {
	private static final int LEFT_BUTTON = 1;
	//?} else {
	/*private static final int LEFT_BUTTON = 0;
	*///?}

	private final Screen parent;
	private ModIcons icons;
	private List<FormattedCharSequence> description = List.of();
	private int selected;
	private int rowHeight;
	private int topRow;
	private int visibleRows;
	private int listLeft;
	private int listWidth;
	private int detailLeft;
	private int detailWidth;
	private int contentTop;
	private int contentBottom;
	private boolean saveFailed;
	private boolean draggingScrollbar;
	private double dragOffset;

	public BypassScreen(Screen parent) {
		super(Component.translatable("shader_exit.screen.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		if (this.icons == null) this.icons = new ModIcons();
		SupportedMod[] mods = SupportedMod.values();
		this.selected = Math.clamp(this.selected, 0, mods.length - 1);

		int totalWidth = Math.min(560, this.width - 24);
		int totalLeft = (this.width - totalWidth) / 2;
		this.listWidth = Math.max(140, totalWidth * 2 / 5);
		this.listLeft = totalLeft;
		this.detailLeft = totalLeft + this.listWidth + 12;
		this.detailWidth = totalLeft + totalWidth - this.detailLeft;

		this.contentTop = 32;
		this.contentBottom = this.height - 58;
		this.rowHeight = Math.max(LIST_ICON_SIZE + 4, this.font.lineHeight + 6);
		this.visibleRows = Math.max(1, (this.contentBottom - this.contentTop) / this.rowHeight);
		this.topRow = Math.clamp(this.topRow, 0, this.maxTopRow());

		SupportedMod mod = mods[this.selected];
		this.description = this.font.split(mod.description(), Math.max(1, this.detailWidth));

		Button toggle = Button.builder(this.toggleLabel(mod), button -> {
			this.saveFailed = !ShaderExit.CONFIG.setEnabled(mod.id(), !mod.enabled());
			button.setMessage(this.toggleLabel(mod));
		}).bounds(this.detailLeft + this.detailWidth - 100, this.contentTop + 8, 100, 20)
			.tooltip(Tooltip.create(Component.translatable("shader_exit.screen.toggle_hint")))
			.createNarration(supplier -> Component.empty().append(mod.displayName()).append(": ")
				.append(this.toggleLabel(mod)).append(". ").append(mod.description()))
			.build();
		toggle.active = mod.installed();
		this.addRenderableWidget(toggle);

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
			.bounds(this.width / 2 - 50, this.height - 28, 100, 20).build());
	}

	private Component toggleLabel(SupportedMod mod) {
		if (!mod.installed()) return Component.translatable("shader_exit.screen.not_installed");
		return Component.translatable(mod.enabled() ? "shader_exit.screen.enabled" : "shader_exit.screen.disabled");
	}

	private void select(int index) {
		if (index != this.selected) {
			this.selected = index;
			this.rebuildWidgets();
		}
	}

	private int maxTopRow() {
		return Math.max(0, SupportedMod.values().length - this.visibleRows);
	}

	private int thumbHeight() {
		int track = this.contentBottom - this.contentTop;
		return Math.max(20, track * this.visibleRows / SupportedMod.values().length);
	}

	private int thumbY() {
		int track = this.contentBottom - this.contentTop - this.thumbHeight();
		if (this.maxTopRow() <= 0 || track <= 0) return this.contentTop;
		return this.contentTop + track * this.topRow / this.maxTopRow();
	}

	private void scrollBy(int rows) {
		int next = Math.clamp(this.topRow + rows, 0, this.maxTopRow());
		if (next != this.topRow) {
			this.topRow = next;
			this.rebuildWidgets();
		}
	}

	private void setScrollFromThumbY(double desiredThumbY) {
		int thumbHeight = this.thumbHeight();
		int track = this.contentBottom - this.contentTop - thumbHeight;
		int next;
		if (track <= 0 || this.maxTopRow() <= 0) {
			next = 0;
		} else {
			double relative = Math.clamp((desiredThumbY - this.contentTop) / track, 0, 1);
			next = (int) Math.round(relative * this.maxTopRow());
		}
		if (next != this.topRow) {
			this.topRow = next;
			this.rebuildWidgets();
		}
	}

	private boolean overList(double mouseX, double mouseY) {
		return mouseX >= this.listLeft && mouseX < this.listLeft + this.listWidth
			&& mouseY >= this.contentTop && mouseY < this.contentBottom;
	}

	private boolean overScrollbar(double mouseX, double mouseY) {
		int barLeft = this.listLeft + this.listWidth - 8;
		int barRight = this.listLeft + this.listWidth - 1;
		return this.maxTopRow() > 0 && mouseX >= barLeft && mouseX <= barRight
			&& mouseY >= this.contentTop && mouseY <= this.contentBottom;
	}

	@Override
	//? if >=1.21.10 {
	public boolean mouseClicked(MouseButtonEvent click, boolean doubleClick) {
		double mouseX = click.x();
		double mouseY = click.y();
		int button = click.button();
	//?} else {
	/*public boolean mouseClicked(double mouseX, double mouseY, int button) {
	*///?}
		if (button == LEFT_BUTTON && this.overScrollbar(mouseX, mouseY)) {
			int thumbHeight = this.thumbHeight();
			int thumbY = this.thumbY();
			if (mouseY >= thumbY && mouseY < thumbY + thumbHeight) {
				this.dragOffset = mouseY - thumbY;
			} else {
				this.dragOffset = thumbHeight / 2.0;
				this.setScrollFromThumbY(mouseY - this.dragOffset);
			}
			this.draggingScrollbar = true;
			return true;
		}
		if (button == LEFT_BUTTON && this.overList(mouseX, mouseY)) {
			int index = this.topRow + (int) ((mouseY - this.contentTop) / this.rowHeight);
			if (index >= 0 && index < SupportedMod.values().length) {
				this.select(index);
				return true;
			}
		}
		//? if >=1.21.10 {
		return super.mouseClicked(click, doubleClick);
		//?} else {
		/*return super.mouseClicked(mouseX, mouseY, button);
		*///?}
	}

	@Override
	//? if >=1.21.10 {
	public boolean mouseDragged(MouseButtonEvent drag, double dragX, double dragY) {
		double mouseX = drag.x();
		double mouseY = drag.y();
	//?} else {
	/*public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
	*///?}
		if (this.draggingScrollbar) {
			this.setScrollFromThumbY(mouseY - this.dragOffset);
			return true;
		}
		//? if >=1.21.10 {
		return super.mouseDragged(drag, dragX, dragY);
		//?} else {
		/*return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
		*///?}
	}

	@Override
	//? if >=1.21.10 {
	public boolean mouseReleased(MouseButtonEvent release) {
		if (release.button() == LEFT_BUTTON) this.draggingScrollbar = false;
		return super.mouseReleased(release);
	}
	//?} else {
	/*public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button == LEFT_BUTTON) this.draggingScrollbar = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}
	*///?}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
		if (vertical != 0 && this.overList(mouseX, mouseY)) {
			this.scrollBy(vertical > 0 ? -1 : 1);
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
		this.centered(graphics, this.title, this.width / 2, 14, 0xFFFFFFFF);

		SupportedMod[] mods = SupportedMod.values();
		int hoveredRow = this.overList(mouseX, mouseY) ? this.topRow + (mouseY - this.contentTop) / this.rowHeight : -1;
		for (int row = 0; row < this.visibleRows; row++) {
			int index = this.topRow + row;
			if (index >= mods.length) break;
			SupportedMod mod = mods[index];
			int y = this.contentTop + row * this.rowHeight;
			boolean installed = mod.installed();
			if (index == this.selected) {
				graphics.fill(this.listLeft - 2, y - 2, this.listLeft + this.listWidth - 10, y + this.rowHeight, 0xFFFFFFFF);
				graphics.fill(this.listLeft - 1, y - 1, this.listLeft + this.listWidth - 11, y + this.rowHeight - 1, 0xFF000000);
			} else if (index == hoveredRow) {
				graphics.fill(this.listLeft - 2, y - 2, this.listLeft + this.listWidth - 10, y + this.rowHeight, 0x20FFFFFF);
			}
			int iconY = y + (this.rowHeight - LIST_ICON_SIZE) / 2;
			this.drawIcon(graphics, mod, this.listLeft, iconY, LIST_ICON_SIZE, installed);
			this.text(graphics, mod.displayName(), this.listLeft + LIST_ICON_SIZE + 6,
				y + (this.rowHeight - this.font.lineHeight) / 2, installed ? 0xFFFFFFFF : 0xFF808080);
		}
		if (this.maxTopRow() > 0) {
			int barLeft = this.listLeft + this.listWidth - 6;
			int barRight = this.listLeft + this.listWidth - 2;
			graphics.fill(barLeft, this.contentTop, barRight, this.contentBottom, 0x30000000);
			int thumbY = this.thumbY();
			graphics.fill(barLeft, thumbY, barRight, thumbY + this.thumbHeight(), 0xFF8F8F8F);
		}

		SupportedMod mod = mods[this.selected];
		boolean installed = mod.installed();
		this.drawIcon(graphics, mod, this.detailLeft, this.contentTop, DETAIL_ICON_SIZE, installed);
		int textX = this.detailLeft + DETAIL_ICON_SIZE + 8;
		this.text(graphics, mod.displayName(), textX, this.contentTop + 2, installed ? 0xFFFFFFFF : 0xFF808080);
		Component status = Component.translatable(installed ? "shader_exit.screen.installed" : "shader_exit.screen.not_installed");
		this.text(graphics, status, textX, this.contentTop + 2 + this.font.lineHeight + 2, installed ? 0xFF8EC28B : 0xFF707070);
		graphics.fill(this.detailLeft, this.contentTop + DETAIL_ICON_SIZE + 8, this.detailLeft + this.detailWidth,
			this.contentTop + DETAIL_ICON_SIZE + 9, 0xFF3A3A3A);
		int descriptionY = this.contentTop + DETAIL_ICON_SIZE + 18;
		for (FormattedCharSequence line : this.description) {
			this.text(graphics, line, this.detailLeft, descriptionY, installed ? 0xFFA0A0A0 : 0xFF707070);
			descriptionY += this.font.lineHeight;
		}

		Component footer = this.saveFailed ? Component.translatable("shader_exit.screen.save_failed")
			: Component.translatable("shader_exit.screen.saved_hint");
		this.centered(graphics, footer, this.width / 2, this.height - 49, this.saveFailed ? 0xFFFF8B8B : 0xFFA0A0A0);
	}

	private void drawIcon(
		//? if >=26.1.2 {
		GuiGraphicsExtractor graphics,
		//?} else {
		/*GuiGraphics graphics,
		*///?}
		SupportedMod mod, int x, int y, int size, boolean installed
	) {
		var icon = this.icons.get(mod);
		if (icon != null) {
			//? if >=1.21.8 {
			graphics.blit(icon, x, y, x + size, y + size, 0, 1, 0, 1);
			//?} else if >=1.21.3 {
			/*graphics.blit(RenderType::guiTextured, icon, x, y, 0, 0, size, size, size, size);
			*///?} else {
			/*graphics.blit(icon, x, y, 0, 0, size, size, size, size);
			*///?}
		} else {
			graphics.fill(x, y, x + size, y + size, installed ? mod.color() : 0xFF343D47);
			this.centered(graphics, Component.literal(mod.initials()), x + size / 2, y + size / 2 - this.font.lineHeight / 2,
				installed ? 0xFF15202B : 0xFF67727E);
		}
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
