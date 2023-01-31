package net.minecraft.src;

public class GuiOptions extends GuiScreen {
	private GuiScreen parentScreen;
	protected String screenTitle = "Options";
	private GameSettings options;

	public GuiOptions(GuiScreen guiScreen1, GameSettings gameSettings2) {
		this.parentScreen = guiScreen1;
		this.options = gameSettings2;
	}

	public void initGui() {
		for(int i1 = 0; i1 < this.options.numberOfOptions; ++i1) {
			int i2 = this.options.isSlider(i1);
			if(i2 == 0) {
				this.controlList.add(new GuiSmallButton(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), this.options.getOptionDisplayString(i1)));
			} else {
				this.controlList.add(new GuiSlider(i1, this.width / 2 - 155 + i1 % 2 * 160, this.height / 6 + 24 * (i1 >> 1), i1, this.options.getOptionDisplayString(i1), this.options.getOptionFloatValue(i1)));
			}
		}

		this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, "Controls"));
		this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id < 100) {
				this.options.setOptionValue(button.id, 1);
				button.displayString = this.options.getOptionDisplayString(button.id);
			}

			if(button.id == 100) {
				this.mc.displayGuiScreen(new GuiControls(this, this.options));
			}

			if(button.id == 200) {
				this.mc.displayGuiScreen(this.parentScreen);
			}

		}
	}

	public void drawScreen(int mouseX, int mouseY, float renderPartialTick) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, renderPartialTick);
	}
}
