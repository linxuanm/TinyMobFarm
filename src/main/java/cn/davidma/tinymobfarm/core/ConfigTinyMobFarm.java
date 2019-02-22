package cn.davidma.tinymobfarm.core;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

@Config(modid = Reference.MOD_ID, name = Reference.MOD_ID)
public class ConfigTinyMobFarm {

	@Name("Lasso Durability")
	public static int LASSO_DURABILITY = 256;
	
	@Name("Mob Farm Rate")
	public static double[] MOB_FARM_SPEED = {50.0, 40.0, 30.0, 20.0, 10.0, 5.0, 2.5, 0.5};
	
	@Comment("Blacklist of mobs that cannot be captured (i.e. minecraft:cow for cow).")
	@Name("Mob Blacklist")
	public static String[] MOB_BLACKLIST = {};
}
