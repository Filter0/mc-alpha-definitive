package net.minecraft.src;

import java.util.Random;

public class BlockTorch extends Block {
	protected BlockTorch(int id, int tex) {
		super(id, tex, Material.circuits);
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

	public int getRenderType() {
		return 2;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x - 1, y, z) ? true : (world.isBlockNormalCube(x + 1, y, z) ? true : (world.isBlockNormalCube(x, y, z - 1) ? true : (world.isBlockNormalCube(x, y, z + 1) ? true : world.isBlockNormalCube(x, y - 1, z))));
	}

	public void onBlockPlaced(World worldObj, int x, int y, int z, int metadata) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if(metadata == 1 && worldObj.isBlockNormalCube(x, y - 1, z)) {
			i6 = 5;
		}

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

		worldObj.setBlockMetadataWithNotify(x, y, z, i6);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		super.updateTick(worldObj, x, y, z, rand);
		if(worldObj.getBlockMetadata(x, y, z) == 0) {
			this.onBlockAdded(worldObj, x, y, z);
		}

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
		} else if(worldObj.isBlockNormalCube(x, y - 1, z)) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 5);
		}

		this.checkIfAttachedToBlock(worldObj, x, y, z);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(this.checkIfAttachedToBlock(worldObj, x, y, z)) {
			int i6 = worldObj.getBlockMetadata(x, y, z);
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

			if(!worldObj.isBlockNormalCube(x, y - 1, z) && i6 == 5) {
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

	public MovingObjectPosition collisionRayTrace(World worldObj, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		int i7 = worldObj.getBlockMetadata(x, y, z) & 7;
		float f8 = 0.15F;
		if(i7 == 1) {
			this.setBlockBounds(0.0F, 0.2F, 0.5F - f8, f8 * 2.0F, 0.8F, 0.5F + f8);
		} else if(i7 == 2) {
			this.setBlockBounds(1.0F - f8 * 2.0F, 0.2F, 0.5F - f8, 1.0F, 0.8F, 0.5F + f8);
		} else if(i7 == 3) {
			this.setBlockBounds(0.5F - f8, 0.2F, 0.0F, 0.5F + f8, 0.8F, f8 * 2.0F);
		} else if(i7 == 4) {
			this.setBlockBounds(0.5F - f8, 0.2F, 1.0F - f8 * 2.0F, 0.5F + f8, 0.8F, 1.0F);
		} else {
			f8 = 0.1F;
			this.setBlockBounds(0.5F - f8, 0.0F, 0.5F - f8, 0.5F + f8, 0.6F, 0.5F + f8);
		}

		return super.collisionRayTrace(worldObj, x, y, z, vector1, vector2);
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		double d7 = (double)((float)x + 0.5F);
		double d9 = (double)((float)y + 0.7F);
		double d11 = (double)((float)z + 0.5F);
		double d13 = (double)0.22F;
		double d15 = (double)0.27F;
		if(i6 == 1) {
			worldObj.spawnParticle("smoke", d7 - d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", d7 - d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
		} else if(i6 == 2) {
			worldObj.spawnParticle("smoke", d7 + d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", d7 + d15, d9 + d13, d11, 0.0D, 0.0D, 0.0D);
		} else if(i6 == 3) {
			worldObj.spawnParticle("smoke", d7, d9 + d13, d11 - d15, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", d7, d9 + d13, d11 - d15, 0.0D, 0.0D, 0.0D);
		} else if(i6 == 4) {
			worldObj.spawnParticle("smoke", d7, d9 + d13, d11 + d15, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", d7, d9 + d13, d11 + d15, 0.0D, 0.0D, 0.0D);
		} else {
			worldObj.spawnParticle("smoke", d7, d9, d11, 0.0D, 0.0D, 0.0D);
			worldObj.spawnParticle("flame", d7, d9, d11, 0.0D, 0.0D, 0.0D);
		}

	}
}
