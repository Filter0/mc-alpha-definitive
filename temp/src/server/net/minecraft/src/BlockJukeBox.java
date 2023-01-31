package net.minecraft.src;

public class BlockJukeBox extends Block {
	protected BlockJukeBox(int id, int blockIndex) {
		super(id, blockIndex, Material.wood);
	}

	public int getBlockTextureFromSide(int side) {
		return this.blockIndexInTexture + (side == 1 ? 1 : 0);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		int i6 = world.getBlockMetadata(x, y, z);
		if(i6 > 0) {
			this.ejectRecord(world, x, y, z, i6);
			return true;
		} else {
			return false;
		}
	}

	public void ejectRecord(World world, int x, int y, int z, int i5) {
		world.playRecord((String)null, x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, 0);
		int i6 = Item.record13.shiftedIndex + i5 - 1;
		float f7 = 0.7F;
		double d8 = (double)(world.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		double d10 = (double)(world.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.2D + 0.6D;
		double d12 = (double)(world.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		EntityItem entityItem14 = new EntityItem(world, (double)x + d8, (double)y + d10, (double)z + d12, new ItemStack(i6));
		entityItem14.delayBeforeCanPickup = 10;
		world.spawnEntityInWorld(entityItem14);
	}

	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int i5, float f6) {
		if(!world.multiplayerWorld) {
			if(i5 > 0) {
				this.ejectRecord(world, x, y, z, i5);
			}

			super.dropBlockAsItemWithChance(world, x, y, z, i5, f6);
		}
	}
}
