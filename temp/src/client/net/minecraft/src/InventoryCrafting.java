package net.minecraft.src;

public class InventoryCrafting implements IInventory {
	private ItemStack[] stackList;
	private int gridSize;
	private CraftingInventoryCB craftingInventory;

	public InventoryCrafting(CraftingInventoryCB craftingInventoryCB1, int i2, int i3) {
		this.gridSize = i2 * i3;
		this.stackList = new ItemStack[this.gridSize];
		this.craftingInventory = craftingInventoryCB1;
	}

	public InventoryCrafting(CraftingInventoryCB craftingInventoryCB1, ItemStack[] itemStack2) {
		this.gridSize = itemStack2.length;
		this.stackList = itemStack2;
		this.craftingInventory = craftingInventoryCB1;
	}

	public int getSizeInventory() {
		return this.gridSize;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.stackList[slot];
	}

	public String getInvName() {
		return "Crafting";
	}

	public ItemStack decrStackSize(int slot, int stackSize) {
		if(this.stackList[slot] != null) {
			ItemStack itemStack3;
			if(this.stackList[slot].stackSize <= stackSize) {
				itemStack3 = this.stackList[slot];
				this.stackList[slot] = null;
				this.craftingInventory.onCraftMatrixChanged(this);
				return itemStack3;
			} else {
				itemStack3 = this.stackList[slot].splitStack(stackSize);
				if(this.stackList[slot].stackSize == 0) {
					this.stackList[slot] = null;
				}

				this.craftingInventory.onCraftMatrixChanged(this);
				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.stackList[slot] = itemStack;
		this.craftingInventory.onCraftMatrixChanged(this);
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
	}
}
