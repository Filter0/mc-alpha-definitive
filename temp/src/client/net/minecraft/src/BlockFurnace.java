package net.minecraft.src;

import java.util.Random;

public class BlockFurnace extends BlockContainer {
	private final boolean isActive;

	protected BlockFurnace(int blockID, boolean isActive) {
		super(blockID, Material.rock);
		this.isActive = isActive;
		this.blockIndexInTexture = 45;
	}

	public int idDropped(int metadata, Random rand) {
		return Block.stoneOvenIdle.blockID;
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		super.onBlockAdded(worldObj, x, y, z);
		this.setDefaultDirection(worldObj, x, y, z);
	}

	private void setDefaultDirection(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockId(x, y, z - 1);
		int i6 = worldObj.getBlockId(x, y, z + 1);
		int i7 = worldObj.getBlockId(x - 1, y, z);
		int i8 = worldObj.getBlockId(x + 1, y, z);
		byte b9 = 3;
		if(Block.opaqueCubeLookup[i5] && !Block.opaqueCubeLookup[i6]) {
			b9 = 3;
		}

		if(Block.opaqueCubeLookup[i6] && !Block.opaqueCubeLookup[i5]) {
			b9 = 2;
		}

		if(Block.opaqueCubeLookup[i7] && !Block.opaqueCubeLookup[i8]) {
			b9 = 5;
		}

		if(Block.opaqueCubeLookup[i8] && !Block.opaqueCubeLookup[i7]) {
			b9 = 4;
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, b9);
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(side == 1) {
			return Block.stone.blockIndexInTexture;
		} else if(side == 0) {
			return Block.stone.blockIndexInTexture;
		} else {
			int i6 = blockAccess.getBlockMetadata(x, y, z);
			return side != i6 ? this.blockIndexInTexture : (this.isActive ? this.blockIndexInTexture + 16 : this.blockIndexInTexture - 1);
		}
	}

	public void randomDisplayTick(World worldObj, int x, int y, int z, Random rand) {
		if(this.isActive) {
			int i6 = worldObj.getBlockMetadata(x, y, z);
			float f7 = (float)x + 0.5F;
			float f8 = (float)y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
			float f9 = (float)z + 0.5F;
			float f10 = 0.52F;
			float f11 = rand.nextFloat() * 0.6F - 0.3F;
			if(i6 == 4) {
				worldObj.spawnParticle("smoke", (double)(f7 - f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(f7 - f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 5) {
				worldObj.spawnParticle("smoke", (double)(f7 + f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(f7 + f10), (double)f8, (double)(f9 + f11), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 2) {
				worldObj.spawnParticle("smoke", (double)(f7 + f11), (double)f8, (double)(f9 - f10), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(f7 + f11), (double)f8, (double)(f9 - f10), 0.0D, 0.0D, 0.0D);
			} else if(i6 == 3) {
				worldObj.spawnParticle("smoke", (double)(f7 + f11), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
				worldObj.spawnParticle("flame", (double)(f7 + f11), (double)f8, (double)(f9 + f10), 0.0D, 0.0D, 0.0D);
			}

		}
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? Block.stone.blockID : (side == 0 ? Block.stone.blockID : (side == 3 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture));
	}

	public boolean blockActivated(World worldObj, int x, int y, int z, EntityPlayer entityPlayer) {
		TileEntityFurnace tileEntityFurnace6 = (TileEntityFurnace)worldObj.getBlockTileEntity(x, y, z);
		entityPlayer.displayGUIFurnace(tileEntityFurnace6);
		return true;
	}

	public static void updateFurnaceBlockState(boolean isActive, World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		TileEntity tileEntity6 = worldObj.getBlockTileEntity(x, y, z);
		if(isActive) {
			worldObj.setBlockWithNotify(x, y, z, Block.stoneOvenActive.blockID);
		} else {
			worldObj.setBlockWithNotify(x, y, z, Block.stoneOvenIdle.blockID);
		}

		worldObj.setBlockMetadataWithNotify(x, y, z, i5);
		worldObj.setBlockTileEntity(x, y, z, tileEntity6);
	}

	protected TileEntity getBlockEntity() {
		return new TileEntityFurnace();
	}
}
