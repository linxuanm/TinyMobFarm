package cn.davidma.tinymobfarm.core;

import net.minecraft.item.Item;

public interface IProxy {

	public void preInit();
	
	public void init();
	
	public void postInit();
	
	public void registerModel(Item item, int meta, String id);
}
