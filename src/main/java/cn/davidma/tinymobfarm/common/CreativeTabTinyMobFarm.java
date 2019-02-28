package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabTinyMobFarm extends CreativeTabs {

	public CreativeTabTinyMobFarm() {
		super(Reference.MOD_ID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(TinyMobFarm.blockMobFarms.get(4));
	}
}
