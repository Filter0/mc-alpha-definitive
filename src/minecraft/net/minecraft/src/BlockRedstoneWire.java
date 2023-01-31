package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneWire extends Block {
	private boolean wiresProvidePower = true;

	public BlockRedstoneWire(int id, int tex) {
		super(id, tex, Material.circuits);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return this.blockIndexInTexture + (metadata > 0 ? 16 : 0);
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

	public int getRenderType() {
		return 5;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z);
	}

	private void updateAndPropagateCurrentStrength(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		int i6 = 0;
		this.wiresProvidePower = false;
		boolean z7 = worldObj.isBlockIndirectlyGettingPowered(x, y, z);
		this.wiresProvidePower = true;
		int i8;
		int i9;
		int i10;
		if(z7) {
			i6 = 15;
		} else {
			for(i8 = 0; i8 < 4; ++i8) {
				i9 = x;
				i10 = z;
				if(i8 == 0) {
					i9 = x - 1;
				}

				if(i8 == 1) {
					++i9;
				}

				if(i8 == 2) {
					i10 = z - 1;
				}

				if(i8 == 3) {
					++i10;
				}

				i6 = this.getMaxCurrentStrength(worldObj, i9, y, i10, i6);
				if(worldObj.isBlockNormalCube(i9, y, i10) && !worldObj.isBlockNormalCube(x, y + 1, z)) {
					i6 = this.getMaxCurrentStrength(worldObj, i9, y + 1, i10, i6);
				} else if(!worldObj.isBlockNormalCube(i9, y, i10)) {
					i6 = this.getMaxCurrentStrength(worldObj, i9, y - 1, i10, i6);
				}
			}

			if(i6 > 0) {
				--i6;
			} else {
				i6 = 0;
			}
		}

		if(i5 != i6) {
			worldObj.setBlockMetadataWithNotify(x, y, z, i6);
			worldObj.markBlocksDirty(x, y, z, x, y, z);
			if(i6 > 0) {
				--i6;
			}

			for(i8 = 0; i8 < 4; ++i8) {
				i9 = x;
				i10 = z;
				int i11 = y - 1;
				if(i8 == 0) {
					i9 = x - 1;
				}

				if(i8 == 1) {
					++i9;
				}

				if(i8 == 2) {
					i10 = z - 1;
				}

				if(i8 == 3) {
					++i10;
				}

				if(worldObj.isBlockNormalCube(i9, y, i10)) {
					i11 += 2;
				}

				int i12 = this.getMaxCurrentStrength(worldObj, i9, y, i10, -1);
				if(i12 >= 0 && i12 != i6) {
					this.updateAndPropagateCurrentStrength(worldObj, i9, y, i10);
				}

				i12 = this.getMaxCurrentStrength(worldObj, i9, i11, i10, -1);
				if(i12 >= 0 && i12 != i6) {
					this.updateAndPropagateCurrentStrength(worldObj, i9, i11, i10);
				}
			}

			if(i5 == 0 || i6 == 0) {
				worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
				worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
				worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
				worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
				worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
				worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
				worldObj.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			}
		}

	}

	private void notifyWireNeighborsOfNeighborChange(World worldObj, int x, int y, int z) {
		if(worldObj.getBlockId(x, y, z) == this.blockID) {
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		}
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		super.onBlockAdded(worldObj, x, y, z);
		this.updateAndPropagateCurrentStrength(worldObj, x, y, z);
		worldObj.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x - 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x + 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x, y, z - 1);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x, y, z + 1);
		if(worldObj.isBlockNormalCube(x - 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x - 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x - 1, y - 1, z);
		}

		if(worldObj.isBlockNormalCube(x + 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x + 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x + 1, y - 1, z);
		}

		if(worldObj.isBlockNormalCube(x, y, z - 1)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y + 1, z - 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y - 1, z - 1);
		}

		if(worldObj.isBlockNormalCube(x, y, z + 1)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y + 1, z + 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y - 1, z + 1);
		}

	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		super.onBlockRemoval(worldObj, x, y, z);
		worldObj.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		this.updateAndPropagateCurrentStrength(worldObj, x, y, z);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x - 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x + 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x, y, z - 1);
		this.notifyWireNeighborsOfNeighborChange(worldObj, x, y, z + 1);
		if(worldObj.isBlockNormalCube(x - 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x - 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x - 1, y - 1, z);
		}

		if(worldObj.isBlockNormalCube(x + 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x + 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x + 1, y - 1, z);
		}

		if(worldObj.isBlockNormalCube(x, y, z - 1)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y + 1, z - 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y - 1, z - 1);
		}

		if(worldObj.isBlockNormalCube(x, y, z + 1)) {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y + 1, z + 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(worldObj, x, y - 1, z + 1);
		}

	}

	private int getMaxCurrentStrength(World worldObj, int x, int y, int z, int i5) {
		if(worldObj.getBlockId(x, y, z) != this.blockID) {
			return i5;
		} else {
			int i6 = worldObj.getBlockMetadata(x, y, z);
			return i6 > i5 ? i6 : i5;
		}
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		boolean z7 = this.canPlaceBlockAt(worldObj, x, y, z);
		if(!z7) {
			this.dropBlockAsItem(worldObj, x, y, z, i6);
			worldObj.setBlockWithNotify(x, y, z, 0);
		} else {
			this.updateAndPropagateCurrentStrength(worldObj, x, y, z);
		}

		super.onNeighborBlockChange(worldObj, x, y, z, id);
	}

	public int idDropped(int metadata, Random rand) {
		return Item.redstone.shiftedIndex;
	}

	public boolean isIndirectlyPoweringTo(World worldObj, int x, int y, int z, int side) {
		return !this.wiresProvidePower ? false : this.isPoweringTo(worldObj, x, y, z, side);
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		if(!this.wiresProvidePower) {
			return false;
		} else if(blockAccess.getBlockMetadata(x, y, z) == 0) {
			return false;
		} else if(metadata == 1) {
			return true;
		} else {
			boolean z6 = isPowerProviderOrWire(blockAccess, x - 1, y, z) || !blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y - 1, z);
			boolean z7 = isPowerProviderOrWire(blockAccess, x + 1, y, z) || !blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y - 1, z);
			boolean z8 = isPowerProviderOrWire(blockAccess, x, y, z - 1) || !blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z - 1);
			boolean z9 = isPowerProviderOrWire(blockAccess, x, y, z + 1) || !blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z + 1);
			if(!blockAccess.isBlockNormalCube(x, y + 1, z)) {
				if(blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y + 1, z)) {
					z6 = true;
				}

				if(blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y + 1, z)) {
					z7 = true;
				}

				if(blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z - 1)) {
					z8 = true;
				}

				if(blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z + 1)) {
					z9 = true;
				}
			}

			return !z8 && !z7 && !z6 && !z9 && metadata >= 2 && metadata <= 5 ? true : (metadata == 2 && z8 && !z6 && !z7 ? true : (metadata == 3 && z9 && !z6 && !z7 ? true : (metadata == 4 && z6 && !z8 && !z9 ? true : metadata == 5 && z7 && !z8 && !z9)));
		}
	}

	public boolean canProvidePower() {
		return this.wiresProvidePower;
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getBlockMetadata(x, y, z) > 0) {
			double d6 = (double)x + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
			double d8 = (double)((float)y + 0.0625F);
			double d10 = (double)z + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
			worldObj.spawnParticle("reddust", d6, d8, d10, 0.0D, 0.0D, 0.0D);
		}

	}

	public static boolean isPowerProviderOrWire(IBlockAccess blockAccess, int x, int y, int z) {
		int i4 = blockAccess.getBlockId(x, y, z);
		return i4 == Block.redstoneWire.blockID ? true : (i4 == 0 ? false : Block.blocksList[i4].canProvidePower());
	}
}
