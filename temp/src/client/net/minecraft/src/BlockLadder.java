package net.minecraft.src;

import java.util.Random;

public class BlockLadder extends Block {
	protected BlockLadder(int blockID, int tex) {
		super(blockID, tex, Material.circuits);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		float f6 = 0.125F;
		if(i5 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f6, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f6);
		}

		if(i5 == 4) {
			this.setBlockBounds(1.0F - f6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f6, 1.0F, 1.0F);
		}

		return super.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		float f6 = 0.125F;
		if(i5 == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f6, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f6);
		}

		if(i5 == 4) {
			this.setBlockBounds(1.0F - f6, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(i5 == 5) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f6, 1.0F, 1.0F);
		}

		return super.getSelectedBoundingBoxFromPool(worldObj, x, y, z);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 8;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
	}

	public void onBlockPlaced(World worldObj, int x, int y, int z, int metadata) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if((i6 == 0 || metadata == 2) && worldObj.isBlockNormalCube(x, y, z + 1)) {
			i6 = 2;
		}

		if((i6 == 0 || metadata == 3) && worldObj.isBlockNormalCube(x, y, z - 1)) {
			i6 = 3;
		}

		if((i6 == 0 || metadata == 4) && worldObj.isBlockNormalCube(x + 1, y, z)) {
			i6 = 4;
		}

		if((i6 == 0 || metadata == 5) && worldObj.isBlockNormalCube(x - 1, y, z)) {
			i6 = 5;
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, i6);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		boolean z7 = false;
		if(i6 == 2 && worldObj.isBlockNormalCube(x, y, z + 1)) {
			z7 = true;
		}

		if(i6 == 3 && worldObj.isBlockNormalCube(x, y, z - 1)) {
			z7 = true;
		}

		if(i6 == 4 && worldObj.isBlockNormalCube(x + 1, y, z)) {
			z7 = true;
		}

		if(i6 == 5 && worldObj.isBlockNormalCube(x - 1, y, z)) {
			z7 = true;
		}

		if(!z7) {
			this.dropBlockAsItem(worldObj, x, y, z, i6);
			worldObj.setBlockWithNotify(x, y, z, 0);
		}

		super.onNeighborBlockChange(worldObj, x, y, z, id);
	}

	public int quantityDropped(Random rand) {
		return 1;
	}
}
