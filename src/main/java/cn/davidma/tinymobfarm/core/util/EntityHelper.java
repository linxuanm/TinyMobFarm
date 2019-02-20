package cn.davidma.tinymobfarm.core.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;

public class EntityHelper {

	public static String getRegistryName(EntityLiving entityLiving) {
		EntityType<?> entityType = entityLiving.getType();
		return entityType.getRegistryName().toString();
	}
}
