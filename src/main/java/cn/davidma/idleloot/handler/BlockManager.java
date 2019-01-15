package cn.davidma.idleloot.handler;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.idleloot.block.StandardBlockBase;
import cn.davidma.idleloot.item.Lasso;
import cn.davidma.idleloot.item.template.StandardItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockManager {
	
	public static List<Block> blocks = new ArrayList<Block>();;

	public static void instantiateAllBlocks() {
		new StandardBlockBase("wood_generator", Material.WOOD);
	}
}
