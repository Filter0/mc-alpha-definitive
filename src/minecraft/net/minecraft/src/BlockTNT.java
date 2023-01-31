package net.minecraft.src;

import java.util.Random;

public class BlockTNT extends Block {
	public BlockTNT(int id, int tex) {
		super(id, tex, Material.tnt);
	}

	public int getBlockTextureFromSide(int side) {
		return side == 0 ? this.blockIndexInTexture + 2 : (side == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(id > 0 && Block.blocksList[id].canProvidePower() && worldObj.isBlockIndirectlyGettingPowered(x, y, z)) {
			this.onBlockDestroyedByPlayer(worldObj, x, y, z, 0);
			worldObj.setBlockWithNotify(x, y, z, 0);
		}

	}

	public int quantityDropped(Random rand) {
		return 0;
	}

	public void onBlockDestroyedByExplosion(World worldObj, int x, int y, int z) {
		EntityTNTPrimed entityTNTPrimed5 = new EntityTNTPrimed(worldObj, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		entityTNTPrimed5.fuse = worldObj.rand.nextInt(entityTNTPrimed5.fuse / 4) + entityTNTPrimed5.fuse / 8;
		worldObj.spawnEntityInWorld(entityTNTPrimed5);
	}

	public void onBlockDestroyedByPlayer(World worldObj, int x, int y, int z, int metadata) {
		EntityTNTPrimed entityTNTPrimed6 = new EntityTNTPrimed(worldObj, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		worldObj.spawnEntityInWorld(entityTNTPrimed6);
		worldObj.playSoundAtEntity(entityTNTPrimed6, "random.fuse", 1.0F, 1.0F);
	}
}
