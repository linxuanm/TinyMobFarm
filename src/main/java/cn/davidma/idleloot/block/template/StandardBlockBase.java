package cn.davidma.idleloot.block.template;

import cn.davidma.idleloot.Main;
import cn.davidma.idleloot.handler.CollectionsManager;
import cn.davidma.idleloot.item.template.StandardItemBase;
import cn.davidma.idleloot.util.Registrable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class StandardBlockBase extends Block implements Registrable {

	public StandardBlockBase(String name, Material material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		CollectionsManager.blocks.add(this);
		CollectionsManager.items.add(new ItemBlock(this).setRegistryName(name));
		setCreativeTab(CollectionsManager.tab);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
