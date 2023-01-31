package net.minecraft.src;

public class EntitySheep extends EntityAnimal {
	public boolean sheared = false;

	public EntitySheep(World world1) {
		super(world1);
		this.texture = "/mob/sheep.png";
		this.setSize(0.9F, 1.3F);
	}

	public boolean attackEntityFrom(Entity entity, int damage) {
		if(!this.sheared && entity instanceof EntityLiving) {
			this.sheared = true;
			int i3 = 1 + this.rand.nextInt(3);

			for(int i4 = 0; i4 < i3; ++i4) {
				EntityItem entityItem5 = this.entityDropItem(Block.cloth.blockID, 1, 1.0F);
				entityItem5.motionY += (double)(this.rand.nextFloat() * 0.05F);
				entityItem5.motionX += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				entityItem5.motionZ += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
			}
		}

		return super.attackEntityFrom(entity, damage);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Sheared", this.sheared);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.sheared = nbttagcompound.getBoolean("Sheared");
	}

	protected String getLivingSound() {
		return "mob.sheep";
	}

	protected String getHurtSound() {
		return "mob.sheep";
	}

	protected String getDeathSound() {
		return "mob.sheep";
	}
}
