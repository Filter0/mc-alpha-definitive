package net.minecraft.src;

import java.util.Random;

public class WorldGenLiquids extends WorldGenerator {
	private int liquidBlockId;

	public WorldGenLiquids(int liquidBlockID) {
		this.liquidBlockId = liquidBlockID;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(world.getBlockId(x, y + 1, z) != Block.stone.blockID) {
			return false;
		} else if(world.getBlockId(x, y - 1, z) != Block.stone.blockID) {
			return false;
		} else if(world.getBlockId(x, y, z) != 0 && world.getBlockId(x, y, z) != Block.stone.blockID) {
			return false;
		} else {
			int i6 = 0;
			if(world.getBlockId(x - 1, y, z) == Block.stone.blockID) {
				++i6;
			}

			if(world.getBlockId(x + 1, y, z) == Block.stone.blockID) {
				++i6;
			}

			if(world.getBlockId(x, y, z - 1) == Block.stone.blockID) {
				++i6;
			}

			if(world.getBlockId(x, y, z + 1) == Block.stone.blockID) {
				++i6;
			}

			int i7 = 0;
			if(world.getBlockId(x - 1, y, z) == 0) {
				++i7;
			}

			if(world.getBlockId(x + 1, y, z) == 0) {
				++i7;
			}

			if(world.getBlockId(x, y, z - 1) == 0) {
				++i7;
			}

			if(world.getBlockId(x, y, z + 1) == 0) {
				++i7;
			}

			if(i6 == 3 && i7 == 1) {
				world.setBlockWithNotify(x, y, z, this.liquidBlockId);
			}

			return true;
		}
	}
}
