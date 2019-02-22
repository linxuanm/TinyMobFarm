package cn.davidma.tinymobfarm.core.util;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.common.item.ItemLasso;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHelper {

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		System.out.println(123);
		event.getRegistry().register(TinyMobFarm.lasso = new ItemLasso());
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		TinyMobFarm.proxy.registerModel(TinyMobFarm.lasso, 0, "inventory");
	}
}
