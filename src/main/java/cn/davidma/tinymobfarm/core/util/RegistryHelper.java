package cn.davidma.tinymobfarm.core.util;

import java.util.ArrayList;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import cn.davidma.tinymobfarm.common.block.BlockMobFarm;
import cn.davidma.tinymobfarm.common.item.ItemBlockMobFarm;
import cn.davidma.tinymobfarm.common.item.ItemLasso;
import cn.davidma.tinymobfarm.core.EnumMobFarm;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class RegistryHelper {

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		TinyMobFarm.blockMobFarms = new ArrayList<BlockMobFarm>();
		
		for (EnumMobFarm i: EnumMobFarm.values()) {
			BlockMobFarm blockMobFarm = new BlockMobFarm(i);
			registry.register(blockMobFarm);
			TinyMobFarm.blockMobFarms.add(blockMobFarm);
			
		}
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		TinyMobFarm.itemBlockMobFarms = new ArrayList<ItemBlock>();
		
		registry.register(TinyMobFarm.lasso = new ItemLasso());
		for (BlockMobFarm i: TinyMobFarm.blockMobFarms) {
			ItemBlock itemBlockMobFarm = new ItemBlockMobFarm(i);
			registry.register(itemBlockMobFarm);
			TinyMobFarm.itemBlockMobFarms.add(itemBlockMobFarm);
		}
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		TinyMobFarm.proxy.registerModel(TinyMobFarm.lasso, 0, "inventory");
		
		for (ItemBlock i: TinyMobFarm.itemBlockMobFarms) {
			TinyMobFarm.proxy.registerModel(i, 0, "inventory");
		}
	}
}
