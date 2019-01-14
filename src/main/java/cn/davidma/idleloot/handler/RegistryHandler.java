package cn.davidma.idleloot.handler;

import cn.davidma.idleloot.Main;
import cn.davidma.idleloot.items.StandardItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {
	
	@SubscribeEvent
	public static void onItemRegistry(RegistryEvent.Register<Item> event) {
		Item[] modItems = new Item[ItemManager.items.size()];
		modItems = ItemManager.items.toArray(modItems);
		event.getRegistry().registerAll(modItems);
	}
	
	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event) {
		for (StandardItemBase i: ItemManager.items) {
			i.registerModels();
		}
	}
}
