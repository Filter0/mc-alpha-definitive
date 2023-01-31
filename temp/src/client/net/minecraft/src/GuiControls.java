package net.minecraft.src;

public class GuiControls extends GuiScreen {
	private GuiScreen parentScreen;
	protected String screenTitle = "Controls";
	private GameSettings options;
	private int buttonId = -1;

	public GuiControls(GuiScreen prevScreen, GameSettings gameSettings) {
		this.parentScreen = prevScreen;
		this.options = gameSettings;
	}

	public void initGui() {
		for(int i1 = 0; i1 < this.options.keyBindings.length; ++i1) {
			this.controlList.add(new GuiSmallButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.getKeyBindingDescription(i1)));
		}

		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected void actionPerformed(GuiButton button) {
		for(int i2 = 0; i2 < this.options.keyBindings.length; ++i2) {
			((GuiButton)this.controlList.get(i2)).displayString = this.options.getKeyBindingDescription(i2);
		}

		if(button.id == 200) {
			this.mc.displayGuiScreen(this.parentScreen);
		} else {
			this.buttonId = button.id;
			button.displayString = "> " + this.options.getKeyBindingDescription(button.id) + " <";
		}

	}

	protected void keyTyped(char character, int key) {
		if(this.buttonId >= 0) {
			this.options.setKeyBinding(this.buttonId, key);
			((GuiButton)this.controlList.get(this.buttonId)).displayString = this.options.getKeyBindingDescription(this.buttonId);
			this.buttonId = -1;
		} else {
			super.keyTyped(character, key);
		}

	}

	public void drawScreen(int mouseX, int mouseY, float renderPartialTick) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, renderPartialTick);
	}
}
