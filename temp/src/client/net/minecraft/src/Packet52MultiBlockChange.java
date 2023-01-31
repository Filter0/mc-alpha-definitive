package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet52MultiBlockChange extends Packet {
	public int xPosition;
	public int zPosition;
	public short[] coordinateArray;
	public byte[] typeArray;
	public byte[] metadataArray;
	public int size;

	public Packet52MultiBlockChange() {
		this.isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.xPosition = dataInputStream.readInt();
		this.zPosition = dataInputStream.readInt();
		this.size = dataInputStream.readShort() & 65535;
		this.coordinateArray = new short[this.size];
		this.typeArray = new byte[this.size];
		this.metadataArray = new byte[this.size];

		for(int i2 = 0; i2 < this.size; ++i2) {
			this.coordinateArray[i2] = dataInputStream.readShort();
		}

		dataInputStream.readFully(this.typeArray);
		dataInputStream.readFully(this.metadataArray);
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.xPosition);
		dataOutputStream.writeInt(this.zPosition);
		dataOutputStream.writeShort((short)this.size);

		for(int i2 = 0; i2 < this.size; ++i2) {
			dataOutputStream.writeShort(this.coordinateArray[i2]);
		}

		dataOutputStream.write(this.typeArray);
		dataOutputStream.write(this.metadataArray);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleMultiBlockChange(this);
	}

	public int getPacketSize() {
		return 10 + this.size * 4;
	}
}
