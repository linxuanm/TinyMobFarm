package cn.davidma.tinymobfarm.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {
	
	// Lasso.
	public static final String MOB = "capturedMob";
	public static final String MOB_NAME = "mobName";
	public static final String MOB_DATA = "mobData";
	public static final String MOB_HEALTH = "mobHealth";
	public static final String MOB_MAX_HEALTH = "mobMaxHealth";
	public static final String MOB_HOSTILE = "mobHostile";
	public static final String MOB_LOOTTABLE_LOCATION = "mobLootTableLocation";
	
	// Tile entity.
	public static final String MOB_FARM_DATA = "mobFarmData";
	public static final String CURR_PROGRESS = "currProgress";
	public static final String INVENTORY = "inventory";
	
	public static NBTTagCompound getBaseTag(ItemStack stack) {
		return stack.getOrCreateChildTag(MOB);
	}
	
	public static void setBaseTag(ItemStack stack, NBTTagCompound nbt) {
		stack.getOrCreateTag().setTag(MOB, nbt);
	}
	
	public static boolean hasMob(ItemStack stack) {
		return stack.getOrCreateTag().hasKey(MOB);
	}
	
	public static boolean hasHostileMob(ItemStack stack) {
		if (!hasMob(stack)) return false;
		NBTTagCompound nbt = NBTHelper.getBaseTag(stack);
		return nbt.getBoolean(NBTHelper.MOB_HOSTILE);
	}

	public static NBTTagList createNBTList(INBTBase... tags) {
		NBTTagList list = new NBTTagList();
		for (INBTBase i: tags) list.add(i);
		
		return list;
	}
}
