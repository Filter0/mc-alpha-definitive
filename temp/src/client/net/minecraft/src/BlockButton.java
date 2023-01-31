package net.minecraft.src;

import java.util.Random;

public class BlockButton extends Block {
	protected BlockButton(int id, int tex) {
		super(id, tex, Material.circuits);
		this.setTickOnLoad(true);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public int tickRate() {
		return 20;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : world.isBlockNormalCube(x, y, z + 1)));
	}

	public void onBlockPlaced(World worldObj, int x, int y, int z, int metadata) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		int i7 = i6 & 8;
		i6 &= 7;
		if(metadata == 2 && worldObj.isBlockNormalCube(x, y, z + 1)) {
			i6 = 4;
		}

		if(metadata == 3 && worldObj.isBlockNormalCube(x, y, z - 1)) {
			i6 = 3;
		}

		if(metadata == 4 && worldObj.isBlockNormalCube(x + 1, y, z)) {
			i6 = 2;
		}

		if(metadata == 5 && worldObj.isBlockNormalCube(x - 1, y, z)) {
			i6 = 1;
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, i6 + i7);
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		if(worldObj.isBlockNormalCube(x - 1, y, z)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 1);
		} else if(worldObj.isBlockNormalCube(x + 1, y, z)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 2);
		} else if(worldObj.isBlockNormalCube(x, y, z - 1)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 3);
		} else if(worldObj.isBlockNormalCube(x, y, z + 1)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 4);
		}

		this.checkIfAttachedToBlock(worldObj, x, y, z);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(this.checkIfAttachedToBlock(worldObj, x, y, z)) {
			int i6 = worldObj.getBlockMetadata(x, y, z) & 7;
			boolean z7 = false;
			if(!worldObj.isBlockNormalCube(x - 1, y, z) && i6 == 1) {
				z7 = true;
			}

			if(!worldObj.isBlockNormalCube(x + 1, y, z) && i6 == 2) {
				z7 = true;
			}

			if(!worldObj.isBlockNormalCube(x, y, z - 1) && i6 == 3) {
				z7 = true;
			}

			if(!worldObj.isBlockNormalCube(x, y, z + 1) && i6 == 4) {
				z7 = true;
			}

			if(z7) {
				this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
				worldObj.setBlockWithNotify(x, y, z, 0);
			}
		}

	}

	private boolean checkIfAttachedToBlock(World worldObj, int x, int y, int z) {
		if(!this.canPlaceBlockAt(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
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

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(worldObj, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		int i7 = i6 & 7;
		int i8 = 8 - (i6 & 8);
		if(i8 == 0) {
			return true;
		} else {
			worldObj.setBlockMetadataWithNotify(x, y, z, i7 + i8);
			worldObj.markBlocksDirty(x, y, z, x, y, z);
			worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			if(i7 == 1) {
				worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(i7 == 2) {
				worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(i7 == 3) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(i7 == 4) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}

			worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
			return true;
		}
	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		if((i5 & 8) > 0) {
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			int i6 = i5 & 7;
			if(i6 == 1) {
				worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(i6 == 2) {
				worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(i6 == 3) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(i6 == 4) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}
		}

		super.onBlockRemoval(worldObj, x, y, z);
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		return (blockAccess.getBlockMetadata(x, y, z) & 8) > 0;
	}

	public boolean isIndirectlyPoweringTo(World worldObj, int x, int y, int z, int side) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if((i6 & 8) == 0) {
			return false;
		} else {
			int i7 = i6 & 7;
			return i7 == 5 && side == 1 ? true : (i7 == 4 && side == 2 ? true : (i7 == 3 && side == 3 ? true : (i7 == 2 && side == 4 ? true : i7 == 1 && side == 5)));
		}
	}

	public boolean canProvidePower() {
		return true;
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if((i6 & 8) != 0) {
			worldObj.setBlockMetadataWithNotify(x, y, z, i6 & 7);
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			int i7 = i6 & 7;
			if(i7 == 1) {
				worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			} else if(i7 == 2) {
				worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			} else if(i7 == 3) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			} else if(i7 == 4) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			} else {
				worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			}

			worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
			worldObj.markBlocksDirty(x, y, z, x, y, z);
		}
	}

	public void setBlockBoundsForItemRender() {
		float f1 = 0.1875F;
		float f2 = 0.125F;
		float f3 = 0.125F;
		this.setBlockBounds(0.5F - f1, 0.5F - f2, 0.5F - f3, 0.5F + f1, 0.5F + f2, 0.5F + f3);
	}
}
