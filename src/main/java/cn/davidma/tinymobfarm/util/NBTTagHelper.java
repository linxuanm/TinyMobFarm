package cn.davidma.tinymobfarm.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTTagHelper {
	
	// All NBT tags are set under the BASE tag for simplicity.

	// Item tags.
	public static final String BASE = "tinymobfarm";
	public static final String ENTITY_INFO = "entityInfo";
	public static final String MOB_HEALTH = "mobHealth";
	public static final String MOB_MAX_HEALTH = "mobMaxHealth";
	public static final String MOB_NAME = "mobName";
	public static final String SHINY = "shiny";
	public static final String CONTAINS_MOB = "containsMob";
	public static final String IS_HOSTILE = "isHostile";
	public static final String LOOT_TABLE_LOCATION = "lootTableLocation";
	
	// TileEntity tags;
	public static final String CURR_PROGRESS_TAG = "currProgress";
	public static final String CUSTOM_NAME_TAG = "customName";
	public static final String ID_TAG = "mobfarmId";
	
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
		return keyValueEquals(nbt, CONTAINS_MOB, true);
	}
	
	public static boolean containsMob(ItemStack stack) {
		return containsMob(getEssentialNBT(stack));
	}
	
	public static boolean isHostile(NBTTagCompound nbt) {
		return keyValueEquals(nbt, IS_HOSTILE, true);
	}
	
	public static boolean isHostile(ItemStack stack) {
		return isHostile(getEssentialNBT(stack));
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
