package cn.davidma.tinymobfarm.block.template;

import cn.davidma.tinymobfarm.Main;
import cn.davidma.tinymobfarm.handler.CollectionsManager;
import cn.davidma.tinymobfarm.item.template.StandardItemBase;
import cn.davidma.tinymobfarm.item.template.TooltipItemBlock;
import cn.davidma.tinymobfarm.util.Registrable;
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
	
	public StandardBlockBase(String name, Material material, int id) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		CollectionsManager.blocks.add(this);
		CollectionsManager.items.add(new TooltipItemBlock(this, id).setRegistryName(name));
		setCreativeTab(CollectionsManager.tab);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
