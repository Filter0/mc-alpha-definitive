package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockPressurePlate extends Block {
	private EnumMobType triggerMobType;

	protected BlockPressurePlate(int id, int tex, EnumMobType triggerMobType) {
		super(id, tex, Material.rock);
		this.triggerMobType = triggerMobType;
		this.setTickOnLoad(true);
		float f4 = 0.0625F;
		this.setBlockBounds(f4, 0.0F, f4, 1.0F - f4, 0.03125F, 1.0F - f4);
	}

	public int tickRate() {
		return 20;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z);
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		boolean z6 = false;
		if(!worldObj.isBlockNormalCube(x, y - 1, z)) {
			z6 = true;
		}

		if(z6) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
		}

	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getBlockMetadata(x, y, z) != 0) {
			this.setStateIfMobInteractsWithPlate(worldObj, x, y, z);
		}
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(world.getBlockMetadata(x, y, z) != 1) {
			this.setStateIfMobInteractsWithPlate(world, x, y, z);
		}
	}

	private void setStateIfMobInteractsWithPlate(World worldObj, int x, int y, int z) {
		boolean z5 = worldObj.getBlockMetadata(x, y, z) == 1;
		boolean z6 = false;
		float f7 = 0.125F;
		List list8 = null;
		if(this.triggerMobType == EnumMobType.everything) {
			list8 = worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f7), (double)y, (double)((float)z + f7), (double)((float)(x + 1) - f7), (double)y + 0.25D, (double)((float)(z + 1) - f7)));
		}

		if(this.triggerMobType == EnumMobType.mobs) {
			list8 = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f7), (double)y, (double)((float)z + f7), (double)((float)(x + 1) - f7), (double)y + 0.25D, (double)((float)(z + 1) - f7)));
		}

		if(this.triggerMobType == EnumMobType.players) {
			list8 = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f7), (double)y, (double)((float)z + f7), (double)((float)(x + 1) - f7), (double)y + 0.25D, (double)((float)(z + 1) - f7)));
		}

		if(list8.size() > 0) {
			z6 = true;
		}

		if(z6 && !z5) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 1);
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			worldObj.markBlocksDirty(x, y, z, x, y, z);
			worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
		}

		if(!z6 && z5) {
			worldObj.setBlockMetadataWithNotify(x, y, z, 0);
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
			worldObj.markBlocksDirty(x, y, z, x, y, z);
			worldObj.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
		}

		if(z6) {
			worldObj.scheduleBlockUpdate(x, y, z, this.blockID);
		}

	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		int i5 = worldObj.getBlockMetadata(x, y, z);
		if(i5 > 0) {
			worldObj.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
			worldObj.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
		}

		super.onBlockRemoval(worldObj, x, y, z);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		boolean z5 = blockAccess.getBlockMetadata(x, y, z) == 1;
		float f6 = 0.0625F;
		if(z5) {
			this.setBlockBounds(f6, 0.0F, f6, 1.0F - f6, 0.03125F, 1.0F - f6);
		} else {
			this.setBlockBounds(f6, 0.0F, f6, 1.0F - f6, 0.0625F, 1.0F - f6);
		}

	}

	public boolean isPoweringTo(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
		return blockAccess.getBlockMetadata(x, y, z) > 0;
	}

	public boolean isIndirectlyPoweringTo(World worldObj, int x, int y, int z, int side) {
		return worldObj.getBlockMetadata(x, y, z) == 0 ? false : side == 1;
	}

	public boolean canProvidePower() {
		return true;
	}

	public void setBlockBoundsForItemRender() {
		float f1 = 0.5F;
		float f2 = 0.125F;
		float f3 = 0.5F;
		this.setBlockBounds(0.5F - f1, 0.5F - f2, 0.5F - f3, 0.5F + f1, 0.5F + f2, 0.5F + f3);
	}
}
