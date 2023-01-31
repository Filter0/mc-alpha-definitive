package net.minecraft.src;

import java.util.Random;

public class WorldGenCactus extends WorldGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int i6 = 0; i6 < 10; ++i6) {
			int i7 = x + rand.nextInt(8) - rand.nextInt(8);
			int i8 = y + rand.nextInt(4) - rand.nextInt(4);
			int i9 = z + rand.nextInt(8) - rand.nextInt(8);
			if(world.getBlockId(i7, i8, i9) == 0) {
				int i10 = 1 + rand.nextInt(rand.nextInt(3) + 1);

				for(int i11 = 0; i11 < i10; ++i11) {
					if(Block.cactus.canBlockStay(world, i7, i8 + i11, i9)) {
						world.setBlock(i7, i8 + i11, i9, Block.cactus.blockID);
					}
				}
			}
		}

		return true;
	}
}
