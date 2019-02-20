package cn.davidma.tinymobfarm.core.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NBTTagCompound;

public class EntityHelper {

	public static String getRegistryName(EntityLiving entityLiving) {
		EntityType<?> entityType = entityLiving.getType();
		return entityType.getRegistryName().toString();
	}
	
	public static String getLootTableLocation(NBTTagCompound nbt) {
		return nbt.getString("DeathLootTable");
	}
}
