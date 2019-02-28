package cn.davidma.tinymobfarm.core.util;

import java.lang.reflect.Method;
import java.util.List;

import cn.davidma.tinymobfarm.core.ConfigTinyMobFarm;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityHelper {
	
	private static Method getLootTable;

	public static String getRegistryName(EntityLiving entityLiving) {
		return EntityRegistry.getEntry(entityLiving.getClass()).getRegistryName().toString();
	}
	
	public static boolean isMobBlacklisted(EntityLiving entityLiving) {
		String mobName = getRegistryName(entityLiving);
		for (String i: ConfigTinyMobFarm.MOB_BLACKLIST) {
			if (mobName.equalsIgnoreCase(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getLootTableLocation(EntityLiving entityLiving) {
		ResourceLocation location = null;
		
		try {
			if (getLootTable == null) {
				getLootTable = ReflectionHelper.findMethod(EntityLiving.class, "getLootTable", "func_184647_J", new Class[0]);
			}
			Object lootTableLocation = getLootTable.invoke(entityLiving);
			if (lootTableLocation instanceof ResourceLocation) {
				location = (ResourceLocation) lootTableLocation;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return location == null ? "" : location.toString();
	}
	
	public static List<ItemStack> generateLoot(ResourceLocation lootTableLocation, World world) {
		LootTableManager lootTableManager = world.getLootTableManager();
		LootTable lootTable = lootTableManager.getLootTableFromLocation(lootTableLocation);
		LootContext.Builder builder = new LootContext.Builder((WorldServer) world);
		FakePlayer daniel = FakePlayerHelper.getPlayer((WorldServer) world);
		builder.withPlayer(daniel);
		return lootTable.generateLootForPools(world.rand, builder.build());
	}
}
