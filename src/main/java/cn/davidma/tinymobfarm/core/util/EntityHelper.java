package cn.davidma.tinymobfarm.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EntityHelper {

	private static Method getLootTable;
	
	public static String getRegistryName(EntityLiving entityLiving) {
		EntityType<?> entityType = entityLiving.getType();
		return entityType.getRegistryName().toString();
	}
	
	public static boolean isMobBlacklisted(EntityLiving entityLiving) {
		String mobName = getRegistryName(entityLiving);
		for (String i: Config.MOB_BLACKLIST) {
			if (mobName.equalsIgnoreCase(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getLootTableLocation(EntityLiving entityLiving) {
		ResourceLocation location = null;
		
		if (getLootTable == null) getLootTable = ObfuscationReflectionHelper.findMethod(EntityLiving.class, "func_184647_J", new Class[0]);
		
		try {
			Object lootTableLocation = getLootTable.invoke(entityLiving);
			if (lootTableLocation instanceof ResourceLocation) {
				location = (ResourceLocation) lootTableLocation;
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return location == null ? "" : location.toString();
	}
	
	public static List<ItemStack> generateLoot(ResourceLocation lootTableLocation, World world) {
		LootTableManager lootTableManager = ServerLifecycleHooks.getCurrentServer().getLootTableManager();
		LootTable lootTable = lootTableManager.getLootTableFromLocation(lootTableLocation);
		LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
		FakePlayer daniel = FakePlayerHelper.getPlayer((WorldServer) world);
		builder.withPlayer(daniel);
		return lootTable.generateLootForPools(world.rand, builder.build());
	}
}
