package cn.davidma.tinymobfarm.core.util;

import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class EntityHelper {

	private static Method getLootTable;
	
	public static String getRegistryName(LivingEntity LivingEntity) {
		EntityType<?> entityType = LivingEntity.getType();
		return entityType.getRegistryName().toString();
	}
	
	public static boolean isMobBlacklisted(LivingEntity LivingEntity) {
		String mobName = getRegistryName(LivingEntity);
		for (String i: Config.MOB_BLACKLIST) {
			if (mobName.equalsIgnoreCase(i)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getLootTableLocation(LivingEntity LivingEntity) {
		ResourceLocation location = null;
		
		try {
			if (getLootTable == null) getLootTable = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "func_184647_J", new Class[0]);
			Object lootTableLocation = getLootTable.invoke(LivingEntity);
			if (lootTableLocation instanceof ResourceLocation) {
				location = (ResourceLocation) lootTableLocation;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return location == null ? "" : location.toString();
	}
	
	public static List<ItemStack> generateLoot(ResourceLocation lootTableLocation, World world) {
		LootTableManager lootTableManager = ServerLifecycleHooks.getCurrentServer().getLootTableManager();
		LootTable lootTable = lootTableManager.getLootTableFromLocation(lootTableLocation);
		LootContext.Builder builder = new LootContext.Builder((ServerWorld) world);
		FakePlayer daniel = FakePlayerHelper.getPlayer((ServerWorld) world);
		builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, daniel);
		LootParameterSet.Builder setBuilder = new LootParameterSet.Builder();
		setBuilder.required(LootParameters.LAST_DAMAGE_PLAYER);
		return lootTable.generate(builder.build(setBuilder.build()));
	}
}
