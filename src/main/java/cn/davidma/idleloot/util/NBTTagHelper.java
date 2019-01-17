package cn.davidma.idleloot.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTagHelper {
	
	// All NBT tags are set under the BASE tag for simplicity.

	// Item tags.
	public static final String BASE = "idleLoot";
	
	// TileEntity tags;
	public static final String CURR_PROGRESS_TAG = "currProgress";
	public static final String TOTAL_PROGRESS_TAG = "totalProgress";
	public static final String CUSTOM_NAME_TAG = "customName";
	
	public static NBTTagCompound getEssentialNBT(ItemStack stack) {
		
		// Yes, OCD is a thing.
		NBTTagCompound base = stack.getTagCompound();
		if (base == null) base = new NBTTagCompound();
		NBTTagCompound nbt = getAttr(base, BASE);
		return nbt;
	}
	
	public static void setEssentialNBT(ItemStack stack, NBTTagCompound nbt) {
		NBTTagCompound base = stack.getTagCompound();
		if (base == null) base = new NBTTagCompound();
		base.setTag(BASE, nbt);
		stack.setTagCompound(base);
	}
	
	public static NBTTagCompound getAttr(NBTTagCompound nbt, String key) {
		NBTTagCompound out = nbt.getCompoundTag(key);
		if (out == null) out = new NBTTagCompound();
		return out;
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
