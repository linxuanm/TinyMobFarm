package cn.davidma.idleloot.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTagHelper {

	public static NBTTagCompound getNBT(ItemStack stack) {
		
		// Yes, OCD is a thing.
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) nbt = new NBTTagCompound();
		return nbt;
	}
	
	public static boolean containsMob(NBTTagCompound nbt) {
		return keyValueEquals(nbt, "containsMob", true);
	}
	
	public static boolean keyValueEquals(NBTTagCompound nbt, String key, int value) {
		return nbt != null && nbt.hasKey(key) && nbt.getInteger(key) == value;
	}
	
	public static boolean keyValueEquals(NBTTagCompound nbt, String key, String value) {
		return nbt != null && nbt.hasKey(key) && nbt.getString(key) == value;
	}
	
	public static boolean keyValueEquals(NBTTagCompound nbt, String key, boolean value) {
		return nbt != null && nbt.hasKey(key) && nbt.getBoolean(key) == value;
	}
}
