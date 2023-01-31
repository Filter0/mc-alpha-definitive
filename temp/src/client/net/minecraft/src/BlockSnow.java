package net.minecraft.src;

import java.util.Random;

public class BlockSnow extends Block {
	protected BlockSnow(int id, int tex) {
		super(id, tex, Material.snow);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int i5 = world.getBlockId(x, y - 1, z);
		return i5 != 0 && Block.blocksList[i5].isOpaqueCube() ? world.getBlockMaterial(x, y - 1, z).getIsSolid() : false;
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		this.canSnowStay(worldObj, x, y, z);
	}

	private boolean canSnowStay(World worldObj, int x, int y, int z) {
		if(!this.canPlaceBlockAt(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public void harvestBlock(World worldObj, int x, int y, int z, int metadata) {
		int i6 = Item.snowball.shiftedIndex;
		float f7 = 0.7F;
		double d8 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		double d10 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		double d12 = (double)(worldObj.rand.nextFloat() * f7) + (double)(1.0F - f7) * 0.5D;
		EntityItem entityItem14 = new EntityItem(worldObj, (double)x + d8, (double)y + d10, (double)z + d12, new ItemStack(i6));
		entityItem14.delayBeforeCanPickup = 10;
		worldObj.spawnEntityInWorld(entityItem14);
		worldObj.setBlockWithNotify(x, y, z, 0);
	}

	public int idDropped(int metadata, Random rand) {
		return Item.snowball.shiftedIndex;
	}

	public int quantityDropped(Random rand) {
		return 0;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		Material material6 = blockAccess.getBlockMaterial(x, y, z);
		return side == 1 ? true : (material6 == this.material ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side));
	}
}
