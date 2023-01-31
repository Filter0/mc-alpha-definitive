package net.minecraft.src;

import java.util.Random;

public class BlockLadder extends Block {
	protected BlockLadder(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
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

		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 8;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
	}

	public void onBlockPlaced(World world, int x, int y, int z, int notifyFlag) {
		int i6 = world.getBlockMetadata(x, y, z);
		if((i6 == 0 || notifyFlag == 2) && world.isBlockNormalCube(x, y, z + 1)) {
			i6 = 2;
		}

		if((i6 == 0 || notifyFlag == 3) && world.isBlockNormalCube(x, y, z - 1)) {
			i6 = 3;
		}

		if((i6 == 0 || notifyFlag == 4) && world.isBlockNormalCube(x + 1, y, z)) {
			i6 = 4;
		}

		if((i6 == 0 || notifyFlag == 5) && world.isBlockNormalCube(x - 1, y, z)) {
			i6 = 5;
		}

		world.setBlockMetadataWithNotify(x, y, z, i6);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		int i6 = world.getBlockMetadata(x, y, z);
		boolean z7 = false;
		if(i6 == 2 && world.isBlockNormalCube(x, y, z + 1)) {
			z7 = true;
		}

		if(i6 == 3 && world.isBlockNormalCube(x, y, z - 1)) {
			z7 = true;
		}

		if(i6 == 4 && world.isBlockNormalCube(x + 1, y, z)) {
			z7 = true;
		}

		if(i6 == 5 && world.isBlockNormalCube(x - 1, y, z)) {
			z7 = true;
		}

		if(!z7) {
			this.dropBlockAsItem(world, x, y, z, i6);
			world.setBlockWithNotify(x, y, z, 0);
		}

		super.onNeighborBlockChange(world, x, y, z, flag);
	}

	public int quantityDropped(Random random) {
		return 1;
	}
}
