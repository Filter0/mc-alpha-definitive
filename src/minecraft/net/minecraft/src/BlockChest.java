package net.minecraft.src;

import java.util.Random;

public class BlockChest extends BlockContainer {
	private Random random = new Random();

	protected BlockChest(int id) {
		super(id, Material.wood);
		this.blockIndexInTexture = 26;
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(side == 1) {
			return this.blockIndexInTexture - 1;
		} else if(side == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int i6 = blockAccess.getBlockId(x, y, z - 1);
			int i7 = blockAccess.getBlockId(x, y, z + 1);
			int i8 = blockAccess.getBlockId(x - 1, y, z);
			int i9 = blockAccess.getBlockId(x + 1, y, z);
			int i10;
			int i11;
			int i12;
			byte b13;
			if(i6 != this.blockID && i7 != this.blockID) {
				if(i8 != this.blockID && i9 != this.blockID) {
					byte b14 = 3;
					if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i7]) {
						b14 = 3;
					}

					if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i6]) {
						b14 = 2;
					}

					if(Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i9]) {
						b14 = 5;
					}

					if(Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[i8]) {
						b14 = 4;
					}

					return side == b14 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
				} else if(side != 4 && side != 5) {
					i10 = 0;
					if(i8 == this.blockID) {
						i10 = -1;
					}

					i11 = blockAccess.getBlockId(i8 == this.blockID ? x - 1 : x + 1, y, z - 1);
					i12 = blockAccess.getBlockId(i8 == this.blockID ? x - 1 : x + 1, y, z + 1);
					if(side == 3) {
						i10 = -1 - i10;
					}

					b13 = 3;
					if((Block.opaqueCubeLookup[i6] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i12]) {
						b13 = 3;
					}

					if((Block.opaqueCubeLookup[i7] || Block.opaqueCubeLookup[i12]) && !Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i11]) {
						b13 = 2;
					}

					return (side == b13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(side != 2 && side != 3) {
				i10 = 0;
				if(i6 == this.blockID) {
					i10 = -1;
				}

				i11 = blockAccess.getBlockId(x - 1, y, i6 == this.blockID ? z - 1 : z + 1);
				i12 = blockAccess.getBlockId(x + 1, y, i6 == this.blockID ? z - 1 : z + 1);
				if(side == 4) {
					i10 = -1 - i10;
				}

				b13 = 5;
				if((Block.opaqueCubeLookup[i8] || Block.opaqueCubeLookup[i11]) && !Block.opaqueCubeLookup[i9] && !Block.opaqueCubeLookup[i12]) {
					b13 = 5;
				}

				if((Block.opaqueCubeLookup[i9] || Block.opaqueCubeLookup[i12]) && !Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i11]) {
					b13 = 4;
				}

				return (side == b13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + i10;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? this.blockIndexInTexture - 1 : (side == 0 ? this.blockIndexInTexture - 1 : (side == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int i5 = 0;
		if(world.getBlockId(x - 1, y, z) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(x + 1, y, z) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(x, y, z - 1) == this.blockID) {
			++i5;
		}

		if(world.getBlockId(x, y, z + 1) == this.blockID) {
			++i5;
		}

		return i5 > 1 ? false : (this.isThereANeighborChest(world, x - 1, y, z) ? false : (this.isThereANeighborChest(world, x + 1, y, z) ? false : (this.isThereANeighborChest(world, x, y, z - 1) ? false : !this.isThereANeighborChest(world, x, y, z + 1))));
	}

	private boolean isThereANeighborChest(World worldObj, int x, int y, int z) {
		return worldObj.getBlockId(x, y, z) != this.blockID ? false : (worldObj.getBlockId(x - 1, y, z) == this.blockID ? true : (worldObj.getBlockId(x + 1, y, z) == this.blockID ? true : (worldObj.getBlockId(x, y, z - 1) == this.blockID ? true : worldObj.getBlockId(x, y, z + 1) == this.blockID)));
	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		TileEntityChest tileEntityChest5 = (TileEntityChest)worldObj.getBlockTileEntity(x, y, z);

		for(int i6 = 0; i6 < tileEntityChest5.getSizeInventory(); ++i6) {
			ItemStack itemStack7 = tileEntityChest5.getStackInSlot(i6);
			if(itemStack7 != null) {
				float f8 = this.random.nextFloat() * 0.8F + 0.1F;
				float f9 = this.random.nextFloat() * 0.8F + 0.1F;
				float f10 = this.random.nextFloat() * 0.8F + 0.1F;

				while(itemStack7.stackSize > 0) {
					int i11 = this.random.nextInt(21) + 10;
					if(i11 > itemStack7.stackSize) {
						i11 = itemStack7.stackSize;
					}

					itemStack7.stackSize -= i11;
					EntityItem entityItem12 = new EntityItem(worldObj, (double)((float)x + f8), (double)((float)y + f9), (double)((float)z + f10), new ItemStack(itemStack7.itemID, i11, itemStack7.itemDmg));
					float f13 = 0.05F;
					entityItem12.motionX = (double)((float)this.random.nextGaussian() * f13);
					entityItem12.motionY = (double)((float)this.random.nextGaussian() * f13 + 0.2F);
					entityItem12.motionZ = (double)((float)this.random.nextGaussian() * f13);
					worldObj.spawnEntityInWorld(entityItem12);
				}
			}
		}

		super.onBlockRemoval(worldObj, x, y, z);
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		Object object6 = (TileEntityChest)worldObj.getBlockTileEntity(x, y, z);
		if(worldObj.isBlockNormalCube(x, y + 1, z)) {
			return true;
		} else if(worldObj.getBlockId(x - 1, y, z) == this.blockID && worldObj.isBlockNormalCube(x - 1, y + 1, z)) {
			return true;
		} else if(worldObj.getBlockId(x + 1, y, z) == this.blockID && worldObj.isBlockNormalCube(x + 1, y + 1, z)) {
			return true;
		} else if(worldObj.getBlockId(x, y, z - 1) == this.blockID && worldObj.isBlockNormalCube(x, y + 1, z - 1)) {
			return true;
		} else if(worldObj.getBlockId(x, y, z + 1) == this.blockID && worldObj.isBlockNormalCube(x, y + 1, z + 1)) {
			return true;
		} else {
			if(worldObj.getBlockId(x - 1, y, z) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)worldObj.getBlockTileEntity(x - 1, y, z), (IInventory)object6);
			}

			if(worldObj.getBlockId(x + 1, y, z) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)worldObj.getBlockTileEntity(x + 1, y, z));
			}

			if(worldObj.getBlockId(x, y, z - 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)worldObj.getBlockTileEntity(x, y, z - 1), (IInventory)object6);
			}

			if(worldObj.getBlockId(x, y, z + 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)worldObj.getBlockTileEntity(x, y, z + 1));
			}

			entityPlayer.displayGUIChest((IInventory)object6);
			return true;
		}
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}
