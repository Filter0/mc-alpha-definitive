package net.minecraft.src;

public class InventoryLargeChest implements IInventory {
	private String name;
	private IInventory upperChest;
	private IInventory lowerChest;

	public InventoryLargeChest(String string1, IInventory iInventory2, IInventory iInventory3) {
		this.name = string1;
		this.upperChest = iInventory2;
		this.lowerChest = iInventory3;
	}

	public int getSizeInventory() {
		return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
	}

	public String getInvName() {
		return this.name;
	}

	public ItemStack getStackInSlot(int slot) {
		return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(slot - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(slot);
	}

	public ItemStack decrStackSize(int slot, int stackSize) {
		return slot >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(slot - this.upperChest.getSizeInventory(), stackSize) : this.upperChest.decrStackSize(slot, stackSize);
	}

	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if(slot >= this.upperChest.getSizeInventory()) {
			this.lowerChest.setInventorySlotContents(slot - this.upperChest.getSizeInventory(), itemStack);
		} else {
			this.upperChest.setInventorySlotContents(slot, itemStack);
		}

	}

	public int getInventoryStackLimit() {
		return this.upperChest.getInventoryStackLimit();
	}

	public void onInventoryChanged() {
		this.upperChest.onInventoryChanged();
		this.lowerChest.onInventoryChanged();
	}
}
