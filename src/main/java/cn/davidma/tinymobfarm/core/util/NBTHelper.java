package cn.davidma.tinymobfarm.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {
	
	// Lasso.
	public static String MOB_DATA = "mobData";
	public static String MOB_NAME = "mobName";
	
	public static NBTTagCompound getBaseTag(ItemStack stack) {
		return stack.getOrCreateChildTag(MOB_DATA);
	}
	
	public static void setBaseTag(ItemStack stack, NBTTagCompound nbt) {
		stack.getOrCreateTag().setTag(MOB_DATA, nbt);
	}
	
	public static boolean hasMob(ItemStack stack) {
		return stack.getOrCreateTag().hasKey(MOB_DATA);
	}
}
