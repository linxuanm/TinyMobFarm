package cn.davidma.tinymobfarm.core.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

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
	
	public static CompoundNBT getBaseTag(ItemStack stack) {
		return stack.getOrCreateTagElement(MOB);
	}
	
	public static void setBaseTag(ItemStack stack, CompoundNBT nbt) {
		stack.getOrCreateTag().put(MOB, nbt);
	}
	
	public static boolean hasMob(ItemStack stack) {
		return stack.getOrCreateTag().contains(MOB);
	}
	
	public static boolean hasHostileMob(ItemStack stack) {
		if (!hasMob(stack)) return false;
		CompoundNBT nbt = NBTHelper.getBaseTag(stack);
		return nbt.getBoolean(NBTHelper.MOB_HOSTILE);
	}

	public static ListNBT createNBTList(INBT... tags) {
		ListNBT list = new ListNBT();
		for (INBT i: tags) list.add(i);
		
		return list;
	}
}
