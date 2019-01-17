package cn.davidma.idleloot.reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

@Config(modid=Info.MOD_ID, name=Info.MOD_ID)
public class IdleLootConfig {
	
	@Comment("The durability for the lasso. Set to -1 for infinite durability")
	@Name("Lasso Durability")
	public static int LASSO_DURABILITY = 256;
	
	@Comment({
		"The rate for different tiered generators (in seconds).",
		"Smaler rate value -> faster speed.",
		"(1. wood; 2. stone ... 8. ultimate)"})
	@Name("Generator Rate")
	public static int[] GENERATOR_SPEED = new int[] {50, 40, 30, 20, 15, 10, 5, 1};
}
