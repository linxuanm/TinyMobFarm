package cn.davidma.tinymobfarm.misc;

import cn.davidma.tinymobfarm.handler.CollectionsManager;
import cn.davidma.tinymobfarm.item.template.StandardItemBase;
import cn.davidma.tinymobfarm.reference.Info;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TinyMobFarmCreativeTab extends CreativeTabs {

	public TinyMobFarmCreativeTab() {
		super(Info.MOD_ID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() {
		for (Item i: CollectionsManager.items) {
			System.out.println(i.getUnlocalizedName());
			if (i.getUnlocalizedName().equals("tile.iron_farm")) return new ItemStack(i);
		}
		return new ItemStack(Items.ROTTEN_FLESH);
	}
}
