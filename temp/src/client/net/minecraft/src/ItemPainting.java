package net.minecraft.src;

public class ItemPainting extends Item {
	public ItemPainting(int i1) {
		super(i1);
		this.maxDamage = 64;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(side == 0) {
			return false;
		} else if(side == 1) {
			return false;
		} else {
			byte b8 = 0;
			if(side == 4) {
				b8 = 1;
			}

			if(side == 3) {
				b8 = 2;
			}

			if(side == 5) {
				b8 = 3;
			}

			EntityPainting entityPainting9 = new EntityPainting(worldObj, x, y, z, b8);
			if(entityPainting9.onValidSurface()) {
				worldObj.spawnEntityInWorld(entityPainting9);
				--itemStack.stackSize;
			}

			return true;
		}
	}
}
