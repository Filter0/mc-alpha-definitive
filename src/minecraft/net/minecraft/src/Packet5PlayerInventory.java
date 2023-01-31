package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory extends Packet {
	public int inventoryType;
	public ItemStack[] inventory;

	public Packet5PlayerInventory() {
	}

	public Packet5PlayerInventory(int invType, ItemStack[] invContents) {
		this.inventoryType = invType;
		this.inventory = new ItemStack[invContents.length];

		for(int i3 = 0; i3 < this.inventory.length; ++i3) {
			this.inventory[i3] = invContents[i3] == null ? null : invContents[i3].copy();
		}

	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.inventoryType = dataInputStream.readInt();
		short s2 = dataInputStream.readShort();
		this.inventory = new ItemStack[s2];

		for(int i3 = 0; i3 < s2; ++i3) {
			short s4 = dataInputStream.readShort();
			if(s4 >= 0) {
				byte b5 = dataInputStream.readByte();
				short s6 = dataInputStream.readShort();
				this.inventory[i3] = new ItemStack(s4, b5, s6);
			}
		}

	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.inventoryType);
		dataOutputStream.writeShort(this.inventory.length);

		for(int i2 = 0; i2 < this.inventory.length; ++i2) {
			if(this.inventory[i2] == null) {
				dataOutputStream.writeShort(-1);
			} else {
				dataOutputStream.writeShort((short)this.inventory[i2].itemID);
				dataOutputStream.writeByte((byte)this.inventory[i2].stackSize);
				dataOutputStream.writeShort((short)this.inventory[i2].itemDmg);
			}
		}

	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handlePlayerInventory(this);
	}

	public int getPacketSize() {
		return 6 + this.inventory.length * 5;
	}
}
