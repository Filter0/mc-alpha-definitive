package net.minecraft.src;

import java.util.Random;

public class BlockChest extends BlockContainer {
	private Random random = new Random();

	protected BlockChest(int id) {
		super(id, Material.wood);
		this.blockIndexInTexture = 26;
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

	private boolean isThereANeighborChest(World world, int x, int y, int z) {
		return world.getBlockId(x, y, z) != this.blockID ? false : (world.getBlockId(x - 1, y, z) == this.blockID ? true : (world.getBlockId(x + 1, y, z) == this.blockID ? true : (world.getBlockId(x, y, z - 1) == this.blockID ? true : world.getBlockId(x, y, z + 1) == this.blockID)));
	}

	public void onBlockRemoval(World world, int x, int y, int z) {
		TileEntityChest tileEntityChest5 = (TileEntityChest)world.getBlockTileEntity(x, y, z);

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
					EntityItem entityItem12 = new EntityItem(world, (double)((float)x + f8), (double)((float)y + f9), (double)((float)z + f10), new ItemStack(itemStack7.itemID, i11, itemStack7.itemDmg));
					float f13 = 0.05F;
					entityItem12.motionX = (double)((float)this.random.nextGaussian() * f13);
					entityItem12.motionY = (double)((float)this.random.nextGaussian() * f13 + 0.2F);
					entityItem12.motionZ = (double)((float)this.random.nextGaussian() * f13);
					world.spawnEntityInWorld(entityItem12);
				}
			}
		}

		super.onBlockRemoval(world, x, y, z);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		Object object6 = (TileEntityChest)world.getBlockTileEntity(x, y, z);
		if(world.isBlockNormalCube(x, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x - 1, y, z) == this.blockID && world.isBlockNormalCube(x - 1, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x + 1, y, z) == this.blockID && world.isBlockNormalCube(x + 1, y + 1, z)) {
			return true;
		} else if(world.getBlockId(x, y, z - 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z - 1)) {
			return true;
		} else if(world.getBlockId(x, y, z + 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z + 1)) {
			return true;
		} else {
			if(world.getBlockId(x - 1, y, z) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x - 1, y, z), (IInventory)object6);
			}

			if(world.getBlockId(x + 1, y, z) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world.getBlockTileEntity(x + 1, y, z));
			}

			if(world.getBlockId(x, y, z - 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x, y, z - 1), (IInventory)object6);
			}

			if(world.getBlockId(x, y, z + 1) == this.blockID) {
				object6 = new InventoryLargeChest("Large chest", (IInventory)object6, (TileEntityChest)world.getBlockTileEntity(x, y, z + 1));
			}

			entityPlayer.displayGUIChest((IInventory)object6);
			return true;
		}
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityChest();
	}
}
