package cn.davidma.tinymobfarm.core;

import net.minecraft.util.ResourceLocation;

public class Reference {

	public static final String MOD_NAME = "Tiny Mob Farm";
	public static final String MOD_ID = "tinymobfarm";
	public static final String VERSION = "1.0.5";
	public static final String MC_VERSION = "[1.13.2]";
	public static final String CLIENT_PROXY = "cn.davidma.tinymobfarm.proxy.ClientProxy";
	public static final String SERVER_PROXY = "cn.davidma.tinymobfarm.proxy.ServerProxy";
	
	public static ResourceLocation getLocation(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}
