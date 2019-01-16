package cn.davidma.idleloot.item.template;

import cn.davidma.idleloot.Main;
import cn.davidma.idleloot.handler.CollectionsManager;
import cn.davidma.idleloot.util.Registrable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StandardItemBase extends Item implements Registrable {

	private String name;
	
	public StandardItemBase(String name) {
		this.name = name;
		setUnlocalizedName(this.name);
		setRegistryName(this.name);
		//setCreativeTab(CreativeTabs.MISC);
		CollectionsManager.items.add(this);
	}

	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
