package cn.davidma.idleloot.items;

import cn.davidma.idleloot.Main;
import net.minecraft.item.Item;

public class StandardItemBase extends Item {

	public StandardItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		//setCreativeTab(CreativeTabs.MISC);
	}

	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
