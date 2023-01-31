package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	private Minecraft mc;

	public EntityPlayerSP(Minecraft mc, World worldObj, Session session) {
		super(worldObj);
		this.mc = mc;
		if(session != null && session.username != null && session.username.length() > 0) {
			this.skinUrl = "http://www.minecraft.net/skin/" + session.username + ".png";
			System.out.println("Loading texture " + this.skinUrl);
		}

		this.username = session.username;
	}

	public void updateEntityActionState() {
		super.updateEntityActionState();
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
	}

	public void onLivingUpdate() {
		this.movementInput.updatePlayerMoveState(this);
		if(this.movementInput.sneak && this.ySize < 0.2F) {
			this.ySize = 0.2F;
		}

		super.onLivingUpdate();
	}

	public void resetPlayerKeyState() {
		this.movementInput.resetKeyState();
	}

	public void handleKeyPress(int i1, boolean z2) {
		this.movementInput.checkKeyForMovementInput(i1, z2);
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		super.writeEntityToNBT(compoundTag);
		compoundTag.setInteger("Score", this.score);
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		super.readEntityFromNBT(compoundTag);
		this.score = compoundTag.getInteger("Score");
	}

	public void displayGUIChest(IInventory inventory) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, inventory));
	}

	public void displayGUIEditSign(TileEntitySign signTileEntity) {
		this.mc.displayGuiScreen(new GuiEditSign(signTileEntity));
	}

	public void displayWorkbenchGUI() {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory));
	}

	public void displayGUIFurnace(TileEntityFurnace furnaceTileEntity) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, furnaceTileEntity));
	}

	public void attackEntity(Entity entity) {
		int i2 = this.inventory.getDamageVsEntity(entity);
		if(i2 > 0) {
			entity.attackEntityFrom(this, i2);
			ItemStack itemStack3 = this.getCurrentEquippedItem();
			if(itemStack3 != null && entity instanceof EntityLiving) {
				itemStack3.hitEntity((EntityLiving)entity);
				if(itemStack3.stackSize <= 0) {
					itemStack3.onItemDestroyedByUse(this);
					this.destroyCurrentEquippedItem();
				}
			}
		}

	}

	public void onItemPickup(Entity entity, int i2) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return this.inventory.getTotalArmorValue();
	}

	public void interactWithEntity(Entity entity) {
		if(!entity.interact(this)) {
			ItemStack itemStack2 = this.getCurrentEquippedItem();
			if(itemStack2 != null && entity instanceof EntityLiving) {
				itemStack2.useItemOnEntity((EntityLiving)entity);
				if(itemStack2.stackSize <= 0) {
					itemStack2.onItemDestroyedByUse(this);
					this.destroyCurrentEquippedItem();
				}
			}

		}
	}

	public void sendChatMessage(String chatMessage) {
	}

	public void onPlayerUpdate() {
	}

	public boolean isSneaking() {
		return this.movementInput.sneak;
	}
}
