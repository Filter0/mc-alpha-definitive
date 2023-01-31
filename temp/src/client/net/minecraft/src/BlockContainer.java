package net.minecraft.src;

public abstract class BlockContainer extends Block {
	protected BlockContainer(int i1, Material material2) {
		super(i1, material2);
		isBlockContainer[i1] = true;
	}

	protected BlockContainer(int i1, int i2, Material material3) {
		super(i1, i2, material3);
	}

	public void onBlockAdded(World worldObj, int x, int y, int z) {
		super.onBlockAdded(worldObj, x, y, z);
		worldObj.setBlockTileEntity(x, y, z, this.getBlockEntity());
	}

	public void onBlockRemoval(World worldObj, int x, int y, int z) {
		super.onBlockRemoval(worldObj, x, y, z);
		worldObj.removeBlockTileEntity(x, y, z);
	}

	protected abstract TileEntity getBlockEntity();
}
