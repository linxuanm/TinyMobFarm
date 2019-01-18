package cn.davidma.tinymobfarm.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class LootTableHelper {

	public static ResourceLocation getLootTable(EntityLiving entity) {
		ResourceLocation location = null;
		
		Class[] paramTypes = {}; // An empty array has the same effect with null (OCD).
		Method getLoot = ReflectionHelper.findMethod(entity.getClass(), "getLootTable", "func_184647_J", paramTypes);
		try {
			Object tmp = getLoot.invoke(entity);
			if (tmp instanceof ResourceLocation) location = (ResourceLocation) tmp;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}
}
