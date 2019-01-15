package cn.davidma.idleloot.handler;

import cn.davidma.idleloot.Main;
import cn.davidma.idleloot.block.StandardBlockBase;
import cn.davidma.idleloot.item.template.StandardItemBase;
import cn.davidma.idleloot.util.Registrable;
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
		ItemManager.instantiateAllItems();
		Item[] modItems = new Item[ItemManager.items.size()];
		modItems = ItemManager.items.toArray(modItems);
		event.getRegistry().registerAll(modItems);
	}
	
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
		BlockManager.instantiateAllBlocks();
		Block[] modBlocks = new Block[BlockManager.blocks.size()];
		modBlocks = BlockManager.blocks.toArray(modBlocks);
		event.getRegistry().registerAll(modBlocks);
	}
	
	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event) {
		for (Item i: ItemManager.items) {
			if (i instanceof Registrable) ((Registrable) i).registerModels();
		}
		for (Block i: BlockManager.blocks) {
			if (i instanceof Registrable) ((Registrable) i).registerModels();
		}
	}
}
