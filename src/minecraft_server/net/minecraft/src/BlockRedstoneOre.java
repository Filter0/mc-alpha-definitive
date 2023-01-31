package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneOre extends Block {
	private boolean glowing;

	public BlockRedstoneOre(int id, int blockIndex, boolean glowing) {
		super(id, blockIndex, Material.rock);
		if(glowing) {
			this.setTickOnLoad(true);
		}

		this.glowing = glowing;
	}

	public int tickRate() {
		return 30;
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.glow(world, x, y, z);
		super.onBlockClicked(world, x, y, z, entityPlayer);
	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		this.glow(world, x, y, z);
		super.onEntityWalking(world, x, y, z, entity);
	}

	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		this.glow(world, x, y, z);
		return super.blockActivated(world, x, y, z, entityPlayer);
	}

	private void glow(World world, int x, int y, int z) {
		this.sparkle(world, x, y, z);
		if(this.blockID == Block.oreRedstone.blockID) {
			world.setBlockWithNotify(x, y, z, Block.oreRedstoneGlowing.blockID);
		}

	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		if(this.blockID == Block.oreRedstoneGlowing.blockID) {
			world.setBlockWithNotify(x, y, z, Block.oreRedstone.blockID);
		}

	}

	public int idDropped(int count, Random random) {
		return Item.redstone.shiftedIndex;
	}

	public int quantityDropped(Random random) {
		return 4 + random.nextInt(2);
	}

	private void sparkle(World world, int x, int y, int z) {
		Random random5 = world.rand;
		double d6 = 0.0625D;

		for(int i8 = 0; i8 < 6; ++i8) {
			double d9 = (double)((float)x + random5.nextFloat());
			double d11 = (double)((float)y + random5.nextFloat());
			double d13 = (double)((float)z + random5.nextFloat());
			if(i8 == 0 && !world.isBlockNormalCube(x, y + 1, z)) {
				d11 = (double)(y + 1) + d6;
			}

			if(i8 == 1 && !world.isBlockNormalCube(x, y - 1, z)) {
				d11 = (double)(y + 0) - d6;
			}

			if(i8 == 2 && !world.isBlockNormalCube(x, y, z + 1)) {
				d13 = (double)(z + 1) + d6;
			}

			if(i8 == 3 && !world.isBlockNormalCube(x, y, z - 1)) {
				d13 = (double)(z + 0) - d6;
			}

			if(i8 == 4 && !world.isBlockNormalCube(x + 1, y, z)) {
				d9 = (double)(x + 1) + d6;
			}

			if(i8 == 5 && !world.isBlockNormalCube(x - 1, y, z)) {
				d9 = (double)(x + 0) - d6;
			}

			if(d9 < (double)x || d9 > (double)(x + 1) || d11 < 0.0D || d11 > (double)(y + 1) || d13 < (double)z || d13 > (double)(z + 1)) {
				world.spawnParticle("reddust", d9, d11, d13, 0.0D, 0.0D, 0.0D);
			}
		}

	}
}
