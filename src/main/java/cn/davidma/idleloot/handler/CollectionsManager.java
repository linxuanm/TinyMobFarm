package cn.davidma.idleloot.handler;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.idleloot.block.template.GeneratorBase;
import cn.davidma.idleloot.item.Lasso;
import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.item.template.StandardItemBase;
import cn.davidma.idleloot.misc.IdleLootCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class CollectionsManager {
	
	public static IdleLootCreativeTab tab = new IdleLootCreativeTab();
	public static List<Item> items = new ArrayList<Item>();
	public static List<Block> blocks = new ArrayList<Block>();
	
	public static void instantiateAllItems() {
		new Lasso("lasso");
	}

	public static void instantiateAllBlocks() {
		new GeneratorBase("wood_generator", Material.WOOD, SoundType.WOOD, 2.0F, "axe", 0);
		new GeneratorBase("stone_generator", Material.ROCK, SoundType.STONE, 1.5F, "pickaxe", 0);
		new GeneratorBase("iron_generator", Material.IRON, SoundType.METAL, 5.0F, "pickaxe", 1);
		new GeneratorBase("gold_generator", Material.IRON, SoundType.METAL, 3.0F, "pickaxe", 1);
		new GeneratorBase("diamond_generator", Material.IRON, SoundType.STONE, 5.0F, "pickaxe", 2);
		new GeneratorBase("emerald_generator", Material.IRON, SoundType.STONE, 5.0F, "pickaxe", 2);
		new GeneratorBase("inferno_generator", Material.IRON, SoundType.STONE, 50.0F, "pickaxe", 3);
		new GeneratorBase("ultimate_generator", Material.IRON, SoundType.STONE, 50.0F, "pickaxe", 3);
	}
}
