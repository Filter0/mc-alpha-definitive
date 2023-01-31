package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneWire extends Block {
	private boolean wiresProvidePower = true;

	public BlockRedstoneWire(int id, int blockIndex) {
		super(id, blockIndex, Material.circuits);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 5;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z);
	}

	private void updateAndPropagateCurrentStrength(World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
		int i6 = 0;
		this.wiresProvidePower = false;
		boolean z7 = world.isBlockIndirectlyGettingPowered(x, y, z);
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

				i6 = this.getMaxCurrentStrength(world, i9, y, i10, i6);
				if(world.isBlockNormalCube(i9, y, i10) && !world.isBlockNormalCube(x, y + 1, z)) {
					i6 = this.getMaxCurrentStrength(world, i9, y + 1, i10, i6);
				} else if(!world.isBlockNormalCube(i9, y, i10)) {
					i6 = this.getMaxCurrentStrength(world, i9, y - 1, i10, i6);
				}
			}

			if(i6 > 0) {
				--i6;
			} else {
				i6 = 0;
			}
		}

		if(i5 != i6) {
			world.setBlockMetadataWithNotify(x, y, z, i6);
			world.markBlocksDirty(x, y, z, x, y, z);
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

				if(world.isBlockNormalCube(i9, y, i10)) {
					i11 += 2;
				}

				int i12 = this.getMaxCurrentStrength(world, i9, y, i10, -1);
				if(i12 >= 0 && i12 != i6) {
					this.updateAndPropagateCurrentStrength(world, i9, y, i10);
				}

				i12 = this.getMaxCurrentStrength(world, i9, i11, i10, -1);
				if(i12 >= 0 && i12 != i6) {
					this.updateAndPropagateCurrentStrength(world, i9, i11, i10);
				}
			}

			if(i5 == 0 || i6 == 0) {
				world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
				world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			}
		}

	}

	private void notifyWireNeighborsOfNeighborChange(World world, int x, int y, int z) {
		if(world.getBlockId(x, y, z) == this.blockID) {
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.updateAndPropagateCurrentStrength(world, x, y, z);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
		if(world.isBlockNormalCube(x - 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x + 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x, y, z - 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
		}

		if(world.isBlockNormalCube(x, y, z + 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
		}

	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		super.onBlockRemoval(world, x, y, z);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		this.updateAndPropagateCurrentStrength(world, x, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
		this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
		if(world.isBlockNormalCube(x - 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x + 1, y, z)) {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
		}

		if(world.isBlockNormalCube(x, y, z - 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
		}

		if(world.isBlockNormalCube(x, y, z + 1)) {
			this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
		} else {
			this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
		}

	}

	private int getMaxCurrentStrength(World world, int x, int y, int z, int i5) {
		if(world.getBlockId(x, y, z) != this.blockID) {
			return i5;
		} else {
			int i6 = world.getBlockMetadata(x, y, z);
			return i6 > i5 ? i6 : i5;
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		int i6 = world.getBlockMetadata(x, y, z);
		boolean z7 = this.canPlaceBlockAt(world, x, y, z);
		if(!z7) {
			this.dropBlockAsItem(world, x, y, z, i6);
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			this.updateAndPropagateCurrentStrength(world, x, y, z);
		}

		super.onNeighborBlockChange(world, x, y, z, flag);
	}

	public int idDropped(int count, Random random) {
		return Item.redstone.shiftedIndex;
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		return !this.wiresProvidePower ? false : this.isPoweringTo(world, x, y, z, flag);
	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		if(!this.wiresProvidePower) {
			return false;
		} else if(blockAccess.getBlockMetadata(x, y, z) == 0) {
			return false;
		} else if(unused == 1) {
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

			return !z8 && !z7 && !z6 && !z9 && unused >= 2 && unused <= 5 ? true : (unused == 2 && z8 && !z6 && !z7 ? true : (unused == 3 && z9 && !z6 && !z7 ? true : (unused == 4 && z6 && !z8 && !z9 ? true : unused == 5 && z7 && !z8 && !z9)));
		}
	}

	public boolean canProvidePower() {
		return this.wiresProvidePower;
	}

	public static boolean isPowerProviderOrWire(IBlockAccess blockAccess, int x, int y, int z) {
		int i4 = blockAccess.getBlockId(x, y, z);
		return i4 == Block.redstoneWire.blockID ? true : (i4 == 0 ? false : Block.blocksList[i4].canProvidePower());
	}
}
