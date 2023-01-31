package net.minecraft.src;

import java.util.Random;

public class WorldGenReed extends WorldGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int i6 = 0; i6 < 20; ++i6) {
			int i7 = x + rand.nextInt(4) - rand.nextInt(4);
			int i8 = y;
			int i9 = z + rand.nextInt(4) - rand.nextInt(4);
			if(world.getBlockId(i7, y, i9) == 0 && (world.getBlockMaterial(i7 - 1, y - 1, i9) == Material.water || world.getBlockMaterial(i7 + 1, y - 1, i9) == Material.water || world.getBlockMaterial(i7, y - 1, i9 - 1) == Material.water || world.getBlockMaterial(i7, y - 1, i9 + 1) == Material.water)) {
				int i10 = 2 + rand.nextInt(rand.nextInt(3) + 1);

				for(int i11 = 0; i11 < i10; ++i11) {
					if(Block.reed.canBlockStay(world, i7, i8 + i11, i9)) {
						world.setBlock(i7, i8 + i11, i9, Block.reed.blockID);
					}
				}
			}
		}

		return true;
	}
}
