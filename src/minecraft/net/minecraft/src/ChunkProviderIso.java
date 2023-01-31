package net.minecraft.src;

import java.io.IOException;

public class ChunkProviderIso implements IChunkProvider {
	private Chunk[] chunks = new Chunk[256];
	private World worldObj;
	private IChunkLoader chunkLoader;
	byte[] blocks = new byte[32768];

	public ChunkProviderIso(World worldObj, IChunkLoader chunkLoader) {
		this.worldObj = worldObj;
		this.chunkLoader = chunkLoader;
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		int i3 = chunkX & 15 | (chunkZ & 15) * 16;
		return this.chunks[i3] != null && this.chunks[i3].isAtLocation(chunkX, chunkZ);
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		int i3 = chunkX & 15 | (chunkZ & 15) * 16;

		try {
			if(!this.chunkExists(chunkX, chunkZ)) {
				Chunk chunk4 = this.getChunkAt(chunkX, chunkZ);
				if(chunk4 == null) {
					chunk4 = new Chunk(this.worldObj, this.blocks, chunkX, chunkZ);
					chunk4.isChunkRendered = true;
					chunk4.neverSave = true;
				}

				this.chunks[i3] = chunk4;
			}

			return this.chunks[i3];
		} catch (Exception exception5) {
			exception5.printStackTrace();
			return null;
		}
	}

	private synchronized Chunk getChunkAt(int chunkX, int chunkZ) {
		try {
			return this.chunkLoader.loadChunk(this.worldObj, chunkX, chunkZ);
		} catch (IOException iOException4) {
			iOException4.printStackTrace();
			return null;
		}
	}

	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
	}

	public boolean saveChunks(boolean z1, IProgressUpdate progressUpdate) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return false;
	}
}
