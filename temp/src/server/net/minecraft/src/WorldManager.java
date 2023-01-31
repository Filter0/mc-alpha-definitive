package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldManager implements IWorldAccess {
	private MinecraftServer mcServer;

	public WorldManager(MinecraftServer minecraftServer1) {
		this.mcServer = minecraftServer1;
	}

	public void spawnParticle(String name, double x, double y, double z, double d8, double d10, double d12) {
	}

	public void obtainEntitySkin(Entity entity) {
		this.mcServer.entityTracker.trackEntity(entity);
	}

	public void releaseEntitySkin(Entity entity) {
		this.mcServer.entityTracker.untrackEntity(entity);
	}

	public void playSound(String name, double x, double y, double z, float volume, float pitch) {
	}

	public void markBlockRangeNeedsUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
	}

	public void updateAllRenderers() {
	}

	public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {
		this.mcServer.configManager.markBlockNeedsUpdate(x, y, z);
	}

	public void playRecord(String name, int x, int y, int z) {
	}

	public void doNothingWithTileEntity(int x, int y, int z, TileEntity tileEntity) {
		this.mcServer.configManager.sentTileEntityToPlayer(x, y, z, tileEntity);
	}
}
