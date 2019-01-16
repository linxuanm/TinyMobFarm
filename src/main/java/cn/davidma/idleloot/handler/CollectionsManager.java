package cn.davidma.idleloot.handler;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.idleloot.block.template.GeneratorBase;
import cn.davidma.idleloot.item.Lasso;
import cn.davidma.idleloot.item.template.InteractiveMobTool;
import cn.davidma.idleloot.item.template.StandardItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class CollectionsManager {
	
	public static List<Item> items = new ArrayList<Item>();
	public static List<Block> blocks = new ArrayList<Block>();
	
	public static void instantiateAllItems() {
		new Lasso("lasso");
	}

	public static void instantiateAllBlocks() {
		new GeneratorBase("wood_generator", Material.WOOD, SoundType.WOOD, 2.0F, "axe", 0);
	}
}
