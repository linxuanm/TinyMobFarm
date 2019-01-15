package cn.davidma.idleloot.reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

@Config(modid=Info.MOD_ID, name=Info.MOD_ID)
public class IdleLootConfig {
	
	@Comment("The durability for the lasso. Set to -1 for infinite durability")
	@Name("Lasso Durability")
	public static int LASSO_DURABILITY = 10;
}
