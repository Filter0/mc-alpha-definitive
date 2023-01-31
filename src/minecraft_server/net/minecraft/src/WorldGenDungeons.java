package net.minecraft.src;

import java.util.Random;

public class WorldGenDungeons extends WorldGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		byte b6 = 3;
		int i7 = rand.nextInt(2) + 2;
		int i8 = rand.nextInt(2) + 2;
		int i9 = 0;

		int i10;
		int i11;
		int i12;
		for(i10 = x - i7 - 1; i10 <= x + i7 + 1; ++i10) {
			for(i11 = y - 1; i11 <= y + b6 + 1; ++i11) {
				for(i12 = z - i8 - 1; i12 <= z + i8 + 1; ++i12) {
					Material material13 = world.getBlockMaterial(i10, i11, i12);
					if(i11 == y - 1 && !material13.isSolid()) {
						return false;
					}

					if(i11 == y + b6 + 1 && !material13.isSolid()) {
						return false;
					}

					if((i10 == x - i7 - 1 || i10 == x + i7 + 1 || i12 == z - i8 - 1 || i12 == z + i8 + 1) && i11 == y && world.getBlockId(i10, i11, i12) == 0 && world.getBlockId(i10, i11 + 1, i12) == 0) {
						++i9;
					}
				}
			}
		}

		if(i9 >= 1 && i9 <= 5) {
			for(i10 = x - i7 - 1; i10 <= x + i7 + 1; ++i10) {
				for(i11 = y + b6; i11 >= y - 1; --i11) {
					for(i12 = z - i8 - 1; i12 <= z + i8 + 1; ++i12) {
						if(i10 != x - i7 - 1 && i11 != y - 1 && i12 != z - i8 - 1 && i10 != x + i7 + 1 && i11 != y + b6 + 1 && i12 != z + i8 + 1) {
							world.setBlockWithNotify(i10, i11, i12, 0);
						} else if(i11 >= 0 && !world.getBlockMaterial(i10, i11 - 1, i12).isSolid()) {
							world.setBlockWithNotify(i10, i11, i12, 0);
						} else if(world.getBlockMaterial(i10, i11, i12).isSolid()) {
							if(i11 == y - 1 && rand.nextInt(4) != 0) {
								world.setBlockWithNotify(i10, i11, i12, Block.cobblestoneMossy.blockID);
							} else {
								world.setBlockWithNotify(i10, i11, i12, Block.cobblestone.blockID);
							}
						}
					}
				}
			}

			label110:
			for(i10 = 0; i10 < 2; ++i10) {
				for(i11 = 0; i11 < 3; ++i11) {
					i12 = x + rand.nextInt(i7 * 2 + 1) - i7;
					int i14 = z + rand.nextInt(i8 * 2 + 1) - i8;
					if(world.getBlockId(i12, y, i14) == 0) {
						int i15 = 0;
						if(world.getBlockMaterial(i12 - 1, y, i14).isSolid()) {
							++i15;
						}

						if(world.getBlockMaterial(i12 + 1, y, i14).isSolid()) {
							++i15;
						}

						if(world.getBlockMaterial(i12, y, i14 - 1).isSolid()) {
							++i15;
						}

						if(world.getBlockMaterial(i12, y, i14 + 1).isSolid()) {
							++i15;
						}

						if(i15 == 1) {
							world.setBlockWithNotify(i12, y, i14, Block.chest.blockID);
							TileEntityChest tileEntityChest16 = (TileEntityChest)world.getBlockTileEntity(i12, y, i14);
							int i17 = 0;

							while(true) {
								if(i17 >= 8) {
									continue label110;
								}

								ItemStack itemStack18 = this.pickCheckLootItem(rand);
								if(itemStack18 != null) {
									tileEntityChest16.setInventorySlotContents(rand.nextInt(tileEntityChest16.getSizeInventory()), itemStack18);
								}

								++i17;
							}
						}
					}
				}
			}

			world.setBlockWithNotify(x, y, z, Block.mobSpawner.blockID);
			TileEntityMobSpawner tileEntityMobSpawner19 = (TileEntityMobSpawner)world.getBlockTileEntity(x, y, z);
			tileEntityMobSpawner19.mobID = this.pickMobSpawner(rand);
			return true;
		} else {
			return false;
		}
	}

	private ItemStack pickCheckLootItem(Random random) {
		int i2 = random.nextInt(11);
		return i2 == 0 ? new ItemStack(Item.saddle) : (i2 == 1 ? new ItemStack(Item.ingotIron, random.nextInt(4) + 1) : (i2 == 2 ? new ItemStack(Item.bread) : (i2 == 3 ? new ItemStack(Item.wheat, random.nextInt(4) + 1) : (i2 == 4 ? new ItemStack(Item.gunpowder, random.nextInt(4) + 1) : (i2 == 5 ? new ItemStack(Item.silk, random.nextInt(4) + 1) : (i2 == 6 ? new ItemStack(Item.bucketEmpty) : (i2 == 7 && random.nextInt(100) == 0 ? new ItemStack(Item.appleGold) : (i2 == 8 && random.nextInt(2) == 0 ? new ItemStack(Item.redstone, random.nextInt(4) + 1) : (i2 == 9 && random.nextInt(10) == 0 ? new ItemStack(Item.itemsList[Item.record13.shiftedIndex + random.nextInt(2)]) : null)))))))));
	}

	private String pickMobSpawner(Random random) {
		int i2 = random.nextInt(4);
		return i2 == 0 ? "Skeleton" : (i2 == 1 ? "Zombie" : (i2 == 2 ? "Zombie" : (i2 == 3 ? "Spider" : "")));
	}
}
