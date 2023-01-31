package net.minecraft.src;

public class BlockJukeBox extends Block {
	protected BlockJukeBox(int blockID, int tex) {
		super(blockID, tex, Material.wood);
	}

	public int getBlockTextureFromSide(int side) {
		return this.blockIndexInTexture + (side == 1 ? 1 : 0);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if(i6 > 0) {
			this.ejectRecord(worldObj, x, y, z, i6);
			return true;
		} else {
			return false;
		}
	}

	public void ejectRecord(World worldObj, int x, int y, int z, int i5) {
		worldObj.playRecord((String)null, x, y, z);
		worldObj.setBlockMetadataWithNotify(x, y, z, 0);
		int i6 = Item.record13.shiftedIndex + i5 - 1;
		float f7 = 0.7F;
		double d8 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		double d10 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.2D + 0.6D;
		double d12 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		EntityItem entityItem14 = new EntityItem(worldObj, (double)x + d8, (double)y + d10, (double)z + d12, new ItemStack(i6));
		entityItem14.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entityItem14);
	}

	public void dropBlockAsItemWithChance(World worldObj, int x, int y, int z, int metadata, float chance) {
		if(!worldObj.multiplayerWorld) {
			if(metadata > 0) {
				this.ejectRecord(worldObj, x, y, z, metadata);
			}

			super.dropBlockAsItemWithChance(worldObj, x, y, z, metadata, chance);
		}
	}
}
