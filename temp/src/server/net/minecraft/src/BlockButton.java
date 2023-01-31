package net.minecraft.src;

import java.util.Random;

public class BlockButton extends Block {
	protected BlockButton(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public int tickRate() {
		return 20;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
	}

	public void onBlockPlaced(World world, int x, int y, int z, int notifyFlag) {
		int i6 = world.getBlockMetadata(x, y, z);
		int i7 = i6 & 8;
		i6 &= 7;
		if(notifyFlag == 2 && world.isBlockNormalCube(x, y, z + 1)) {
			i6 = 4;
		}

		if(notifyFlag == 3 && world.isBlockNormalCube(x, y, z - 1)) {
			i6 = 3;
		}

		if(notifyFlag == 4 && world.isBlockNormalCube(x + 1, y, z)) {
			i6 = 2;
		}

		if(notifyFlag == 5 && world.isBlockNormalCube(x - 1, y, z)) {
			i6 = 1;
		}

		world.setBlockMetadataWithNotify(x, y, z, i6 + i7);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(world.isBlockNormalCube(x - 1, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
		} else if(world.isBlockNormalCube(x + 1, y, z)) {
			world.setBlockMetadataWithNotify(x, y, z, 2);
		} else if(world.isBlockNormalCube(x, y, z - 1)) {
			world.setBlockMetadataWithNotify(x, y, z, 3);
		} else if(world.isBlockNormalCube(x, y, z + 1)) {
			world.setBlockMetadataWithNotify(x, y, z, 4);
		}

		this.checkIfAttachedToBlock(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		if(this.checkIfAttachedToBlock(world, x, y, z)) {
			int i6 = world.getBlockMetadata(x, y, z) & 7;
			boolean z7 = false;
			if(!world.isBlockNormalCube(x - 1, y, z) && i6 == 1) {
				z7 = true;
			}

			if(!world.isBlockNormalCube(x + 1, y, z) && i6 == 2) {
				z7 = true;
			}

			if(!world.isBlockNormalCube(x, y, z - 1) && i6 == 3) {
				z7 = true;
			}

			if(!world.isBlockNormalCube(x, y, z + 1) && i6 == 4) {
				z7 = true;
			}

			if(z7) {
				this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
				world.setBlockWithNotify(x, y, z, 0);
			}
		}

	}

	private boolean checkIfAttachedToBlock(World world, int x, int y, int z) {
		if(!this.canPlaceBlockAt(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlockWithNotify(x, y, z, 0);
			return false;
		} else {
			return true;
		}
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		int i5 = blockAccess.getBlockMetadata(x, y, z);
		int i6 = i5 & 7;
		boolean z7 = (i5 & 8) > 0;
		float f8 = 0.375F;
		float f9 = 0.625F;
		float f10 = 0.1875F;
		float f11 = 0.125F;
		if(z7) {
			f11 = 0.0625F;
		}

		if(i6 == 1) {
			this.setBlockBounds(0.0F, f8, 0.5F - f10, f11, f9, 0.5F + f10);
		} else if(i6 == 2) {
			this.setBlockBounds(1.0F - f11, f8, 0.5F - f10, 1.0F, f9, 0.5F + f10);
		} else if(i6 == 3) {
			this.setBlockBounds(0.5F - f10, f8, 0.0F, 0.5F + f10, f9, f11);
		} else if(i6 == 4) {
			this.setBlockBounds(0.5F - f10, f8, 1.0F - f11, 0.5F + f10, f9, 1.0F);
		}

	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(world, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		int i6 = world.getBlockMetadata(x, y, z);
		int i7 = i6 & 7;
		int i8 = 8 - (i6 & 8);
		if(i8 == 0) {
			return true;
		} else {
			world.setBlockMetadataWithNotify(x, y, z, i7 + i8);
			world.markBlocksDirty(x, y, z, x, y, z);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			if(i7 == 1) {
				world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(i7 == 2) {
				world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(i7 == 3) {
				world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(i7 == 4) {
				world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}

			world.scheduleBlockUpdate(x, y, z, this.blockID);
			return true;
		}
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
		if((i5 & 8) > 0) {
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			int i6 = i5 & 7;
			if(i6 == 1) {
				world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(i6 == 2) {
				world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(i6 == 3) {
				world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(i6 == 4) {
				world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		return (blockAccess.getBlockMetadata(x, y, z) & 8) > 0;
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		int i6 = world.getBlockMetadata(x, y, z);
		if((i6 & 8) == 0) {
			return false;
		} else {
			int i7 = i6 & 7;
			return i7 == 5 && flag == 1 ? true : (i7 == 4 && flag == 2 ? true : (i7 == 3 && flag == 3 ? true : (i7 == 2 && flag == 4 ? true : i7 == 1 && flag == 5)));
		}
	}

	public boolean canProvidePower() {
		return true;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		int i6 = world.getBlockMetadata(x, y, z);
		if((i6 & 8) != 0) {
			world.setBlockMetadataWithNotify(x, y, z, i6 & 7);
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			int i7 = i6 & 7;
			if(i7 == 1) {
				world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(i7 == 2) {
				world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(i7 == 3) {
				world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(i7 == 4) {
				world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}

			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
			world.markBlocksDirty(x, y, z, x, y, z);
		}
	}
}
