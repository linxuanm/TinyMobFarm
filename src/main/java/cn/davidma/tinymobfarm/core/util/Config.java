package cn.davidma.tinymobfarm.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
	
	public static final ForgeConfigSpec SPEC;
	public static final ConfigBuilder CONFIG;
	
	static {
		final Pair<ConfigBuilder, ForgeConfigSpec> pair =
				new ForgeConfigSpec.Builder().configure(ConfigBuilder::new);
		CONFIG = pair.getLeft();
		SPEC = pair.getRight();
	}
	
	public static void bakeConfig(ModConfig config) {
		LASSO_DURABILITY = CONFIG.lassoDurability.get();
		MOB_BLACKLIST = CONFIG.blacklist.get();
		DISABLE_WHEN_CHEST_FULL = CONFIG.disableWhenChestFull.get();
	}
	
	// Config.
	public static int LASSO_DURABILITY;
	public static List<String> MOB_BLACKLIST;
	public static boolean DISABLE_WHEN_CHEST_FULL;
	
	public static class ConfigBuilder {
		
		private final ForgeConfigSpec.IntValue lassoDurability;
		private final ForgeConfigSpec.ConfigValue<List<String>> blacklist;
		private final ForgeConfigSpec.BooleanValue disableWhenChestFull;
		
		public ConfigBuilder(ForgeConfigSpec.Builder builder) {
			builder.push("general");
			
			this.lassoDurability = builder
					.comment("The durability of lasso. -1 for infinite.")
					.defineInRange("lassoDurability", 128, -1, Integer.MAX_VALUE);
			
			this.blacklist = builder
					.comment("Mobs to be blacklisted. e.g. 'minecraft:cow' for cows.")
					.define("blacklist", new ArrayList<>());
			
			this.disableWhenChestFull = builder
					.comment("Disable the mob farm when there is no chest/full "
							+ "chest to prevent loot from spilling out. Recommended on server.")
					.define("disableWhenChestFull", false);
			
			builder.pop();
		}
	}
}
