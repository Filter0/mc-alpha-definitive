package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
	private String key = null;

	abstract void writeTagContents(DataOutput dataOutput1) throws IOException;

	abstract void readTagContents(DataInput dataInput1) throws IOException;

	public abstract byte getType();

	public String getKey() {
		return this.key == null ? "" : this.key;
	}

	public NBTBase setKey(String key) {
		this.key = key;
		return this;
	}

	public static NBTBase readNamedTag(DataInput dataInput) throws IOException {
		byte b1 = dataInput.readByte();
		if(b1 == 0) {
			return new NBTTagEnd();
		} else {
			NBTBase nBTBase2 = createTagOfType(b1);
			nBTBase2.key = dataInput.readUTF();
			nBTBase2.readTagContents(dataInput);
			return nBTBase2;
		}
	}

	public static void writeNamedTag(NBTBase nbtbase, DataOutput dataOutput) throws IOException {
		dataOutput.writeByte(nbtbase.getType());
		if(nbtbase.getType() != 0) {
			dataOutput.writeUTF(nbtbase.getKey());
			nbtbase.writeTagContents(dataOutput);
		}
	}

	public static NBTBase createTagOfType(byte type) {
		switch(type) {
		case 0:
			return new NBTTagEnd();
		case 1:
			return new NBTTagByte();
		case 2:
			return new NBTTagShort();
		case 3:
			return new NBTTagInt();
		case 4:
			return new NBTTagLong();
		case 5:
			return new NBTTagFloat();
		case 6:
			return new NBTTagDouble();
		case 7:
			return new NBTTagByteArray();
		case 8:
			return new NBTTagString();
		case 9:
			return new NBTTagList();
		case 10:
			return new NBTTagCompound();
		default:
			return null;
		}
	}

	public static String getTagName(byte type) {
		switch(type) {
		case 0:
			return "TAG_End";
		case 1:
			return "TAG_Byte";
		case 2:
			return "TAG_Short";
		case 3:
			return "TAG_Int";
		case 4:
			return "TAG_Long";
		case 5:
			return "TAG_Float";
		case 6:
			return "TAG_Double";
		case 7:
			return "TAG_Byte_Array";
		case 8:
			return "TAG_String";
		case 9:
			return "TAG_List";
		case 10:
			return "TAG_Compound";
		default:
			return "UNKNOWN";
		}
	}
}
