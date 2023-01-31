package net.minecraft.src;

public class TileEntitySign extends TileEntity {
	public String[] signText = new String[]{"", "", "", ""};
	public int lineBeingEdited = -1;

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("Text1", this.signText[0]);
		nbttagcompound.setString("Text2", this.signText[1]);
		nbttagcompound.setString("Text3", this.signText[2]);
		nbttagcompound.setString("Text4", this.signText[3]);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		for(int i2 = 0; i2 < 4; ++i2) {
			this.signText[i2] = nbttagcompound.getString("Text" + (i2 + 1));
			if(this.signText[i2].length() > 15) {
				this.signText[i2] = this.signText[i2].substring(0, 15);
			}
		}

	}
}
