package net.minecraft.src;

public class TileEntityChest extends TileEntity implements IInventory {
	private ItemStack[] chestContents = new ItemStack[36];

	public int getSizeInventory() {
		return 27;
	}

	public ItemStack getStackInSlot(int slot) {
		return this.chestContents[slot];
	}

	public ItemStack decrStackSize(int slot, int stackSize) {
		if(this.chestContents[slot] != null) {
			ItemStack itemStack3;
			if(this.chestContents[slot].stackSize <= stackSize) {
				itemStack3 = this.chestContents[slot];
				this.chestContents[slot] = null;
				this.onInventoryChanged();
				return itemStack3;
			} else {
				itemStack3 = this.chestContents[slot].splitStack(stackSize);
				if(this.chestContents[slot].stackSize == 0) {
					this.chestContents[slot] = null;
				}

				this.onInventoryChanged();
				return itemStack3;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.chestContents[slot] = itemStack;
		if(itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public String getInvName() {
		return "Chest";
	}

	public void readFromNBT(NBTTagCompound compoundTag) {
		super.readFromNBT(compoundTag);
		NBTTagList nBTTagList2 = compoundTag.getTagList("Items");
		this.chestContents = new ItemStack[this.getSizeInventory()];

		for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
			NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3);
			int i5 = nBTTagCompound4.getByte("Slot") & 255;
			if(i5 >= 0 && i5 < this.chestContents.length) {
				this.chestContents[i5] = new ItemStack(nBTTagCompound4);
			}
		}

	}

	public void writeToNBT(NBTTagCompound compoundTag) {
		super.writeToNBT(compoundTag);
		NBTTagList nBTTagList2 = new NBTTagList();

		for(int i3 = 0; i3 < this.chestContents.length; ++i3) {
			if(this.chestContents[i3] != null) {
				NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
				nBTTagCompound4.setByte("Slot", (byte)i3);
				this.chestContents[i3].writeToNBT(nBTTagCompound4);
				nBTTagList2.setTag(nBTTagCompound4);
			}
		}

		compoundTag.setTag("Items", nBTTagList2);
	}

	public int getInventoryStackLimit() {
		return 64;
	}
}
