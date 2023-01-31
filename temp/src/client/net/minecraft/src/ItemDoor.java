package net.minecraft.src;

public class ItemDoor extends Item {
	private Material material;

	public ItemDoor(int id, Material material) {
		super(id);
		this.material = material;
		this.maxDamage = 64;
		this.maxStackSize = 1;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		if(side != 1) {
			return false;
		} else {
			++y;
			Block block8;
			if(this.material == Material.wood) {
				block8 = Block.doorWood;
			} else {
				block8 = Block.doorSteel;
			}

			if(!block8.canPlaceBlockAt(worldObj, x, y, z)) {
				return false;
			} else {
				int i9 = MathHelper.floor_double((double)((entityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
				byte b10 = 0;
				byte b11 = 0;
				if(i9 == 0) {
					b11 = 1;
				}

				if(i9 == 1) {
					b10 = -1;
				}

				if(i9 == 2) {
					b11 = -1;
				}

				if(i9 == 3) {
					b10 = 1;
				}

				int i12 = (worldObj.isBlockNormalCube(x - b10, y, z - b11) ? 1 : 0) + (worldObj.isBlockNormalCube(x - b10, y + 1, z - b11) ? 1 : 0);
				int i13 = (worldObj.isBlockNormalCube(x + b10, y, z + b11) ? 1 : 0) + (worldObj.isBlockNormalCube(x + b10, y + 1, z + b11) ? 1 : 0);
				boolean z14 = worldObj.getBlockId(x - b10, y, z - b11) == block8.blockID || worldObj.getBlockId(x - b10, y + 1, z - b11) == block8.blockID;
				boolean z15 = worldObj.getBlockId(x + b10, y, z + b11) == block8.blockID || worldObj.getBlockId(x + b10, y + 1, z + b11) == block8.blockID;
				boolean z16 = false;
				if(z14 && !z15) {
					z16 = true;
				} else if(i13 > i12) {
					z16 = true;
				}

				if(z16) {
					i9 = i9 - 1 & 3;
					i9 += 4;
				}

				worldObj.setBlockWithNotify(x, y, z, block8.blockID);
				worldObj.setBlockMetadataWithNotify(x, y, z, i9);
				worldObj.setBlockWithNotify(x, y + 1, z, block8.blockID);
				worldObj.setBlockMetadataWithNotify(x, y + 1, z, i9 + 8);
				--itemStack.stackSize;
				return true;
			}
		}
	}
}
