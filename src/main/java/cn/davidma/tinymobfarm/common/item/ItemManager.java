package cn.davidma.tinymobfarm.common.item;

import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ItemManager {
	
	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(new Lasso(new Item.Properties()).setRegistryName(Reference.getLocation("lasso")));
	}
}
