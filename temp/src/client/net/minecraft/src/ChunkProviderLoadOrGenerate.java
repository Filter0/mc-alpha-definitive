package net.minecraft.src;

import java.io.IOException;

public class ChunkProviderLoadOrGenerate implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private Chunk[] chunks = new Chunk[1024];
	private World worldObj;
	int lastQueriedChunkXPos = -999999999;
	int lastQueriedChunkZPos = -999999999;
	private Chunk lastQueriedChunk;

	public ChunkProviderLoadOrGenerate(World world, IChunkLoader chunkLoader, IChunkProvider chunkProvider) {
		this.blankChunk = new Chunk(world, new byte[32768], 0, 0);
		this.blankChunk.isChunkRendered = true;
		this.blankChunk.neverSave = true;
		this.worldObj = world;
		this.chunkLoader = chunkLoader;
		this.chunkProvider = chunkProvider;
	}

	public boolean chunkExists(int chunkX, int chunkZ) {
		if(chunkX == this.lastQueriedChunkXPos && chunkZ == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return true;
		} else {
			int i3 = chunkX & 31;
			int i4 = chunkZ & 31;
			int i5 = i3 + i4 * 32;
			return this.chunks[i5] != null && (this.chunks[i5] == this.blankChunk || this.chunks[i5].isAtLocation(chunkX, chunkZ));
		}
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		if(chunkX == this.lastQueriedChunkXPos && chunkZ == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
			return this.lastQueriedChunk;
		} else {
			int i3 = chunkX & 31;
			int i4 = chunkZ & 31;
			int i5 = i3 + i4 * 32;
			if(!this.chunkExists(chunkX, chunkZ)) {
				if(this.chunks[i5] != null) {
					this.chunks[i5].onChunkUnload();
					this.saveChunk(this.chunks[i5]);
					this.saveExtraChunkData(this.chunks[i5]);
				}

				Chunk chunk6 = this.getChunkAt(chunkX, chunkZ);
				if(chunk6 == null) {
					if(this.chunkProvider == null) {
						chunk6 = this.blankChunk;
					} else {
						chunk6 = this.chunkProvider.provideChunk(chunkX, chunkZ);
					}
				}

				this.chunks[i5] = chunk6;
				if(this.chunks[i5] != null) {
					this.chunks[i5].onChunkLoad();
				}

				if(!this.chunks[i5].isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX + 1, chunkZ)) {
					this.populate(this, chunkX, chunkZ);
				}

				if(this.chunkExists(chunkX - 1, chunkZ) && !this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX - 1, chunkZ)) {
					this.populate(this, chunkX - 1, chunkZ);
				}

				if(this.chunkExists(chunkX, chunkZ - 1) && !this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX + 1, chunkZ)) {
					this.populate(this, chunkX, chunkZ - 1);
				}

				if(this.chunkExists(chunkX - 1, chunkZ - 1) && !this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX - 1, chunkZ)) {
					this.populate(this, chunkX - 1, chunkZ - 1);
				}
			}

			this.lastQueriedChunkXPos = chunkX;
			this.lastQueriedChunkZPos = chunkZ;
			this.lastQueriedChunk = this.chunks[i5];
			return this.chunks[i5];
		}
	}

	private Chunk getChunkAt(int posX, int posZ) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk chunk3 = this.chunkLoader.loadChunk(this.worldObj, posX, posZ);
				if(chunk3 != null) {
					chunk3.lastSaveTime = this.worldObj.worldTime;
				}

				return chunk3;
			} catch (Exception exception4) {
				exception4.printStackTrace();
				return null;
			}
		}
	}

	private void saveExtraChunkData(Chunk chunk) {
		if(this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.worldObj, chunk);
			} catch (Exception exception3) {
				exception3.printStackTrace();
			}

		}
	}

	private void saveChunk(Chunk chunk) {
		if(this.chunkLoader != null) {
			try {
				chunk.lastSaveTime = this.worldObj.worldTime;
				this.chunkLoader.saveChunk(this.worldObj, chunk);
			} catch (IOException iOException3) {
				iOException3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider chunkProvider, int chunkX, int chunkZ) {
		Chunk chunk4 = this.provideChunk(chunkX, chunkZ);
		if(!chunk4.isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(chunkProvider, chunkX, chunkZ);
				chunk4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean z1, IProgressUpdate progressUpdate) {
		int i3 = 0;
		int i4 = 0;
		int i5;
		if(progressUpdate != null) {
			for(i5 = 0; i5 < this.chunks.length; ++i5) {
				if(this.chunks[i5] != null && this.chunks[i5].needsSaving(z1)) {
					++i4;
				}
			}
		}

		i5 = 0;

		for(int i6 = 0; i6 < this.chunks.length; ++i6) {
			if(this.chunks[i6] != null) {
				if(z1 && !this.chunks[i6].neverSave) {
					this.saveExtraChunkData(this.chunks[i6]);
				}

				if(this.chunks[i6].needsSaving(z1)) {
					this.saveChunk(this.chunks[i6]);
					this.chunks[i6].isModified = false;
					++i3;
					if(i3 == 2 && !z1) {
						return false;
					}

					if(progressUpdate != null) {
						++i5;
						if(i5 % 10 == 0) {
							progressUpdate.setLoadingProgress(i5 * 100 / i4);
						}
					}
				}
			}
		}

		if(z1) {
			if(this.chunkLoader == null) {
				return true;
			}

			this.chunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		if(this.chunkLoader != null) {
			this.chunkLoader.chunkTick();
		}

		return this.chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}
}
