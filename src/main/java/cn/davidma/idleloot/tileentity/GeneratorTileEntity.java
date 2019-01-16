package cn.davidma.idleloot.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class GeneratorTileEntity extends TileEntity {

	private NBTTagCompound entityNBT;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag("entityInfo", entityNBT);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		entityNBT = nbt.getCompoundTag("entityInfo");
	}
}
