package net.minecraft.src;

import java.util.Random;

public class BlockSand extends Block {
	public static boolean fallInstantly = false;

	public BlockSand(int id, int tex) {
		super(id, tex, Material.sand);
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		this.tryToFall(worldObj, x, y, z);
	}

	private void tryToFall(World worldObj, int x, int y, int z) {
		if(canFallBelow(worldObj, x, y - 1, z) && y >= 0) {
			EntityFallingSand entityFallingSand8 = new EntityFallingSand(worldObj, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.blockID);
			if(fallInstantly) {
				while(!entityFallingSand8.isDead) {
					entityFallingSand8.onUpdate();
				}
			} else {
				worldObj.spawnEntityInWorld(entityFallingSand8);
			}
		}

	}

	public int tickRate() {
		return 3;
	}

	public static boolean canFallBelow(World worldObj, int x, int y, int z) {
		int i4 = worldObj.getBlockId(x, y, z);
		if(i4 == 0) {
			return true;
		} else if(i4 == Block.fire.blockID) {
			return true;
		} else {
			Material material5 = Block.blocksList[i4].material;
			return material5 == Material.water ? true : material5 == Material.lava;
		}
	}
}
