package cn.davidma.tinymobfarm.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class LootTableHelper {

	public static ResourceLocation getLootTableLocation(EntityLiving entity) {
		ResourceLocation location = null;
		
		Class[] paramTypes = {}; // An empty array has the same effect with null (OCD).
		Method getLoot = ReflectionHelper.findMethod(entity.getClass(), "getLootTable", "func_184647_J", paramTypes);
		try {
			Object tmp = getLoot.invoke(entity);
			if (tmp instanceof ResourceLocation) location = (ResourceLocation) tmp;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		return location;
	}
	
	public static List<ItemStack> genLoots(ResourceLocation location, World world) {
		LootTable loottable = world.getLootTableManager().getLootTableFromLocation(location);
		LootContext.Builder lootContext = (new LootContext.Builder((WorldServer) world));
		List<ItemStack> loots = loottable.generateLootForPools(new Random(), lootContext.build());
		
		return loots;
	}
}
