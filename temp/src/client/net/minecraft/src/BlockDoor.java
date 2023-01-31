package net.minecraft.src;

import java.util.Random;

public class BlockDoor extends Block {
	protected BlockDoor(int i1, Material material2) {
		super(i1, material2);
		this.blockIndexInTexture = 97;
		if(material2 == Material.iron) {
			++this.blockIndexInTexture;
		}

		float f3 = 0.5F;
		float f4 = 1.0F;
		this.setBlockBounds(0.5F - f3, 0.0F, 0.5F - f3, 0.5F + f3, f4, 0.5F + f3);
	}

	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		if(side != 0 && side != 1) {
			int i3 = this.getState(metadata);
			if((i3 == 0 || i3 == 2) ^ side <= 3) {
				return this.blockIndexInTexture;
			} else {
				int i4 = i3 / 2 + (side & 1 ^ i3);
				i4 += (metadata & 4) / 4;
				int i5 = this.blockIndexInTexture - (metadata & 8) * 2;
				if((i4 & 1) != 0) {
					i5 = -i5;
				}

				return i5;
			}
		} else {
			return this.blockIndexInTexture;
		}
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 7;
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(worldObj, x, y, z);
		return super.getSelectedBoundingBoxFromPool(worldObj, x, y, z);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		this.setBlockBoundsBasedOnState(worldObj, x, y, z);
		return super.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		this.setDoorRotation(this.getState(blockAccess.getBlockMetadata(x, y, z)));
	}

	public void setDoorRotation(int metadata) {
		float f2 = 0.1875F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
		if(metadata == 0) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f2);
		}

		if(metadata == 1) {
			this.setBlockBounds(1.0F - f2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

		if(metadata == 2) {
			this.setBlockBounds(0.0F, 0.0F, 1.0F - f2, 1.0F, 1.0F, 1.0F);
		}

		if(metadata == 3) {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, f2, 1.0F, 1.0F);
		}

	}

	public void onBlockClicked(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		this.blockActivated(worldObj, x, y, z, entityPlayer);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		if(this.material == Material.iron) {
			return true;
		} else {
			int i6 = worldObj.getBlockMetadata(x, y, z);
			if((i6 & 8) != 0) {
				if(worldObj.getBlockId(x, y - 1, z) == this.blockID) {
					this.blockActivated(worldObj, x, y - 1, z, entityPlayer);
				}

				return true;
			} else {
				if(worldObj.getBlockId(x, y + 1, z) == this.blockID) {
					worldObj.setBlockMetadataWithNotify(x, y + 1, z, (i6 ^ 4) + 8);
				}

				worldObj.setBlockMetadataWithNotify(x, y, z, i6 ^ 4);
				worldObj.markBlocksDirty(x, y - 1, z, x, y, z);
				if(Math.random() < 0.5D) {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				}

				return true;
			}
		}
	}

	public void onPoweredBlockChange(World worldObj, int x, int y, int z, boolean z5) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if((i6 & 8) != 0) {
			if(worldObj.getBlockId(x, y - 1, z) == this.blockID) {
				this.onPoweredBlockChange(worldObj, x, y - 1, z, z5);
			}

		} else {
			boolean z7 = (worldObj.getBlockMetadata(x, y, z) & 4) > 0;
			if(z7 != z5) {
				if(worldObj.getBlockId(x, y + 1, z) == this.blockID) {
					worldObj.setBlockMetadataWithNotify(x, y + 1, z, (i6 ^ 4) + 8);
				}

				worldObj.setBlockMetadataWithNotify(x, y, z, i6 ^ 4);
				worldObj.markBlocksDirty(x, y - 1, z, x, y, z);
				if(Math.random() < 0.5D) {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_open", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.door_close", 1.0F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
				}

			}
		}
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		int i6 = worldObj.getBlockMetadata(x, y, z);
		if((i6 & 8) != 0) {
			if(worldObj.getBlockId(x, y - 1, z) != this.blockID) {
				worldObj.setBlockWithNotify(x, y, z, 0);
			}

			if(id > 0 && Block.blocksList[id].canProvidePower()) {
				this.onNeighborBlockChange(worldObj, x, y - 1, z, id);
			}
		} else {
			boolean z7 = false;
			if(worldObj.getBlockId(x, y + 1, z) != this.blockID) {
				worldObj.setBlockWithNotify(x, y, z, 0);
				z7 = true;
			}

			if(!worldObj.isBlockNormalCube(x, y - 1, z)) {
				worldObj.setBlockWithNotify(x, y, z, 0);
				z7 = true;
				if(worldObj.getBlockId(x, y + 1, z) == this.blockID) {
					worldObj.setBlockWithNotify(x, y + 1, z, 0);
				}
			}

			if(z7) {
				this.dropBlockAsItem(worldObj, x, y, z, i6);
			} else if(id > 0 && Block.blocksList[id].canProvidePower()) {
				boolean z8 = worldObj.isBlockIndirectlyGettingPowered(x, y, z) || worldObj.isBlockIndirectlyGettingPowered(x, y + 1, z);
				this.onPoweredBlockChange(worldObj, x, y, z, z8);
			}
		}

	}

	public int idDropped(int metadata, Random rand) {
		return (metadata & 8) != 0 ? 0 : (this.material == Material.iron ? Item.doorSteel.shiftedIndex : Item.doorWood.shiftedIndex);
	}

	public MovingObjectPosition collisionRayTrace(World worldObj, int x, int y, int z, Vec3D vector1, Vec3D vector2) {
		this.setBlockBoundsBasedOnState(worldObj, x, y, z);
		return super.collisionRayTrace(worldObj, x, y, z, vector1, vector2);
	}

	public int getState(int metadata) {
		return (metadata & 4) == 0 ? metadata - 1 & 3 : metadata & 3;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return y >= 127 ? false : world.isBlockNormalCube(x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
	}
}
