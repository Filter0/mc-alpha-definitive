package net.minecraft.src;

import java.util.Random;

public class WorldGenFlowers extends WorldGenerator {
	private int plantBlockId;

	public WorldGenFlowers(int plantBlockID) {
		this.plantBlockId = plantBlockID;
	}

	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int i6 = 0; i6 < 64; ++i6) {
			int i7 = x + rand.nextInt(8) - rand.nextInt(8);
			int i8 = y + rand.nextInt(4) - rand.nextInt(4);
			int i9 = z + rand.nextInt(8) - rand.nextInt(8);
			if(world.getBlockId(i7, i8, i9) == 0 && ((BlockFlower)Block.blocksList[this.plantBlockId]).canBlockStay(world, i7, i8, i9)) {
				world.setBlock(i7, i8, i9, this.plantBlockId);
			}
		}

		return true;
	}
}
