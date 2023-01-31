package net.minecraft.src;

public class ItemHoe extends Item {
	public ItemHoe(int id, int strength) {
		super(id);
		this.maxStackSize = 1;
		this.maxDamage = 32 << strength;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World worldObj, int x, int y, int z, int side) {
		int i8 = worldObj.getBlockId(x, y, z);
		Material material9 = worldObj.getBlockMaterial(x, y + 1, z);
		if((material9.isSolid() || i8 != Block.grass.blockID) && i8 != Block.dirt.blockID) {
			return false;
		} else {
			Block block10 = Block.tilledField;
			worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block10.stepSound.getStepSound(), (block10.stepSound.getVolume() + 1.0F) / 2.0F, block10.stepSound.getPitch() * 0.8F);
			worldObj.setBlockWithNotify(x, y, z, block10.blockID);
			itemStack.damageItem(1);
			if(worldObj.rand.nextInt(8) == 0 && i8 == Block.grass.blockID) {
				byte b11 = 1;

				for(int i12 = 0; i12 < b11; ++i12) {
					float f13 = 0.7F;
					float f14 = worldObj.rand.nextFloat() * f13 + (1.0F - f13) * 0.5F;
					float f15 = 1.2F;
					float f16 = worldObj.rand.nextFloat() * f13 + (1.0F - f13) * 0.5F;
					EntityItem entityItem17 = new EntityItem(worldObj, (double)((float)x + f14), (double)((float)y + f15), (double)((float)z + f16), new ItemStack(Item.seeds));
					entityItem17.delayBeforeCanPickup = 10;
					worldObj.spawnEntityInWorld(entityItem17);
				}
			}

			return true;
		}
	}

	public boolean isFull3D() {
		return true;
	}
}
