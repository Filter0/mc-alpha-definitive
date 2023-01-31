package net.minecraft.src;

public class ItemBlock extends Item {
	private int blockID;

	public ItemBlock(int i1) {
		super(i1);
		this.blockID = i1 + 256;
		this.setIconIndex(Block.blocksList[i1 + 256].getBlockTextureFromSide(2));
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(worldObj.getBlockId(x, y, z) == Block.snow.blockID) {
			side = 0;
		} else {
			if(side == 0) {
				--y;
			}

			if(side == 1) {
				++y;
			}

			if(side == 2) {
				--z;
			}

			if(side == 3) {
				++z;
			}

			if(side == 4) {
				--x;
			}

			if(side == 5) {
				++x;
			}
		}

		if(itemStack.stackSize == 0) {
			return false;
		} else {
			if(worldObj.canBlockBePlacedAt(this.blockID, x, y, z, false)) {
				Block block8 = Block.blocksList[this.blockID];
				if(worldObj.setBlockWithNotify(x, y, z, this.blockID)) {
					Block.blocksList[this.blockID].onBlockPlaced(worldObj, x, y, z, side);
					worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block8.stepSound.getStepSound(), (block8.stepSound.getVolume() + 1.0F) / 2.0F, block8.stepSound.getPitch() * 0.8F);
					--itemStack.stackSize;
				}
			}

			return true;
		}
	}
}
