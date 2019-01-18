package cn.davidma.tinymobfarm.handler;

import cn.davidma.tinymobfarm.Main;
import cn.davidma.tinymobfarm.block.template.StandardBlockBase;
import cn.davidma.tinymobfarm.item.template.StandardItemBase;
import cn.davidma.tinymobfarm.util.Registrable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {
	
	@SubscribeEvent
	public static void onItemRegistry(RegistryEvent.Register<Item> event) {
		CollectionsManager.instantiateAllItems();
		Item[] modItems = new Item[CollectionsManager.items.size()];
		modItems = CollectionsManager.items.toArray(modItems);
		event.getRegistry().registerAll(modItems);
	}
	
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
		CollectionsManager.instantiateAllBlocks();
		Block[] modBlocks = new Block[CollectionsManager.blocks.size()];
		modBlocks = CollectionsManager.blocks.toArray(modBlocks);
		event.getRegistry().registerAll(modBlocks);
	}
	
	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event) {
		for (Item i: CollectionsManager.items) {
			if (i instanceof Registrable) ((Registrable) i).registerModels();
		}
		for (Block i: CollectionsManager.blocks) {
			if (i instanceof Registrable) ((Registrable) i).registerModels();
		}
	}
}
