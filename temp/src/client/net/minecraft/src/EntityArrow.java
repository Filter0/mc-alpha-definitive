package net.minecraft.src;

import java.util.List;

public class EntityArrow extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private boolean inData = false;
	public int arrowShake = 0;
	private EntityLiving shootingEntity;
	private int ticksInGround;
	private int ticksInAir = 0;

	public EntityArrow(World world1) {
		super(world1);
		this.setSize(0.5F, 0.5F);
	}

	public EntityArrow(World world, EntityLiving entityLiving) {
		super(world);
		this.shootingEntity = entityLiving;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entityLiving.posX, entityLiving.posY, entityLiving.posZ, entityLiving.rotationYaw, entityLiving.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= (double)0.1F;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
	}

	public void setArrowHeading(double moveX, double moveY, double moveZ, float arrowSpeed, float arrowSpread) {
		float f9 = MathHelper.sqrt_double(moveX * moveX + moveY * moveY + moveZ * moveZ);
		moveX /= (double)f9;
		moveY /= (double)f9;
		moveZ /= (double)f9;
		moveX += this.rand.nextGaussian() * (double)0.0075F * (double)arrowSpread;
		moveY += this.rand.nextGaussian() * (double)0.0075F * (double)arrowSpread;
		moveZ += this.rand.nextGaussian() * (double)0.0075F * (double)arrowSpread;
		moveX *= (double)arrowSpeed;
		moveY *= (double)arrowSpeed;
		moveZ *= (double)arrowSpeed;
		this.motionX = moveX;
		this.motionY = moveY;
		this.motionZ = moveZ;
		float f10 = MathHelper.sqrt_double(moveX * moveX + moveZ * moveZ);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(moveX, moveZ) * 180.0D / (double)(float)Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(moveY, (double)f10) * 180.0D / (double)(float)Math.PI);
		this.ticksInGround = 0;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inData) {
			int i1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			if(i1 == this.inTile) {
				++this.ticksInGround;
				if(this.ticksInGround == 1200) {
					this.setEntityDead();
				}

				return;
			}

			this.inData = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
			this.ticksInGround = 0;
			this.ticksInAir = 0;
		} else {
			++this.ticksInAir;
		}

		Vec3D vec3D15 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		Vec3D vec3D2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition movingObjectPosition3 = this.worldObj.rayTraceBlocks(vec3D15, vec3D2);
		vec3D15 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		vec3D2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(movingObjectPosition3 != null) {
			vec3D2 = Vec3D.createVector(movingObjectPosition3.hitVec.xCoord, movingObjectPosition3.hitVec.yCoord, movingObjectPosition3.hitVec.zCoord);
		}

		Entity entity4 = null;
		List list5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d6 = 0.0D;

		float f10;
		for(int i8 = 0; i8 < list5.size(); ++i8) {
			Entity entity9 = (Entity)list5.get(i8);
			if(entity9.canBeCollidedWith() && (entity9 != this.shootingEntity || this.ticksInAir >= 5)) {
				f10 = 0.3F;
				AxisAlignedBB axisAlignedBB11 = entity9.boundingBox.expand((double)f10, (double)f10, (double)f10);
				MovingObjectPosition movingObjectPosition12 = axisAlignedBB11.calculateIntercept(vec3D15, vec3D2);
				if(movingObjectPosition12 != null) {
					double d13 = vec3D15.distanceTo(movingObjectPosition12.hitVec);
					if(d13 < d6 || d6 == 0.0D) {
						entity4 = entity9;
						d6 = d13;
					}
				}
			}
		}

		if(entity4 != null) {
			movingObjectPosition3 = new MovingObjectPosition(entity4);
		}

		float f16;
		if(movingObjectPosition3 != null) {
			if(movingObjectPosition3.entityHit != null) {
				if(movingObjectPosition3.entityHit.attackEntityFrom(this.shootingEntity, 4)) {
					this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.setEntityDead();
				} else {
					this.motionX *= -0.10000000149011612D;
					this.motionY *= -0.10000000149011612D;
					this.motionZ *= -0.10000000149011612D;
					this.rotationYaw += 180.0F;
					this.prevRotationYaw += 180.0F;
					this.ticksInAir = 0;
				}
			} else {
				this.xTile = movingObjectPosition3.blockX;
				this.yTile = movingObjectPosition3.blockY;
				this.zTile = movingObjectPosition3.blockZ;
				this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
				this.motionX = (double)((float)(movingObjectPosition3.hitVec.xCoord - this.posX));
				this.motionY = (double)((float)(movingObjectPosition3.hitVec.yCoord - this.posY));
				this.motionZ = (double)((float)(movingObjectPosition3.hitVec.zCoord - this.posZ));
				f16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / (double)f16 * (double)0.05F;
				this.posY -= this.motionY / (double)f16 * (double)0.05F;
				this.posZ -= this.motionZ / (double)f16 * (double)0.05F;
				this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				this.inData = true;
				this.arrowShake = 7;
			}
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		f16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)(float)Math.PI);

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f16) * 180.0D / (double)(float)Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f17 = 0.99F;
		f10 = 0.03F;
		if(this.handleWaterMovement()) {
			for(int i18 = 0; i18 < 4; ++i18) {
				float f19 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f19, this.posY - this.motionY * (double)f19, this.posZ - this.motionZ * (double)f19, this.motionX, this.motionY, this.motionZ);
			}

			f17 = 0.8F;
		}

		this.motionX *= (double)f17;
		this.motionY *= (double)f17;
		this.motionZ *= (double)f17;
		this.motionY -= (double)f10;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
		compoundTag.setShort("xTile", (short)this.xTile);
		compoundTag.setShort("yTile", (short)this.yTile);
		compoundTag.setShort("zTile", (short)this.zTile);
		compoundTag.setByte("inTile", (byte)this.inTile);
		compoundTag.setByte("shake", (byte)this.arrowShake);
		compoundTag.setByte("inGround", (byte)(this.inData ? 1 : 0));
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
		this.xTile = compoundTag.getShort("xTile");
		this.yTile = compoundTag.getShort("yTile");
		this.zTile = compoundTag.getShort("zTile");
		this.inTile = compoundTag.getByte("inTile") & 255;
		this.arrowShake = compoundTag.getByte("shake") & 255;
		this.inData = compoundTag.getByte("inGround") == 1;
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer) {
		if(this.inData && this.shootingEntity == entityPlayer && this.arrowShake <= 0 && entityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow.shiftedIndex, 1))) {
			this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			entityPlayer.onItemPickup(this, 1);
			this.setEntityDead();
		}

	}

	public float getShadowSize() {
		return 0.0F;
	}
}
