package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRedstoneTorch extends BlockTorch {
	private boolean torchActive = false;
	private static List torchUpdates = new ArrayList();

	private boolean checkForBurnout(World world, int x, int y, int z, boolean z5) {
		if(z5) {
			torchUpdates.add(new RedstoneUpdateInfo(x, y, z, world.worldTime));
		}

		int i6 = 0;

		for(int i7 = 0; i7 < torchUpdates.size(); ++i7) {
			RedstoneUpdateInfo redstoneUpdateInfo8 = (RedstoneUpdateInfo)torchUpdates.get(i7);
			if(redstoneUpdateInfo8.x == x && redstoneUpdateInfo8.y == y && redstoneUpdateInfo8.z == z) {
				++i6;
				if(i6 >= 8) {
					return true;
				}
			}
		}

		return false;
	}

	protected BlockRedstoneTorch(int id, int blockIndex, boolean torchActive) {
		super(id, blockIndex);
		this.torchActive = torchActive;
		this.setTickOnLoad(true);
	}

	public int tickRate() {
		return 2;
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(world.getBlockMetadata(x, y, z) == 0) {
			super.onBlockAdded(world, x, y, z);
		}

		if(this.torchActive) {
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		}

	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		if(this.torchActive) {
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
			world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		}

	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int unused) {
		if(!this.torchActive) {
			return false;
		} else {
			int i6 = blockAccess.getBlockMetadata(x, y, z);
			return i6 == 5 && unused == 1 ? false : (i6 == 3 && unused == 3 ? false : (i6 == 4 && unused == 2 ? false : (i6 == 1 && unused == 5 ? false : i6 != 2 || unused != 4)));
		}
	}

	private boolean isIndirectlyPowered(World world, int x, int y, int z) {
		int i5 = world.getBlockMetadata(x, y, z);
		return i5 == 5 && world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) ? true : (i5 == 3 && world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) ? true : (i5 == 4 && world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) ? true : (i5 == 1 && world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) ? true : i5 == 2 && world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5))));
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		boolean z6 = this.isIndirectlyPowered(world, x, y, z);

		while(torchUpdates.size() > 0 && world.worldTime - ((RedstoneUpdateInfo)torchUpdates.get(0)).updateTime > 100L) {
			torchUpdates.remove(0);
		}

		if(this.torchActive) {
			if(z6) {
				world.setBlockAndMetadataWithNotify(x, y, z, Block.torchRedstoneIdle.blockID, world.getBlockMetadata(x, y, z));
				if(this.checkForBurnout(world, x, y, z, true)) {
					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

					for(int i7 = 0; i7 < 5; ++i7) {
						double d8 = (double)x + random.nextDouble() * 0.6D + 0.2D;
						double d10 = (double)y + random.nextDouble() * 0.6D + 0.2D;
						double d12 = (double)z + random.nextDouble() * 0.6D + 0.2D;
						world.spawnParticle("smoke", d8, d10, d12, 0.0D, 0.0D, 0.0D);
					}
				}
			}
		} else if(!z6 && !this.checkForBurnout(world, x, y, z, false)) {
			world.setBlockAndMetadataWithNotify(x, y, z, Block.torchRedstoneActive.blockID, world.getBlockMetadata(x, y, z));
		}

	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		super.onNeighborBlockChange(world, x, y, z, flag);
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int flag) {
		return flag == 0 ? this.isPoweringTo(world, x, y, z, flag) : false;
	}

	public int idDropped(int count, Random random) {
		return Block.torchRedstoneActive.blockID;
	}

	public boolean canProvidePower() {
		return true;
	}
}
