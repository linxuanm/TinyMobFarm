package cn.davidma.tinymobfarm.core;

import cn.davidma.tinymobfarm.common.TinyMobFarm;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class TinyMobFarmCreativeTab extends ItemGroup {

	public TinyMobFarmCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(TinyMobFarm.mobFarms.get(2));
	}
}
