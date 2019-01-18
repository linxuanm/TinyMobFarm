package cn.davidma.tinymobfarm.item.template;

import cn.davidma.tinymobfarm.Main;
import cn.davidma.tinymobfarm.handler.CollectionsManager;
import cn.davidma.tinymobfarm.util.Registrable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StandardItemBase extends Item implements Registrable {

	private String name;
	
	public StandardItemBase(String name) {
		this.name = name;
		setUnlocalizedName(this.name);
		setRegistryName(this.name);
		CollectionsManager.items.add(this);
		setCreativeTab(CollectionsManager.tab);
	}
	
	public String getAssignedName() {
		return name;
	}

	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
