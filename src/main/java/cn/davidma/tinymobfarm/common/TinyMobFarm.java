package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.core.Reference;
import net.minecraftforge.fml.common.Mod;

@Mod(value = Reference.MOD_ID)
public class TinyMobFarm {

	public static TinyMobFarm instance;
	
	public TinyMobFarm() {
		instance = this;
	}
}
